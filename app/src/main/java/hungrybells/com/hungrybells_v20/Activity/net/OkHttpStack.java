package hungrybells.com.hungrybells_v20.Activity.net;

import com.android.volley.toolbox.HurlStack;
import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

/**
 * Created by ajeetkumar on 17/05/15.
 */
public class OkHttpStack extends HurlStack {
    private final OkHttpClient mClient;

    public OkHttpStack() {
        this(new OkHttpClient());
    }

    public OkHttpStack(OkHttpClient client) {
        if (client == null) {
            throw new NullPointerException("OkHttpClient Client must not be null.");
        }
        mClient = client;
        mClient.setConnectTimeout(30000, TimeUnit.MILLISECONDS);
        mClient.setReadTimeout(30000, TimeUnit.MILLISECONDS);
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, null, null);
        } catch (GeneralSecurityException e) {
            throw new AssertionError(); // The system has no TLS. Just give up.
        }
        mClient.setSslSocketFactory(sslContext.getSocketFactory());
        //URL.setURLStreamHandlerFactory(this.client);
    }

    @Override protected HttpURLConnection createConnection(URL url) throws IOException {
        return mClient.open(url);
    }
}

