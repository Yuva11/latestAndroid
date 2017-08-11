package hungrybells.com.hungrybells_v20.Activity.net;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpStatus;
import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import hungrybells.com.hungrybells_v20.Activity.app.HBLog;
import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;
import hungrybells.com.hungrybells_v20.Activity.entity.common.NoDataFoundResponse;
import hungrybells.com.hungrybells_v20.Activity.utils.Constants;

/**
 * Created by ajeetkumar on 17/05/15.
 */
public class HBGsonRequest extends Request<IDataModel> {

    private final String TAG = HBGsonRequest.class.getName();
    private final Response.Listener<IDataModel> mListener;
    private IDataModel mDataModel;
    private final Gson mGson;
    private Map<String, String> mHeaders;
    private Map<String, String> mParams;
    private final String mRequestBody;
    private String mUrl;

    /** Charset for request. */
    private static final String PROTOCOL_CHARSET = "utf-8";
    public static final int MY_SOCKET_TIMEOUT_MS = 30000;

    /** Content type for request. */
    private static final String PROTOCOL_CONTENT_TYPE = String.format(
            "application/json; charset=%s", PROTOCOL_CHARSET);

    public HBGsonRequest(String url, Response.Listener<IDataModel> listener,
                              Response.ErrorListener errorListener, IDataModel model,
                              Map<String, String> params, Map<String, String> headers,
                              String requestBody, int httpRequestMethod) {
        super(httpRequestMethod, url, errorListener);
        mListener = listener;
        mDataModel = model;
        mGson = new GsonBuilder().disableHtmlEscaping().serializeNulls().create();
        mParams = params;
        mHeaders = headers;
        mRequestBody = requestBody;
        mUrl = url;

        HBLog.d(TAG, "Url: " + url);
        HBLog.d(TAG, "RequestBody: " + requestBody + " Request method: " + httpRequestMethod + " params:" + params);
    }

    @Override
    public Request<?> setRetryPolicy(RetryPolicy retryPolicy) {
        retryPolicy = new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        return super.setRetryPolicy(retryPolicy);
    }

    @Override
    protected void deliverResponse(IDataModel model) {
        mListener.onResponse(model);
    }

    @Override
    protected Response<IDataModel> parseNetworkResponse(NetworkResponse response) {
        String jsonString = new String(response.data);
        HBLog.d(TAG, "json Response String: " + jsonString + " statusCode: " + response.statusCode);
        try {
            if (jsonString == null || jsonString.trim().length() == 0) {
                throw new IllegalStateException();
            }

            // Modifying this part to accommodate the old api results
            // 1. check if the json string is an array, cause then it might be the a result of nearest loaction api call
            if (jsonString.charAt(0) == '[') {
                jsonString = "{\"locations\":" + jsonString + "}";
                HBLog.d(TAG, "json Response String: " + jsonString + " statusCode: " + response.statusCode);
            }

            return Response.success(
                    (IDataModel) mGson.fromJson(jsonString,
                            mDataModel.getClass()), getCacheEntry());

        } catch (Exception exception) {

            exception.printStackTrace();

            // Work around for till the server starts returning failure calls
            if (response.statusCode == 200 || response.statusCode == 500) {
                return Response.success((IDataModel)new NoDataFoundResponse(response.statusCode), getCacheEntry());
            }
            VolleyError volleyError = new VolleyError(Constants.PARSE_ERROR);
            volleyError.setUrl(mUrl);
            return Response.error(volleyError);
        }
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams != null ? mParams : super.getParams();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders != null ? mHeaders : super.getHeaders();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        try {
            return mRequestBody == null ? null : mRequestBody
                    .getBytes(PROTOCOL_CHARSET);
        } catch (UnsupportedEncodingException uee) {

            return null;
        }
    }

    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    /**
     * @deprecated Use {@link #getBody()}.
     */
    @Override
    public String getPostBodyContentType() {
        return getBodyContentType();
    }

    /**
     * @throws AuthFailureError
     * @deprecated Use {@link #getBody()}.
     */
    @Override
    public byte[] getPostBody() throws AuthFailureError {
        return getBody();
    }
}
