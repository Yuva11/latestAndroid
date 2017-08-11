package hungrybells.com.hungrybells_v20.Activity.app;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import hungrybells.com.hungrybells_v20.Activity.net.OkHttpStack;

/**
 * Created by ajeetkumar on 17/05/15.
 */
public class HBVolley {

    private static final Object TAG = HBVolley.class.getSimpleName();
    private static RequestQueue mRequestQueue;
    private static ImageLoader mImageLoader;
    private static Context mContext;

    private HBVolley() {
    }

    /**
     * Initialize Volley Request Queue.
     *
     * @param context
     *            Application Context.
     */
    public static void init(Context context) {
        mContext = context;
        mRequestQueue = Volley.newRequestQueue(context, new OkHttpStack());
    }

    /**
     * Method to get the Volley Request Queue.
     *
     * @param context
     *            Application Context.
     * @return Request Queue
     */
    public static RequestQueue getRequestQueue(Context context) {
        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            return mRequestQueue = Volley.newRequestQueue(context,
                    new OkHttpStack());
        }
    }

    public static ImageLoader getImageLoader() {
        getRequestQueue(mContext);
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(mRequestQueue,
                    new LruBitmapCache());
        }
        return mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue(mContext).add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue(mContext).add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
