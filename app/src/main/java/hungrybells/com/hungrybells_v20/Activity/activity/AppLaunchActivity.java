package hungrybells.com.hungrybells_v20.Activity.activity;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.format.Time;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.localytics.android.Localytics;
import com.mobileapptracker.MATEvent;
import com.mobileapptracker.MobileAppTracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hungrybells.com.hungrybells_v20.Activity.app.HBLog;
import hungrybells.com.hungrybells_v20.Activity.app.HBVolley;
import hungrybells.com.hungrybells_v20.Activity.app.HungryBellsApplication;
import hungrybells.com.hungrybells_v20.Activity.entity.category.CategoryListResponse;
import hungrybells.com.hungrybells_v20.Activity.entity.food_tags_list.CategoryList;
import hungrybells.com.hungrybells_v20.Activity.entity.category.CategoryListRequest;
import hungrybells.com.hungrybells_v20.Activity.entity.change_location.Location;
import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;
import hungrybells.com.hungrybells_v20.Activity.entity.common.NoDataFoundResponse;
import hungrybells.com.hungrybells_v20.Activity.entity.common.RequestObj;
import hungrybells.com.hungrybells_v20.Activity.entity.dealList.Deals;
import hungrybells.com.hungrybells_v20.Activity.entity.dealList.DealsListRequest;
import hungrybells.com.hungrybells_v20.Activity.entity.dealList.DealsListResponse;
import hungrybells.com.hungrybells_v20.Activity.entity.feedback.CheckFeedbackRequest;
import hungrybells.com.hungrybells_v20.Activity.entity.feedback.CheckFeedbackResponse;
import hungrybells.com.hungrybells_v20.Activity.entity.food_tags_list.FavTagList;
import hungrybells.com.hungrybells_v20.Activity.entity.food_tags_list.Favourites;
import hungrybells.com.hungrybells_v20.Activity.entity.food_tags_list.FoodTag;
import hungrybells.com.hungrybells_v20.Activity.entity.food_tags_list.FoodTagListReqest;
import hungrybells.com.hungrybells_v20.Activity.entity.food_tags_list.TagsList;
import hungrybells.com.hungrybells_v20.Activity.net.HBGsonRequest;
import hungrybells.com.hungrybells_v20.Activity.utils.Constants;
import hungrybells.com.hungrybells_v20.Activity.utils.GPSTracker;
import hungrybells.com.hungrybells_v20.Activity.utils.local_noitification.LocalNotificationReceiver;
import hungrybells.com.hungrybells_v20.R;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/*
import com.quantumgraph.sdk.QG;

*/

