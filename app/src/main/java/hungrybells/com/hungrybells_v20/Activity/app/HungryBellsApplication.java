package hungrybells.com.hungrybells_v20.Activity.app;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
import com.localytics.android.LocalyticsActivityLifecycleCallbacks;


import io.fabric.sdk.android.Fabric;
import java.util.HashMap;

import hungrybells.com.hungrybells_v20.Activity.utils.Constants;
import hungrybells.com.hungrybells_v20.R;
import io.branch.referral.Branch;
import io.branch.referral.BranchApp;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by riteshranjan on 01/06/15.
 */
public class HungryBellsApplication extends BranchApp {


     /*
   * Google Analytics configuration values.
   */
    // Placeholder property ID.

    private static final String PROPERTY_ID = "UA-64050386-1";

    // Dispatch period in seconds.
    private static final int GA_DISPATCH_PERIOD = 30;

    // Prevent hits from being sent to reports, i.e. during testing.
    private static final boolean GA_IS_DRY_RUN = Constants.ANALYTICS_DEBUG;

    public static int GENERAL_TRACKER = 0;

    public enum TrackerName {
        APP_TRACKER,
        GLOBAL_TRACKER,
        ECOMMERCE_TRACKER,
    }

    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();


    public HungryBellsApplication(){
        super();
    }

    @Override
    public void onCreate() {

        FacebookSdk.sdkInitialize(getApplicationContext());

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("GothamRounded-Book.otf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

        // Register LocalyticsActivityLifecycleCallbacks
        registerActivityLifecycleCallbacks(
                new LocalyticsActivityLifecycleCallbacks(this));

        Branch.getAutoInstance(this);

        super.onCreate();
        Fabric.with(this, new Crashlytics());



    }


    public synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {


            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);

            GoogleAnalytics.getInstance(this).setDryRun(GA_IS_DRY_RUN);


            analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID)
                    : (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(
                    R.xml.global_tracker)
                    : analytics.newTracker(R.xml.ecommerce_tracker);
            t.enableAdvertisingIdCollection(true);
            mTrackers.put(trackerId, t);
        }
        return mTrackers.get(trackerId);
    }

}
