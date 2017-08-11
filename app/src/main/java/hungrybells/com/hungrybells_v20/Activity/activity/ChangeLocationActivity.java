package hungrybells.com.hungrybells_v20.Activity.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.localytics.android.Localytics;


import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hungrybells.com.hungrybells_v20.Activity.adapters.LocationsListViewAdapter;
import hungrybells.com.hungrybells_v20.Activity.app.HBLog;
import hungrybells.com.hungrybells_v20.Activity.app.HBVolley;
import hungrybells.com.hungrybells_v20.Activity.app.HungryBellsApplication;
import hungrybells.com.hungrybells_v20.Activity.entity.change_location.ChangeLocationRequest;
import hungrybells.com.hungrybells_v20.Activity.entity.change_location.ChangeLocationResponse;
import hungrybells.com.hungrybells_v20.Activity.entity.change_location.Location;
import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;
import hungrybells.com.hungrybells_v20.Activity.entity.common.RequestObj;
import hungrybells.com.hungrybells_v20.Activity.entity.old_api.location.GetNearestLocationResponse;
import hungrybells.com.hungrybells_v20.Activity.entity.old_api.location.NearestLocation;
import hungrybells.com.hungrybells_v20.Activity.net.HBGsonRequest;
import hungrybells.com.hungrybells_v20.Activity.utils.Constants;
import hungrybells.com.hungrybells_v20.R;

public class ChangeLocationActivity extends ActionBarActivity implements
        Response.Listener<IDataModel>, Response.ErrorListener {

    private List<Location> allNearestLocations;
    private LocationsListViewAdapter locationsListViewAdapter;

    private ProgressDialog progressDialog;
    private RelativeLayout retryConnectionLayout;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable(){
        public void run() {
            // Check if progress dialog is till showing and whether or not data has been recieved by the app or not
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();

                // API call hasnt gone as expected, enable the retry UI
                retryConnectionLayout.setVisibility(View.VISIBLE);
            }
        }
    };

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @InjectView(R.id.location_list_view)
    ListView locationsListView;

    @InjectView(R.id.search_location)
    EditText mSearchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_location);

        ButterKnife.inject(this);
        setupToolbar();



        // Initialize Google Analytics
        try {

            Tracker t = ((HungryBellsApplication) getApplication()).getTracker(HungryBellsApplication.TrackerName.APP_TRACKER);
            t.setScreenName("Change Location View");
            t.send(new HitBuilders.AppViewBuilder().build());
        }catch(Exception  e)
        {
            //Toast.makeText(getApplicationContext(), "Error"+e.getMessage(), 1).show();
        }

        // Localytics
        //Localytics.tagEvent("Change Location Viewed");

        retryConnectionLayout = (RelativeLayout)findViewById(R.id.retry_connection_layout);
        retryConnectionLayout.setVisibility(View.GONE);

        // initializing the locations list view
        ListView listView = (ListView)findViewById(R.id.location_list_view);
        allNearestLocations = new ArrayList<Location>();
        locationsListViewAdapter = new LocationsListViewAdapter(this, R.layout.location_list_item, allNearestLocations);
        listView.setAdapter(locationsListViewAdapter);
        listView.setOnItemClickListener(locationListItemClickListener);

        getLocationList();

        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String text = editable.toString().toLowerCase();
                locationsListViewAdapter.filter(text);

                locationsListViewAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back);
        getSupportActionBar().setTitle("");
    }



    @Override
    protected void onResume() {
        super.onResume();

        Localytics.openSession();
        Localytics.tagScreen("Change Location");
        Localytics.upload();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_change_location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResponse(IDataModel dataModel) {

        if(dataModel instanceof ChangeLocationResponse) {
            ChangeLocationResponse locations = (ChangeLocationResponse) dataModel;

            allNearestLocations.clear();

            for (Location item : locations.getChangeLocation()) {
                HBLog.d("locationName", item.getLocationName());
                allNearestLocations.add(item);
            }

            locationsListViewAdapter.addToArrayList(allNearestLocations);
            locationsListViewAdapter.notifyDataSetChanged();
            retryConnectionLayout.setVisibility(View.GONE);

        } else {

            // API call hasnt gone as expected, enable the retry UI
            retryConnectionLayout.setVisibility(View.VISIBLE);
        }


        // Hide the Progress bar as the ui elements is setup correctly
        // Stop the progress dialog
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }


    AdapterView.OnItemClickListener locationListItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {

            Constants.shouldUpldatePlaceLatLong = false;

            //Save clicked deal in Constants and use it later
            Location nearestLocation = allNearestLocations.get(position);

            Intent intent = new Intent(ChangeLocationActivity.this, AppLaunchActivity.class);
            intent.putExtra(Constants.selectedLocation, nearestLocation);
            startActivity(intent);

        }
    };

    public void retryConnectionToServer(View v) {

        // Call to retry the earlier server call
        getLocationList();
    }

    private void getLocationList() {

        if (isNetworkConnected()) {

            // Show Pregress Dialog before any setup
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading..");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.show();

            String url = Constants.GET_LOCATIONS_WITH_ACTIVE_DEALS;//Constants.GET_NEAREST_LOCATION_URL + Constants.LATITUDE + "/" + Constants.LONGITUDE + ".";

            ChangeLocationRequest changeLocationRequest = new ChangeLocationRequest();
            changeLocationRequest.setUser_id(Constants.USER_ID);
            changeLocationRequest.setLatitude(Constants.DEVICE_LAT);
            changeLocationRequest.setLongitude(Constants.DEVICE_LONG);

            RequestObj requestObj = new RequestObj();
            requestObj.setBody(changeLocationRequest);

            Gson gson = new Gson();

            HBVolley.getRequestQueue(getApplicationContext()).add(
                    new HBGsonRequest(
                            url,
                            this,
                            this,
                            new ChangeLocationResponse(),
                            null,
                            null,
                            gson.toJson(requestObj),
                            Request.Method.POST
                    )).setShouldCache(true);

            // Create a time-out. This is used to check wheather the data has been returned from server or not and if not would dismiss the loading
            // screen and show the retry layout
            handler.postDelayed(runnable, Constants.INTERVAL);

        } else {

            retryConnectionLayout.setVisibility(View.VISIBLE);

        }

    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }
}