public class AppLaunchActivity extends ActionBarActivity implements
        Response.Listener<IDataModel>, Response.ErrorListener {

    //region ## VARIABLS
    private TextView mToolbarTitle;
    private MenuItem locationMenuItem;
    private static CategoryListResponse categoryList;
    private static TagsList mTagList;
    private static FavTagList favTagList;
    private static DealsListResponse dealListResponse;

    private static long back_pressed;
    private static long lastBackPressTime;
    private Toast toast;
    private boolean getLatLongInProcess = false;
    // New 3 second gps detection logic vaeriables

    private Timer gpsDetectionTimer = new Timer();
    private TimerTask gpstask;
    private int gpsDetectionRetryCount = 0;

  /*  private static QG qg;
*/
    private Runnable gpsRunnable = new Runnable() {
        @Override
        public void run() {
            if (gpsDetectionRetryCount >= 5) {
                showNoLocationFound();
            } else {
                gpsDetectionRetryCount++;
                GPSTracker deviceGPS = new GPSTracker(AppLaunchActivity.this);
                if (deviceGPS.canGetLocation()) {
                    double latitude = deviceGPS.getLatitude();
                    double longitude = deviceGPS.getLongitude();
                    if (latitude == 0.0 && longitude == 0.0) {
                        //showNoLocationFound();
                        handler.postDelayed(gpsRunnable, 1000);
                    } else {
                        // Update all related variables
                        if (Constants.shouldUpldatePlaceLatLong) {
                            Constants.LATITUDE = String.valueOf(latitude);
                            Constants.LONGITUDE = String.valueOf(longitude);
                        }
                        // Saves device location in a separate variable as above variable changes whenever user selects another location from the menu
                        Constants.DEVICE_LAT = String.valueOf(latitude);
                        Constants.DEVICE_LONG = String.valueOf(longitude);

                        findCityName(latitude, longitude);
                        getTagsListFromServer();
                        getFavTagsListFromServer();
                        getCategories();
                        loadAutoSuggestions();
                        gpsDetectionTimer.cancel();
                        gpsDetectionTimer.purge();
                        // getLatLongInProcess = false;
                    }
                } else {
                    handler.postDelayed(gpsRunnable, 1000);
                }
            }
        }
    };

    private void getCategories() {
        // categoriesLayout.setVisibility(View.GONE);
        categoriesLayout.setVisibility(View.GONE);
        //Toast.makeText(getApplicationContext(),"loading categories",Toast.LENGTH_LONG).show();

        if (isNetworkConnected()) {
            if ((categoryList == null || categoryList.getCategoryList() == null) && !isFetchingDataFromServer) {

                isFetchingDataFromServer = true;

                showProgressDialog();

                CategoryListRequest categoryListRequest = new CategoryListRequest();
                categoryListRequest.setLongitude(Constants.LONGITUDE);
                categoryListRequest.setLatitude(Constants.LATITUDE);
                categoryListRequest.setUserId(Constants.USER_ID);
               // HBLog.i("Info", categoryListRequest);

/*
                RequestObj req = new RequestObj();
                req.setBody(categoryListRequest);
*/

                Gson gson = new Gson();
                HBLog.i("Info", gson.toJson(categoryListRequest));
/*
                System.out.println(gson.toJson(req));
*/

                String jsonStr = gson.toJson(categoryListRequest);

                HBVolley.getRequestQueue(getApplicationContext()).add(
                        new HBGsonRequest(
                                Constants.CATEGORY_LIST_URL,
                                this,
                                this,
                                new CategoryListResponse(),
                                null,
                                null,
                                jsonStr,
                                Request.Method.POST
                        )).setShouldCache(true);


                // Create a time-out. This is used to check wheather the data has been returned from server or not and if not would dismiss the loading
                // screen and show the retry layout
                handler.postDelayed(runnable, Constants.INTERVAL);
            } else {
                // update UI with saved Data
                //updateUI(mTagList);
               updateCategories(categoryList);
            }
        } else {
            retryConnectionLayout.setVisibility(View.VISIBLE);
        }
    }

    private void loadAutoSuggestions() {
        DealsListRequest dealsListRequest = new DealsListRequest();
        dealsListRequest.setLongitude(Constants.LONGITUDE);
        dealsListRequest.setLatitude(Constants.LATITUDE);
        dealsListRequest.setUser_id(Constants.USER_ID);
        dealsListRequest.setSearchString("");

        RequestObj req = new RequestObj();
        req.setBody(dealsListRequest);

        Gson gson = new Gson();
        HBLog.i("loading auto suggestion", gson.toJson(req));
        HBVolley.getRequestQueue(getApplicationContext()).add(
                new HBGsonRequest(
                        Constants.SEARCH_TAGS_URL,
                        this,
                        this,
                        new DealsListResponse(),
                        null,
                        null,
                        gson.toJson(req),
                        Request.Method.POST
                )).setShouldCache(true);
    }

    Tracker t;

    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();

    private ProgressDialog progressDialog;
    private RelativeLayout retryConnectionLayout;
    private LinearLayout outsideLocationLayout;

    private boolean isAppActiveInCurrentCity = true;
    private boolean isAppActiveInCurrentSubLocality = true;
    private boolean isFetchingDataFromServer = false;   // it's set true when calling server and set false when call is completed. It helps in reducing call redundancy
    private boolean isNoLocationDialogVisible = false;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        public void run() {
            // Check if progress dialog is till showing and whether or not data has been recieved by the app or not
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();

                // API call hasnt gone as expected, enable the retry UI
                if (isAppActiveInCurrentCity) retryConnectionLayout.setVisibility(View.VISIBLE);
            }
        }
    };

    // This is for local notification
    private PendingIntent pendingIntent;

    public MobileAppTracker mobileAppTracker = null;    // Mobile App Tracking - MAT
    private String deviceId;
    private String primaryEmailAddress;
    private AlarmManager am;

    //region Inject views

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @InjectView(R.id.total_food_items_count)
    TextView mTotalFoodCount;

    @InjectView(R.id.location_text)
    TextView mLocationName;

    @InjectView(R.id.categoriesList)
    LinearLayout categoriesLayout;

    @InjectView(R.id.trending_list)
    LinearLayout mTrendingList;

    @InjectView(R.id.recommended_list)
    LinearLayout mRecommendedList;

    @InjectView(R.id.favourites_list)
    LinearLayout mFavoritesList;


    //Category Text
    @InjectView(R.id.category1)
    TextView categoryButton1;

    @InjectView(R.id.category2)
    TextView categoryButton2;

    @InjectView(R.id.category3)
    TextView categoryButton3;

    @InjectView(R.id.category4)
    TextView categoryButton4;

    @InjectView(R.id.category5)
    TextView categoryButton5;

    @InjectView(R.id.category6)
    TextView categoryButton6;

    @InjectView(R.id.category7)
    TextView categoryButton7;

    @InjectView(R.id.category8)
    TextView categoryButton8;

    @InjectView(R.id.category9)
    TextView categoryButton9;

    // Trending Text
    @InjectView(R.id.trending_text1)
    TextView mTrendingText1;

    @InjectView(R.id.trending_text2)
    TextView mTrendingText2;

    @InjectView(R.id.trending_text3)
    TextView mTrendingText3;

    @InjectView(R.id.trending_text4)
    TextView mTrendingText4;

    @InjectView(R.id.trending_text5)
    TextView mTrendingText5;

    @InjectView(R.id.trending_text6)
    TextView mTrendingText6;

    @InjectView(R.id.trending_text7)
    TextView mTrendingText7;

    @InjectView(R.id.trending_text8)
    TextView mTrendingText8;

    @InjectView(R.id.trending_text9)
    TextView mTrendingText9;

    //Recommended Text
    @InjectView(R.id.recommended_text1)
    TextView mRecommendedText1;

    @InjectView(R.id.recommended_text2)
    TextView mRecommendedText2;

    @InjectView(R.id.recommended_text3)
    TextView mRecommendedText3;

    @InjectView(R.id.recommended_text4)
    TextView mRecommendedText4;

    @InjectView(R.id.recommended_text5)
    TextView mRecommendedText5;

    @InjectView(R.id.recommended_text6)
    TextView mRecommendedText6;

    @InjectView(R.id.recommended_text7)
    TextView mRecommendedText7;

    @InjectView(R.id.recommended_text8)
    TextView mRecommendedText8;

    @InjectView(R.id.recommended_text9)
    TextView mRecommendedText9;


    //Favourites Text
    @InjectView(R.id.favourites_text1)
    TextView mFavouritesText1;

    @InjectView(R.id.favourites_text2)
    TextView mFavouritesText2;

    @InjectView(R.id.favourites_text3)
    TextView mFavouritesText3;

    @InjectView(R.id.favourites_text4)
    TextView mFavouritesText4;

    @InjectView(R.id.favourites_text5)
    TextView mFavouritesText5;

    @InjectView(R.id.favourites_text6)
    TextView mFavouritesText6;

    @InjectView(R.id.favourites_text7)
    TextView mFavouritesText7;

    @InjectView(R.id.favourites_text8)
    TextView mFavouritesText8;

    @InjectView(R.id.favourites_text9)
    TextView mFavouritesText9;

    @InjectView(R.id.food_search)
    MultiAutoCompleteTextView mFoodSeachEditText;
    //EditText mFoodSeachEditText;

    /*
    @InjectView(R.id.show_all_foods_btn)
    Button mShowAllFoodsBtn;
    */
    @InjectView(R.id.guide_layout)
    RelativeLayout mGuideLayout;


    @InjectView(R.id.no_data_layout)
    LinearLayout mNoDataLayout;

    @InjectView(R.id.contextual_tag)
    Button mContextualButton;

    @InjectView(R.id.contextual_layout)
    LinearLayout mContextuallayout;

    @InjectView(R.id.credit_balance_tv)
    TextView mCreditBanalanceTV;

    @InjectView(R.id.closed_textview)
    TextView mClosedTextView;

    //endregion

    private TextView appRatingTitleTextView;
    private Button appRatingLeftButton;
    private Button appRatingRightButton;
    private Integer appRatingStage;

    Branch branch;

    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_launch);

        ButterKnife.inject(this);
        setupToolbar();

        setupNavDrawer();

        //Get Saved User ID & Add it to Constants.USER_ID
        getSavdUserID();

        checkHungryBellsOperationTime();

        // get new location logic
        //region Android Device ID and primary email address
        this.deviceId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        try {
            //Retrieving user's email address
            Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
            Account[] accounts = AccountManager.get(getApplicationContext()).getAccounts();
            for (Account account : accounts) {
                if (emailPattern.matcher(account.name).matches()) {
                    String possibleEmail = account.name;
                    if (!possibleEmail.isEmpty() && possibleEmail.contains("@")) {
                        this.primaryEmailAddress = possibleEmail;
                        Constants.DEVICE_EMAIL = possibleEmail;
                    }
                }
            }

            //Retrieving user's phone number
            TelephonyManager tMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            Constants.DEVICE_PHONE_NO = tMgr.getLine1Number();

        } catch (Exception e) {
            // Log it somewhere
        }
        //endregion

        //region Initializing the MAT SDK
        mobileAppTracker = MobileAppTracker.init(getApplicationContext(), Constants.MAT_ADVERTISING_KEY, Constants.MAT_CONVERSION_KEY);
        mobileAppTracker.setDebugMode(Constants.DEBUG);
        //mobileAppTracker.setUserId(this.deviceId);
        //mobileAppTracker.setAndroidId(this.deviceId);

        // Marking all existing users as exiting in the MAT system also
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String isExistingUser = sharedPref.getString(getString(R.string.first_launch), "true");
        if (!isExistingUser.equals("true")) {
            mobileAppTracker.setExistingUser(true);
        } else {
            mobileAppTracker.measureEvent(MATEvent.REGISTRATION);
        }
        //endregion

        //region Initialize Google Analytics
        try {
            t = ((HungryBellsApplication) getApplication()).getTracker(HungryBellsApplication.TrackerName.APP_TRACKER);
            t.setScreenName("Landing View");
            t.send(new HitBuilders.AppViewBuilder().build());
        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(), "Error" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        //endregion

        // Localytics
        Localytics.tagEvent("Homepage Viewed");

        //Guide Layout
        showGuideLayoutForFirstTime();

        // set trending, recomended and fav list to visibility gone
        mTrendingList.setVisibility(View.GONE);
        mRecommendedList.setVisibility(View.GONE);
        mFavoritesList.setVisibility(View.GONE);
        categoriesLayout.setVisibility(View.GONE);


        retryConnectionLayout = (RelativeLayout) findViewById(R.id.retry_connection_layout);
        retryConnectionLayout.setVisibility(View.GONE);

        outsideLocationLayout = (LinearLayout) findViewById(R.id.outside_location_layout);
        outsideLocationLayout.setVisibility(View.GONE);

     /*   List<Deals> listItem=new ArrayList<Deals>();
        for(int i=0;i<listItem.size();i++) {
            dishItems=listItem.get(i).getDealTag();
        }
*/
        // add search event handler
        mFoodSeachEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });

        /*
        if (!Constants.IS_APP_SESSION_ACTIVE) {

            // Get Latitude and Longtitude
            if (locationDataNotFound()){
                getLatLongPosition();
            }

            Constants.IS_APP_SESSION_ACTIVE = true;
        }
        */

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Constants.selectedLocation)) {
            Location nearestLocation = (Location) intent.getSerializableExtra(Constants.selectedLocation);
            Constants.LONGITUDE = nearestLocation.getLongitude();
            Constants.LATITUDE = nearestLocation.getLatitude();

            Constants.LOCATION_NAME = nearestLocation.getLocationName();
            mLocationName.setText(Constants.LOCATION_NAME);

            mTagList = null;

            // resetting the tag list so that the new data is loaded in it

            getTagsListFromServer();

            favTagList =null;
            getFavTagsListFromServer();

            categoryList=null;
            getCategories();

            //Load auto suggestion for deal names
            loadAutoSuggestions();
        } else {
            //Get Saved User ID & Add it to Constants.USER_ID
            getSavdUserID();

            if (!Constants.DEVICE_LAT.isEmpty() && !Constants.DEVICE_LONG.isEmpty()) {
                getTagsListFromServer();
                getFavTagsListFromServer();
                getCategories();
                loadAutoSuggestions();
            }

        }

        //show contextual banner - breakfast/lunch/dinner/nightfood
        showContextualTag();

        // Setup daily local notification
        //setLocalAppNotification();

        // Get data from server
        //getTagsListFromServer();

        checkForUserFeedback();
    }

    private void showAutoSugestion(String[] dishItems) {
        mFoodSeachEditText.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,dishItems ));
        mFoodSeachEditText.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }


    @Override
    public void onStart() {
        super.onStart();

        //QGraph push notification
     /*   QG qg = QG.getInstance(getApplicationContext());
        qg.onStart("98473a0bafe301c3cd04", "588516395889");
*/
        branch = Branch.getInstance();
        branch.initSession(new Branch.BranchReferralInitListener() {
            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                if (error == null) {
                    // params are the deep linked params associated with the link that the user clicked before showing up
                    Log.i("BranchConfigTest", "deep link data: " + referringParams.toString());
                } else {
                    Log.i("HungryBells", error.getMessage());
                }
            }
        }, true, this.getIntent().getData(), this);

        branch.setIdentity(this.primaryEmailAddress);

        branch.loadRewards(new Branch.BranchReferralStateChangedListener() {
            @Override
            public void onStateChanged(boolean b, BranchError branchError) {
                int credits = Branch.getInstance().getCredits();
                updateHungryBellsMoney(credits);
            }
        });
        getHungryBellsMoney();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }


    // TODO - GET LOGCATION LOGIC _ REFRESH -[ WORKING]

    private void getDeviceLocation() {

        gpsDetectionRetryCount = 0;
        handler.postDelayed(gpsRunnable, 0);

    }


    @Override
    protected void onRestart() {

        super.onRestart();
        // refresh location & data
        //getLatLongPosition();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //  mFavoritesList.setVisibility(View.GONE);
        if (Constants.shouldUpldatePlaceLatLong)
            getDeviceLocation();
        else {
            getTagsListFromServer();
            getCategories();
            getFavTagsListFromServer();
            loadAutoSuggestions();
        }

        AppEventsLogger.activateApp(this);
        // Get source of open for app re-engagement
        mobileAppTracker.setReferralSources(this);
        // MAT will not function unless the measureSession call is included
        mobileAppTracker.measureSession();

        Localytics.openSession();
        Localytics.tagScreen("Home");
        Localytics.upload();
        showContextualTag();

        // Check if the device has got the location or not and then call the getTagList from server
        /*
        if (locationDataNotFound()) {

            getLatLongPosition();

        }
        */
        DealsListActivity.mDealsListResponse=null;
    }


    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }


    private void showContextualTag() {
        Date date = new Date();   // given date
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30")); // creates a new calendar instance
        calendar.setTime(date);   // assigns calendar to given date
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        mContextualButton.setVisibility(View.VISIBLE);
        mContextualButton.setBackgroundResource(R.drawable.lunch);

        if (hours >= 6 && hours < 10) {
            mContextualButton.setText("BREAKFAST");
            mContextualButton.setBackgroundResource(R.drawable.breakfast);
        } else if (hours >= 11 && hours < 15) {

            mContextualButton.setText("LUNCH");
        } else if (hours >= 19 && hours <= 22) {

            mContextualButton.setText("DINNER");
        } else if (hours >= 23 && hours < 1) {
            mContextualButton.setText("LATE NIGHT FOOD");
        } else {
            mContextualButton.setVisibility(View.GONE);
        }
    }


    public void contextualBtnClicked(View v) {
        Button b = (Button) v;
        String buttonText = b.getText().toString();
        String tagString = buttonText.toLowerCase();

        if (buttonText.equals("LATE NIGHT FOOD")) {
            tagString = "Latenight";
        }


        Constants.FOOD_TAG_VARIABLE = tagString;

        t.send(new HitBuilders.EventBuilder()
                .setCategory("CONTEXTUAL_FOOD")
                .setAction(Constants.LOCATION_NAME)
                .setLabel(tagString.toString())
                .build());

        //Go to Deal List Activity
        Intent intent = new Intent(AppLaunchActivity.this, DealsListActivity.class);
        intent.putExtra(Constants.FOOD_TAG, tagString);
        startActivity(intent);


    }

