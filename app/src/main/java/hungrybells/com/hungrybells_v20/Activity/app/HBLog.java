package hungrybells.com.hungrybells_v20.Activity.app;

import android.util.Log;

import hungrybells.com.hungrybells_v20.Activity.utils.Constants;

/**
 * Created by ajeetkumar on 17/05/15.
 */
public class HBLog {

    public static void d(String tag, String message) {
        if (Constants.DEBUG)
            Log.d(tag, message);
    }

    public static void i(String tag, String message) {
        if (Constants.DEBUG)
            Log.i(tag, message);
    }

    public static void v(String tag, String message) {
        if (Constants.DEBUG)
            Log.v(tag, message);
    }

}