/*
    private void getLatLongPosition() {


        if (!getLatLongInProcess) {

            getLatLongInProcess = true;

            showProgressDialog();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // after 2 sec wait
                    GPSTracker gps = new GPSTracker(AppLaunchActivity.this);

                    // check if GPS enabled
                    if (gps.canGetLocation()) {

                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();

                        if (latitude == 0.0 && longitude == 0.0) {
                            showNoLocationFound();
                        } else {
                            // Update all related variables
                            Constants.LATITUDE = String.valueOf(latitude);
                            Constants.LONGITUDE = String.valueOf(longitude);

                            // Saves device location in a separate variable as above variable changes whenever user selects another location from the menu
                            Constants.DEVICE_LAT = String.valueOf(latitude);
                            Constants.DEVICE_LONG = String.valueOf(longitude);
                        }

                        findCityName(latitude, longitude);

                        getTagsListFromServer();

                        getLatLongInProcess = false;

                    } else {
                        // can't get location
                        // GPS or Network is not enabled
                        // Ask user to enable GPS/network in settings
                        //  gps.showSettingsAlert();
                        // Toast.makeText(getApplicationContext(), "Location not found", Toast.LENGTH_LONG).show();
                        showNoLocationFound();
                    }
                }

            }, 2000);

        }

    }
    */

    private void getSavdUserID() {
        SharedPreferences sharedPref = AppLaunchActivity.this.getPreferences(Context.MODE_PRIVATE);
        String user_id = sharedPref.getString(getString(R.string.user_id), "null");

        // set user_id to constants if the user exists
        if (user_id != "null") {
            Constants.USER_ID = user_id;
        }
    }

    private void saveNewUserId(String user_id) {
        SharedPreferences sharedPref = AppLaunchActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.user_id), user_id);
        editor.commit();
    }

    private void performSearch() {
        if (isAppActiveInCurrentCity) {

            if (isAppActiveInCurrentSubLocality) {

                /*

                //Track Search Querry
                JSONObject props = new JSONObject();
                try {
                    props.put("QueryString", mFoodSeachEditText.getText().toString());
                    props.put("Locality", Constants.LOCATION_NAME);
                }catch (Exception e)
                {

                }

                mixpanel.track("Search", props);
                */


                // Build and send an Event.
                t.send(new HitBuilders.EventBuilder()
                        .setCategory("SEARCH")
                        .setAction(Constants.LOCATION_NAME.toString())
                        .setLabel(mFoodSeachEditText.getText().toString())
                        .build());

                //Go to Deal List Activity
                Intent intent = new Intent(AppLaunchActivity.this, DealsListActivity.class);
                intent.putExtra(Constants.FOOD_TAG, mFoodSeachEditText.getText().toString());
                intent.putExtra("SEARCH_OPERATION", true);
                startActivity(intent);
            } else {
                showNoDealsInLocality();
            }
        } else {
            showOutOfCityMessage();
        }


    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_menu);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        mToolbarTitle.setText("Hungry Bells");

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_app_launch, menu);
        locationMenuItem = menu.findItem(R.id.location);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.location) {
            navigateToChangeLocationActivity();
        }

        // Pass the event to ActionBarDrawerToggle
        // If it returns true, then it has handled
        // the nav drawer indicator touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    //region Volley Response Handlers
    @Override
    public void onResponse(IDataModel dataModel) {
        //Toast.makeText(getApplicationContext(),"Server responded:"+dataModel.toString(),Toast.LENGTH_LONG).show();
        HBLog.i("Response",dataModel.toString());
        if (dataModel instanceof TagsList) {
            isAppActiveInCurrentSubLocality = true;
            HBLog.i("Info", ((TagsList) dataModel).toString());
            mTagList = (TagsList) dataModel;
            if (mTagList.getResult() == null) {
                isAppActiveInCurrentSubLocality = false;
                if (isAppActiveInCurrentCity) {
                    if (Constants.DEVICE_LAT.equals(""))
                        showNoLocationFound();
                    else
                        showNoDealsInLocality();
                } else {
                    if (Constants.DEVICE_LAT.equals(""))
                        showNoLocationFound();
                    else
                        showOutOfCityMessage();
                }
            } else {
                // data received from server, check and hide any and all no food layout
                updateUI(mTagList);
            }

            setupLocalyticsUser();
            retryConnectionLayout.setVisibility(View.GONE);
            outsideLocationLayout.setVisibility(View.GONE);

        }else if (dataModel instanceof CategoryListResponse) {
            categoryList= (CategoryListResponse) dataModel;
            if (categoryList==null ||categoryList.getCategoryList()== null || categoryList.getCategoryList().length==0) {
                categoriesLayout.setVisibility(View.GONE);
            }else
                updateCategories(categoryList);
        } else if (dataModel instanceof CheckFeedbackResponse) {
            CheckFeedbackResponse response = (CheckFeedbackResponse) dataModel;
            if (response.getCode().equals("1")) {
                // Show the user with feedback form page
                Intent feedbackIntent = new Intent(getApplicationContext(), FeedbackActivity.class);
                feedbackIntent.putExtra(Constants.ORDER_ID, response.getOrderid());
                startActivity(feedbackIntent);
            }
        } else if (dataModel instanceof FavTagList) {
            favTagList = (FavTagList) dataModel;
            if (favTagList==null ||favTagList.getFavourites() == null || favTagList.getFavourites().length==0) {
                mFavoritesList.setVisibility(View.GONE);
            }else
                updateFavTagsUI(favTagList);
        } else if(dataModel instanceof  DealsListResponse){
            dealListResponse=(DealsListResponse)dataModel;
            List<Deals> deals=dealListResponse.getResult().getDeals();
            String[] dealNames=new String[deals.size()];
            for(int i=0;i<deals.size();i++){
                dealNames[i]=deals.get(i).getName();
            }
            showAutoSugestion(dealNames);
        } else {

            // In case there is no data, check whether it is because of network issue or server has no data
            if (dataModel instanceof NoDataFoundResponse) {
                NoDataFoundResponse noDataFoundResponse = (NoDataFoundResponse) dataModel;
                if (noDataFoundResponse.getStatusCode() == 200) {
                    // API call executed successfully
                } else if (noDataFoundResponse.getStatusCode() == 500) {
                    // API call resulted into some exception on the server
                }

                isAppActiveInCurrentSubLocality = false;

                if (isAppActiveInCurrentCity) {
                    if (!isAppActiveInCurrentSubLocality) {
                        showNoDealsInLocality();    // No Deals in locality
                    }
                } else {
                    showOutOfCityMessage(); // User is out of "Bengaluru"
                }

            } else {
                // Mostly no network Issue
                if (isAppActiveInCurrentCity) retryConnectionLayout.setVisibility(View.VISIBLE);
            }

        }

        // Hide the Progress bar as the ui elements is setup correctly
        // Stop the progress dialog
        if (progressDialog!=null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }


        isFetchingDataFromServer = false;
    }

    @Override
    public void onErrorResponse(VolleyError error) {

        isFetchingDataFromServer = false;

    }

    //endregion

    // Update the UI after getting response from server

    private void updateUI(TagsList tagsList) {
        try {
            //show no data layout
            mNoDataLayout.setVisibility(View.GONE);

            //Set User id in Constants
            Constants.USER_ID = tagsList.getUser_id().toString();
            saveNewUserId(Constants.USER_ID);

            Constants.LOCATION_NAME = tagsList.getResult().getLocation();
            mTotalFoodCount.setText(tagsList.getResult().getTotal_food_items());
            mLocationName.setText(Constants.LOCATION_NAME);

            // Send App opened location
            t.send(new HitBuilders.EventBuilder()
                    .setCategory("Location")
                    .setAction("App Opened Location")
                    .setLabel(mLocationName.getText().toString())
                    .build());

            // Trending
            TextView[] trendingTextButtonArray = {mTrendingText1, mTrendingText2, mTrendingText3, mTrendingText4, mTrendingText5, mTrendingText6, mTrendingText7, mTrendingText8, mTrendingText9};
            for (int i = 0; i < trendingTextButtonArray.length; i++) {
                trendingTextButtonArray[i].setVisibility(View.GONE);
            }

            if (tagsList.getResult().getTrending() != null) {
                FoodTag[] trendingFoodTagArray = tagsList.getResult().getTrending();
                int count = trendingFoodTagArray.length;

                for (int i = 0; i < count; i++) {
                    if (i < trendingTextButtonArray.length) {
                        trendingTextButtonArray[i].setText(trendingFoodTagArray[i].getTag_name());
                        trendingTextButtonArray[i].setVisibility(View.VISIBLE);
                    }
                }

                // trending list make visisble
                mTrendingList.setVisibility(View.VISIBLE);
            } else {
                mTrendingList.setVisibility(View.GONE);
            }


            // Recommended
            TextView[] recommendedTextButtonArray = {mRecommendedText1, mRecommendedText2, mRecommendedText3, mRecommendedText4, mRecommendedText5, mRecommendedText6, mRecommendedText7, mRecommendedText8, mRecommendedText9};
            for (int i = 0; i < recommendedTextButtonArray.length; i++) {
                recommendedTextButtonArray[i].setVisibility(View.GONE);
            }

            if (tagsList.getResult().getRecomended() != null) {

                FoodTag[] recommendedFoodTagArray = tagsList.getResult().getRecomended();
                int tagCount = recommendedFoodTagArray.length;
                int btnCount = recommendedTextButtonArray.length;


                for (int i = 0; i < tagCount && i < btnCount; i++) {
                    String btnTitle = recommendedFoodTagArray[i].getTag_name();
                    recommendedTextButtonArray[i].setText(btnTitle);
                    recommendedTextButtonArray[i].setVisibility(View.VISIBLE);
                }

                // Recommended list make visible
                mRecommendedList.setVisibility(View.VISIBLE);
            } else {
                mRecommendedList.setVisibility(View.GONE);
            }

            // Favorites
            if (tagsList.getResult().getFavourites() == null) {
                getFavTagsListFromServer();
                //getCategories();
            } else {
                findViewById(R.id.favourites_list).setVisibility(View.VISIBLE);
            }

            getCategories();
        } catch (Exception e) {
            // Do nothing..
            //Googl
//            Log.i("Favourite tags :",e.getMessage());
        }


    }


    private void updateFavTagsUI(FavTagList favTagList) {
        Log.i("Inside update Fav tag", "");
        try {
            //show no data layout
            mNoDataLayout.setVisibility(View.GONE);
            saveNewUserId(Constants.USER_ID);


     /*   // Send App opened location
        t.send(new HitBuilders.EventBuilder()
                .setCategory("Location")
                .setAction("App Opened Location")
                .setLabel(mLocationName.getText().toString())
                .build());*/


            // Favourites
            TextView[] favTextButtonArray = {mFavouritesText1, mFavouritesText2, mFavouritesText3, mFavouritesText4, mFavouritesText5, mFavouritesText6, mFavouritesText7, mFavouritesText8, mFavouritesText9};
            for (int i = 0; i < favTextButtonArray.length; i++) {
                favTextButtonArray[i].setVisibility(View.GONE);
            }

//            Log.i("favTagList.getCategories()",favTagList.getCategories()+"");
            if (favTagList!=null&& favTagList.getFavourites() != null && favTagList.getFavourites().length>0) {

                Favourites[] favouriteFoodTagArray =favTagList.getFavourites();
                int count = favouriteFoodTagArray.length;
                Log.i("Fav tags size",count+"");
                for (int i = 0; i < count; i++) {
                    if (i < favTextButtonArray.length) {
                        Log.i("Setting fav tag",favouriteFoodTagArray[i].getTag_name());
                        favTextButtonArray[i].setText(favouriteFoodTagArray[i].getTag_name());
                        favTextButtonArray[i].setVisibility(View.VISIBLE);
                    }
                }
                if(favTagList.getFavourites() == null || favTagList.getFavourites().length==0)
                    mFavoritesList.setVisibility(View.GONE);
                else
                    mFavoritesList.setVisibility(View.VISIBLE);
                Log.i("favarates tagsForHB",favouriteFoodTagArray.length+"");
                // trending list make visisble
            } else {
                Log.i("Hiding fav tags","");
                mFavoritesList.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            // Do nothing..
            Log.i("exception for fav",e+"");
            mFavoritesList.setVisibility(View.GONE);
        }
    }

    private void setupLocalyticsUser() {
        // set Userid
        Localytics.setCustomerId(Constants.USER_ID);

        // Set email
        String CUSTOMER_EMAIL = Constants.DEVICE_EMAIL;
        Localytics.setCustomerEmail(CUSTOMER_EMAIL);
    }

    public void foodTagClicked(View v) {
        TextView foodTagTextBtn = (TextView) v;
        Constants.FOOD_TAG_VARIABLE = foodTagTextBtn.getText().toString();
        t.send(new HitBuilders.EventBuilder()
                .setCategory("FOODTAG")
                .setAction("Tag Clicked")
                .setLabel(foodTagTextBtn.getText().toString())
                .build());
        //Go to Deal List Activity
        Intent intent = new Intent(AppLaunchActivity.this, DealsListActivity.class);
        intent.putExtra(Constants.FOOD_TAG, foodTagTextBtn.getText().toString());
        Constants.SELECTED_CATEGORY="";
        startActivity(intent);
    }

    public void categoryClicked(View v) {
        TextView categoryButton = (TextView) v;
        String selectedCategory = categoryButton.getText().toString();
        t.send(new HitBuilders.EventBuilder()
                .setCategory("CATEGORY")
                .setAction("Category Clicked")
                .setLabel(categoryButton.getText().toString())
                .build());

        //Go to Deal List Activity
        Intent intent = new Intent(AppLaunchActivity.this, DealsListActivity.class);
        Constants.SELECTED_CATEGORY=categoryButton.getText().toString();
        startActivity(intent);
    }

    public void showAllFoods(View v) {
        // If App is active in current or selected locality only then proceed
        if (isAppActiveInCurrentCity) {
            if (isAppActiveInCurrentSubLocality) {
                t.send(new HitBuilders.EventBuilder()
                        .setCategory("Show All")
                        .setAction("Show All Clicked")
                        .build());
                //Go to Deal List Activity
                Intent intent = new Intent(AppLaunchActivity.this, DealsListActivity.class);
                intent.putExtra(Constants.FOOD_TAG, "");
                intent.putExtra("SEARCH_OPERATION", true);
                startActivity(intent);
            } else {
                showNoDealsInLocality();
            }
        } else {
            showOutOfCityMessage();
        }
    }

    public void retryConnectionToServer(View v) {
        // Call to retry the earlier server call
        getTagsListFromServer();
        getCategories();
        getFavTagsListFromServer();
        loadAutoSuggestions();
    }

    private void getTagsListFromServer() {
        mFavoritesList.setVisibility(View.GONE);
        if (isNetworkConnected()) {
            if ((mTagList == null || mTagList.getResult() == null) && !isFetchingDataFromServer) {
                isFetchingDataFromServer = true;
                showProgressDialog();
                FoodTagListReqest foodTagListReqest = new FoodTagListReqest();
                foodTagListReqest.setLongitude(Constants.LONGITUDE);
                foodTagListReqest.setLatitude(Constants.LATITUDE);
                foodTagListReqest.setUser_id(Constants.USER_ID);
                foodTagListReqest.setDevice_id(this.deviceId);
                foodTagListReqest.setPrimary_email(this.primaryEmailAddress);

                CategoryListRequest catListReqest = new CategoryListRequest();
                catListReqest.setLongitude(Constants.LONGITUDE);
                catListReqest.setLatitude(Constants.LATITUDE);
                catListReqest.setUserId(Constants.USER_ID);

                RequestObj req = new RequestObj();
                req.setBody(foodTagListReqest);

                Gson gson = new Gson();
                HBLog.i("Info", gson.toJson(req));

                System.out.println(gson.toJson(req));

                String jsonStr = gson.toJson(req);
                String jsonStr1 = gson.toJson(catListReqest);

                HBVolley.getRequestQueue(getApplicationContext()).add(
                        new HBGsonRequest(
                                Constants.TAGS_LIST_URL,
                                this,
                                this,
                                new TagsList(),
                                null,
                                null,
                                jsonStr,
                                Request.Method.POST
                        )).setShouldCache(true);

                HBVolley.getRequestQueue(getApplicationContext()).add(
                        new HBGsonRequest(
                                Constants.FAV_TAGS_LIST_URL,
                                this,
                                this,
                                new FavTagList(),
                                null,
                                null,
                                jsonStr,
                                Request.Method.POST
                        )).setShouldCache(true);

                HBVolley.getRequestQueue(getApplicationContext()).add(
                        new HBGsonRequest(
                                Constants.CATEGORY_LIST_URL,
                                this,
                                this,
                                new CategoryListResponse(),
                                null,
                                null,
                                jsonStr1,
                                Request.Method.POST
                        )).setShouldCache(true);
                // Create a time-out. This is used to check wheather the data has been returned from server or not and if not would dismiss the loading
                // screen and show the retry layout
                handler.postDelayed(runnable, Constants.INTERVAL);



            } else {
                // update UI with saved Data
                updateUI(mTagList);
            }
        } else {
            retryConnectionLayout.setVisibility(View.VISIBLE);
        }

    }


    private void getFavTagsListFromServer() {
        Log.i("Inside getFavTagList", "");
        if (isNetworkConnected()) {
            Log.i("Network connected","");
            Log.i("favtaglist",favTagList+"");
            if ((favTagList == null || favTagList.getFavourites() == null ||favTagList.getFavourites().length==0) && !isFetchingDataFromServer) {
                Log.i("inside if","");
                isFetchingDataFromServer = true;

                showProgressDialog();

                FoodTagListReqest foodTagListReqest = new FoodTagListReqest();
                foodTagListReqest.setLongitude(Constants.LONGITUDE);
                foodTagListReqest.setLatitude(Constants.LATITUDE);
                foodTagListReqest.setUser_id(Constants.USER_ID);
                foodTagListReqest.setDevice_id(this.deviceId);
                foodTagListReqest.setPrimary_email(this.primaryEmailAddress);


                RequestObj req = new RequestObj();
                req.setBody(foodTagListReqest);

                Gson gson = new Gson();
                HBLog.i("Info", gson.toJson(req));

                System.out.println(gson.toJson(req));

                String jsonStr = gson.toJson(req);

                HBVolley.getRequestQueue(getApplicationContext()).add(
                        new HBGsonRequest(
                                Constants.FAV_TAGS_LIST_URL,
                                this,
                                this,
                                new FavTagList(),
                                null,
                                null,
                                jsonStr,
                                Request.Method.POST
                        )).setShouldCache(true);


                Log.i("Fav request sent","");
                // Create a time-out. This is used to check wheather the data has been returned from server or not and if not would dismiss the loading
                // screen and show the retry layout
                handler.postDelayed(runnable, Constants.INTERVAL);
            } else {
                // update UI with saved Data
                updateFavTagsUI(favTagList);
            }

        } else {
            Log.i("No network","");
            retryConnectionLayout.setVisibility(View.VISIBLE);
        }


    }

    private void findCityName(double lattitude, double longitude) {
        if (Constants.LOCATION_NAME == null || Constants.LOCATION_NAME.isEmpty()) {
            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            try {
                String locationName = "";
                String cityName = "";
                List<Address> addresses = gcd.getFromLocation(lattitude, longitude, 10);

                Log.d("Ritesh", "addresst count " + addresses.size());
                for (Address adrs : addresses) {
                    if (adrs != null && adrs.getLocality() != null) {
                        String city = adrs.getLocality();
                        cityName = city;
                        String addressLine_1 = adrs.getAddressLine(1);
                        String[] addrs = addressLine_1.split(",");
                        String locality = addrs.length > 0 ? (addrs.length < 2 ? addrs[0] : addrs[addrs.length - 1]) : "";//adrs.getSubLocality();

                        if (locality != null && !locality.isEmpty()) {
                            locationName = locality;
                            mLocationName.setText(locationName);
                            break;
                        }

                        Log.d("Ritesh", "city " + city);
                        if (locationName.isEmpty()) {
                            if (city != null && !city.equals("")) {
                                locationName = city;
                                Log.d("Ritesh", "CityName " + locationName);
                                mLocationName.setText(locationName);
                                break;
                            }
                        }
                    }
                }

                // Check if the location is bangalore or not.
                // If NOT, then show a message alerting user that services is not available at their location
                // Toast.makeText(getApplicationContext(),"City"+cityName,Toast.LENGTH_LONG).show();
                if (!cityName.toLowerCase().equals("bengaluru") && !cityName.toLowerCase().equals("bangalore") && cityName.isEmpty()) {
                    isAppActiveInCurrentCity = false;
                    outsideLocationLayout.setVisibility(View.VISIBLE);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("Ritesh", "Exception in finding city  " + e.toString());
            }
        }
    }

    //region All NavDrawer

    private void setupNavDrawer() {
        mNavItems.add(new NavItem("Discover", "Discover your food", R.drawable.ic_discover));
        //mNavItems.add(new NavItem("Favorites", "Your favorite foods", R.drawable.));
        mNavItems.add(new NavItem("Refer & earn", "Refer your friends", R.drawable.ic_coins));
        mNavItems.add(new NavItem("Feedback", "Give us your feedback", R.drawable.ic_feedback));
        mNavItems.add(new NavItem("Write a Review", "Rate us in play store", R.drawable.ic_write_review));
        mNavItems.add(new NavItem("About", "Get to know about us", R.drawable.ic_about));


        // DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        // Populate the Navigtion Drawer with options
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);

        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
            }
        });


        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //Log.d(TAG, "onDrawerClosed: " + getTitle());

                invalidateOptionsMenu();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(mDrawerPane);
            }
        });

        mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_action_menu);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    /*
    * Called when a particular item from the navigation drawer
    * is selected.
    * */
    private void selectItemFromDrawer(int position) {

        mDrawerList.setItemChecked(position, true);
        //setTitle(mNavItems.get(position).mTitle);

        Intent intent;

        switch (position) {
            case 0:
                break;
            case 1:
                Intent i = new Intent(AppLaunchActivity.this, ReferralActivity.class);// ReferralActivity.class);
                startActivity(i);
                break;
            case 2:
                //Intent order = new Intent(AppLaunchActivity.this, MyOrdersActivity.class);// ReferralActivity.class);
                //startActivity(order);
                //break;
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "feedback@getwise.in", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                startActivity(Intent.createChooser(emailIntent, "Send Feedback"));
                break;
            case 3:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=com.HungryBells.activity"));
                startActivity(intent);
                break;
            case 4:
                intent = new Intent(AppLaunchActivity.this, AboutActivity.class);
                startActivity(intent);
                break;
            default:
                break;

        }
        // Close the drawer
        mDrawerLayout.closeDrawer(mDrawerPane);
    }

    // sidebar navitem
    class NavItem {
        String mTitle;
        String mSubtitle;
        int mIcon;

        public NavItem(String title, String subtitle, int icon) {
            mTitle = title;
            mSubtitle = subtitle;
            mIcon = icon;
        }
    }


    class DrawerListAdapter extends BaseAdapter {
        Context mContext;
        ArrayList<NavItem> mNavItems;

        public DrawerListAdapter(Context context, ArrayList<NavItem> navItems) {
            mContext = context;
            mNavItems = navItems;
        }

        @Override
        public int getCount() {
            return mNavItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mNavItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.drawer_item, null);
            } else {
                view = convertView;
            }

            TextView titleView = (TextView) view.findViewById(R.id.title);
            TextView subtitleView = (TextView) view.findViewById(R.id.subTitle);
            ImageView iconView = (ImageView) view.findViewById(R.id.icon);
            titleView.setText(mNavItems.get(position).mTitle);
            subtitleView.setText(mNavItems.get(position).mSubtitle);
            iconView.setImageResource(mNavItems.get(position).mIcon);
            return view;
        }
    }

    //endregion


    //region Guide Layout
    private void showGuideLayoutForFirstTime() {
        SharedPreferences sharedPref = AppLaunchActivity.this.getPreferences(Context.MODE_PRIVATE);
        String firstLaunch = sharedPref.getString(getString(R.string.first_launch), "true");

        // set user_id to constants if the user exixts
        if (firstLaunch == "true") {
            mGuideLayout.setVisibility(View.VISIBLE);
            //save first launch as false now
            SharedPreferences sharedPref1 = AppLaunchActivity.this.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref1.edit();
            editor.putString(getString(R.string.first_launch), "false");
            editor.commit();
        } else {
            mGuideLayout.setVisibility(View.GONE);
        }
    }

    public void guildeLayoutOkClicked(View v) {
        mGuideLayout.setVisibility(View.GONE);
    }

    //endregion

    private void showOutOfCityMessage() {
        new MaterialDialog.Builder(this)
                .title("Alert")
                .content("We are not there in your city yet. We are working on expanding to more cities. Do check back later to discover your favourite dishes.")
                .positiveText("Ok")
                .positiveColor(Color.BLUE)
                .show();
        //show no data layout
        if (mTagList == null || mTagList.getResult() == null || mTagList.getResult().getTrending().length == 0) mNoDataLayout.setVisibility(View.VISIBLE);
        mContextuallayout.setVisibility(View.GONE);

        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder()
                .setCategory("No Data")
                .setAction("Out of City")
                .setLabel(mLocationName.getText().toString())
                .build());

    }

    private void showNoDealsInLocality() {

        new MaterialDialog.Builder(this)
                .title("Alert")
                .content("There are no food content available in this sub locality. Would you like to explore your nearest locations?")
                .positiveText("Yes")
                .positiveColor(Color.BLUE)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        navigateToChangeLocationActivity();
                    }


                })
                .show();
       /*

        new MaterialDialog.Builder(this)
                .title("Alert")
                .content("There are no food content available in this sub locality. Would you like to explore your nearest locations?")
                .positiveText("Yes")
                .negativeText("Select Location Manually")
                .positiveColor(Color.BLUE)
                .negativeColor(Color.DKGRAY)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        navigateToChangeLocationActivity();
                    }

                    @Override

                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        dialog.dismiss();
                        isNoLocationDialogVisible = false;
                        navigateToChangeLocationActivity();
                    }

                })
               .show();

                */

        //show no data layout
        if (mTagList == null || mTagList.getResult() == null || mTagList.getResult().getTrending().length == 0) mNoDataLayout.setVisibility(View.VISIBLE);
        mContextuallayout.setVisibility(View.GONE);

        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder()
                .setCategory("No Data")
                .setAction("No Food in Sublocality")
                .setLabel(mLocationName.getText().toString())
                .build());
    }

    private void navigateToChangeLocationActivity() {
        // Intent intent = new Intent(AppLaunchActivity.this, FeedbackActivity.class);
        Intent intent = new Intent(AppLaunchActivity.this, ChangeLocationActivity.class);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        /*
        if(back_pressed + 2000 > System.currentTimeMillis()) super.onBackPressed();
        else Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();;
        back_pressed = System.currentTimeMillis();
        */

        if (this.lastBackPressTime < System.currentTimeMillis() - 4000) {
            toast = Toast.makeText(this, "Press back again to close this app", Toast.LENGTH_SHORT);
            toast.show();
            this.lastBackPressTime = System.currentTimeMillis();
        } else {
            if (toast != null) {
                toast.cancel();
            }
            finish();
            super.onBackPressed();

        }
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }


    public void callBtnClicked(View view) {
        String phone = "+918088002288";
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        startActivity(intent);
    }


    //region Local Nofication Creation - Daily @ 12:05 PM
    private void setLocalAppNotification() {
        // Alarm manager - magic thing that does what we need
        am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        // Intent for our  BroadcastReceiver
        Intent intent = new Intent(this, LocalNotificationReceiver.class);
        // PendingIntent for AlarmManager
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        // Just in case we have already set up AlarmManager,
        // we do cancel.
        am.cancel(pendingIntent);
        //Some simple code to define time of notification:
        Calendar cal = Calendar.getInstance();
        Date stamp = cal.getTime();
        stamp.setHours(12);
        stamp.setMinutes(5);
        stamp.setSeconds(0);
        // In case it's too late to notify user today
        if (stamp.getTime() < System.currentTimeMillis())
            stamp.setTime(stamp.getTime() + AlarmManager.INTERVAL_DAY);
        // Set one-time alarm
        am.setRepeating(AlarmManager.RTC_WAKEUP, stamp.getTime(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    //endregion

     /*
        stage 1- Like using our app?
        stage 2- How about rating us in the play store?
        stage 3 - Would you mind giving us some feedback?

       */

    private void setupNativeRating() {
        appRatingTitleTextView = (TextView) findViewById(R.id.app_rating_title);
        appRatingLeftButton = (Button) findViewById(R.id.left_btn);
        appRatingRightButton = (Button) findViewById(R.id.right_btn);
        appRatingStage = 1;
    }

    public void leftButtonClicked(View v) {
        switch (appRatingStage) {
            case 1:
                appRatingTitleTextView.setText("Would you mind giving us some feedback?");
                appRatingLeftButton.setText("No, thanks");
                appRatingRightButton.setText("Ok, sure");
                break;
            case 2:
                break;
            case 3:
                break;
            default:
                break;
        }
    }


    public void rightButtonClicked(View v) {
        switch (appRatingStage) {
            case 1:
                appRatingTitleTextView.setText("How about rating on the play store?");
                appRatingLeftButton.setText("No, thanks");
                appRatingRightButton.setText("Ok, sure");
                break;
            case 2:
                break;
            case 3:
                break;
            default:
                break;
        }
    }


    public void branchTestShareClicked(View v) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("$android_url", "http://49apps.net/apk/hb2.2.2.3.apk");
        } catch (JSONException e) {
        }


        branch.getShortUrl(obj, new Branch.BranchLinkCreateListener() {
            @Override
            public void onLinkCreate(String url, BranchError error) {
                //Log.i(TAG, "Ready to share my link = " + url);
                String shareString = "BRANCH TEST LINK -- " + url;
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareString);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share via"));
            }
        });


       /*
       BranchShortLinkBuilder shortUrlBuilder = new BranchShortLinkBuilder(AppLaunchActivity.this)
               .addTag("tag1")
               .addTag("tag2")
               .setChannel("channel1")
               .setFeature("feature1")
               .setStage("1")
               .addParameters("email", Constants.DEVICE_EMAIL)
               .addParameters("$android_url", "")
                // deeplink data - anything you want!


       try {
           params.put("$android_url", "http://myawesomesite.com/android-app-landing-page");
       } catch (JSONException ex) { }
       Branch.getInstance(getApplicationContext()).getShortUrl(params, new BranchLinkCreateListener() {
           @Override
           public void onLinkCreate(String url, BranchError error) {
               if (error == null) {
                   Log.i("Branch", "created a URL with a custom $android_url");
               }
           }
       })

       shortUrlBuilder.generateShortUrl(new Branch.BranchLinkCreateListener() {
           @Override
           public void onLinkCreate(String url, BranchError error) {
               if (error != null) {
                   Log.e("Branch Error", "Branch create short url failed. Caused by -" + error.getMessage());
               } else {
                   Log.i("Branch", "Got a Branch URL " + url);

                   String shareString = "BRANCH TEST LINK -- "+ url;

                   Intent sendIntent = new Intent();
                   sendIntent.setAction(Intent.ACTION_SEND);
                   sendIntent.putExtra(Intent.EXTRA_TEXT, shareString);
                   sendIntent.setType("text/plain");
                   startActivity(Intent.createChooser(sendIntent, "Share via"));
               }
           }
       });
       */

    }


    private void updateHungryBellsMoney(int money) {
        Constants.HB_MONEY = money;
        //save first launch as false now
        SharedPreferences sharedPref1 = AppLaunchActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref1.edit();
        editor.putString(getString(R.string.hb_money), money + "");
        editor.commit();
    }

    private void getHungryBellsMoney() {
        SharedPreferences sharedPref = AppLaunchActivity.this.getPreferences(Context.MODE_PRIVATE);
        String moneyStr = sharedPref.getString(getString(R.string.hb_money), "0");
        Constants.HB_MONEY = Integer.parseInt(moneyStr);
    }


    private void showNoLocationFound() {
        if (!isNoLocationDialogVisible) {
            new MaterialDialog.Builder(this)
                    .title("No Location Found")
                    .content("App would need GPS permission to detect the location automatically. Please provide GPS access to the app.")
                    .positiveText("Ok")
                    .negativeText("Cancel")
                    .positiveColor(Color.RED)
                    .negativeColor(Color.GRAY)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            GPSTracker gpsTracker = new GPSTracker(AppLaunchActivity.this);
                            gpsTracker.showSettingsAlert();
                            dialog.dismiss();
                            isNoLocationDialogVisible = false;
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            super.onNegative(dialog);
                        }
                    }).show();
            isNoLocationDialogVisible = true;
        }

        //show no data layout
        mNoDataLayout.setVisibility(View.GONE);
        mContextuallayout.setVisibility(View.GONE);

        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder()
                .setCategory("No Location")
                .setAction("No Location Found")
                .setLabel(mLocationName.getText().toString())
                .build());
    }


    private void checkForUserFeedback() {
        CheckFeedbackRequest request = new CheckFeedbackRequest();
        request.setUser_id(Constants.USER_ID);

        Gson gson = new Gson();
        String jsonStr = gson.toJson(request);

        HBVolley.getRequestQueue(getApplicationContext()).add(
                new HBGsonRequest(
                        Constants.CHECK_FEEDBACK_URL,
                        this,
                        this,
                        new CheckFeedbackResponse(),
                        null,
                        null,
                        jsonStr,
                        Request.Method.POST
                )).setShouldCache(false);
    }


    public void checkHungryBellsOperationTime() {
        Time now = new Time();
        now.setToNow();

        int hr = now.hour;
        int min = now.minute;

        if (hr < 9 || (hr >= 21 && min > 30)) {
            mClosedTextView.setVisibility(View.VISIBLE);
        } else {
            mClosedTextView.setVisibility(View.GONE);
        }
    }

    public void showProgressDialog() {
        // Show Pregress Dialog before any setup
        if (progressDialog == null) progressDialog = new ProgressDialog(this);
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.setMessage("Fetching foods in your locality..");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }
    }


    private boolean locationDataNotFound() {
        if (Constants.DEVICE_LAT.isEmpty() || Constants.DEVICE_LONG.isEmpty()) {
            return true;
        } else if ((Constants.DEVICE_LAT.equals("0.0") && Constants.DEVICE_LONG.equals("0.0"))) {
            return true;
        } else {
            return false;
        }
    }

    private void updateCategories(CategoryListResponse categoryList) {
        Log.i("","Inside update categories");
        categoriesLayout.setVisibility(View.GONE);
        try {
            //show no data layout
            // mNoDataLayout.setVisibility(View.GONE);
            mNoDataLayout.setVisibility(View.GONE);
            saveNewUserId(Constants.USER_ID);

            // Categories
            TextView[] categoryButtons = {categoryButton1,categoryButton2,categoryButton3,categoryButton4,categoryButton5,categoryButton6,categoryButton7,categoryButton8,categoryButton9};
            for (int i = 0; i < categoryButtons.length; i++) {
                categoryButtons[i].setVisibility(View.GONE);
            }

            if (categoryList!=null && categoryList.getCategoryList() != null && categoryList.getCategoryList().length>0) {
                CategoryList[] categories =categoryList.getCategoryList();
                int count = categories.length;
                Log.i("Categories count",count+"");
                for (int i = 0; i < count; i++) {
                        if(categories[i]!=null) {
                            Log.i("Setting category name", categories[i].getName());
                            categoryButtons[i].setText(categories[i].getName());
                            categoryButtons[i].setVisibility(View.VISIBLE);
                        }
                    if(i==8)
                        break;
                }

                Log.i("categories ",categories.length+"");
            } else {
                Log.i("Hiding categories ","");
                categoriesLayout.setVisibility(View.GONE);
            }

            if(categoryList == null || categoryList.getCategoryList().length==0)
                categoriesLayout.setVisibility(View.GONE);
            else
                categoriesLayout.setVisibility(View.VISIBLE);

        } catch (Exception e) {
            // Do nothing..
            Log.i("","exception in getCategories"+e);
            categoriesLayout.setVisibility(View.GONE);
        }
    }

}
