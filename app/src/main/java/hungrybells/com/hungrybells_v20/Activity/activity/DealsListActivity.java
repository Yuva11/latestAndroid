package hungrybells.com.hungrybells_v20.Activity.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.localytics.android.Localytics;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hungrybells.com.hungrybells_v20.Activity.adapters.DealsListViewAdapter;
import hungrybells.com.hungrybells_v20.Activity.app.HBLog;
import hungrybells.com.hungrybells_v20.Activity.app.HBVolley;
import hungrybells.com.hungrybells_v20.Activity.app.HungryBellsApplication;
import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;
import hungrybells.com.hungrybells_v20.Activity.entity.common.RequestObj;
import hungrybells.com.hungrybells_v20.Activity.entity.dealList.CategoryDealsRequest;
import hungrybells.com.hungrybells_v20.Activity.entity.dealList.Deals;
import hungrybells.com.hungrybells_v20.Activity.entity.dealList.DealsListRequest;
import hungrybells.com.hungrybells_v20.Activity.entity.dealList.DealsListResponse;
import hungrybells.com.hungrybells_v20.Activity.net.HBGsonRequest;
import hungrybells.com.hungrybells_v20.Activity.utils.Constants;
import hungrybells.com.hungrybells_v20.R;

/**
 * Created by ajeetkumar on 18/05/15.
 */
public class DealsListActivity extends ActionBarActivity implements
        Response.Listener<IDataModel>, Response.ErrorListener {

    Tracker t;

    private TextView mToolbarTitle;
    public static DealsListResponse mDealsListResponse;
    private ListView mDealsListView;
    private List<Deals> listItem;
    private static DealsListViewAdapter mDealsListAdapter;
    private static String tagSelected = "";
    private String apiString = "";
    private static boolean searchOperation = false;
    private static int selectedFilter;

    private ProgressDialog progressDialog;
    private RelativeLayout retryConnectionLayout;
    private RelativeLayout noDataLayout;

    @InjectView(R.id.guide_layout)
    RelativeLayout mGuideLayout;

    int pageNumber=1,pageSize=1000;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable(){
        public void run() {
            // Check if progress dialog is till showing and whether or not data has been recieved by the app or not
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();

                // API call hasnt gone as expected, enable the retry UI
                if (!searchOperation) {
                    retryConnectionLayout.setVisibility(View.VISIBLE);
                } else {
                    noDataLayout.setVisibility(View.VISIBLE);
                    noDataLayout.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    @InjectView(R.id.deals_list_view)
    ListView dealsListView;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    //region App Lifecycle Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deals_list);

        ButterKnife.inject(this);

        // Initialize Google Analytics
        try {
            t = ((HungryBellsApplication) getApplication()).getTracker(HungryBellsApplication.TrackerName.APP_TRACKER);
            t.setScreenName("Deal List View");
            t.send(new HitBuilders.AppViewBuilder().build());
        }catch(Exception  e)
        {
            //Toast.makeText(getApplicationContext(), "Error"+e.getMessage(), 1).show();
        }

        // Localytics
        Localytics.tagEvent("Deal List Viewed");

        //Guide Layout
        showGuideLayoutForFirstTime();

        retryConnectionLayout = (RelativeLayout)findViewById(R.id.retry_connection_layout);
        retryConnectionLayout.setVisibility(View.GONE);

        noDataLayout = (RelativeLayout) findViewById(R.id.no_data_layout);
        noDataLayout.setVisibility(View.GONE);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Constants.FOOD_TAG)) {
            tagSelected = intent.getStringExtra(Constants.FOOD_TAG);
            searchOperation = intent.getBooleanExtra("SEARCH_OPERATION", false);
            mDealsListResponse = null;
        }

        setupToolbar();

        mDealsListView = (ListView) this.findViewById(R.id.deals_list_view);
        listItem = new ArrayList<Deals>();
        mDealsListAdapter = new DealsListViewAdapter(this, R.layout.deals_list_item, listItem );
        mDealsListView.setAdapter(mDealsListAdapter);
        mDealsListView.setOnItemClickListener(dealsListItemClickListener);
        dealsListView.setAdapter(mDealsListAdapter);

        /*if(Constants.SELECTED_CATEGORY.isEmpty()){
            tagSelected = Constants.SELECTED_CATEGORY;
        }*/

        if(tagSelected == "") {
            tagSelected = Constants.FOOD_TAG_VARIABLE;
        }
        

    }

    private void showGuideLayoutForFirstTime(){
        SharedPreferences sharedPref = DealsListActivity.this.getPreferences(Context.MODE_PRIVATE);
        String firstLaunch = sharedPref.getString(getString(R.string.first_launch), "true");

        // set user_id to constants if the user exixts
        if (firstLaunch == "true") {
            mGuideLayout.setVisibility(View.VISIBLE);
            //save first launch as false now
            SharedPreferences sharedPref1 = DealsListActivity.this.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref1.edit();
            editor.putString(getString(R.string.first_launch), "false");
            editor.commit();
        } else {
            mGuideLayout.setVisibility(View.GONE);
        }
    }

    public void guildeLayoutOkClicked(View v){
        mGuideLayout.setVisibility(View.GONE);
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

    //endregion

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back);
        getSupportActionBar().setTitle("");
        mToolbarTitle = (TextView) findViewById(R.id.toolbar_title);

        if (tagSelected.equals("")) {
          mToolbarTitle.setText("All Foods");
        } else {
            mToolbarTitle.setText(tagSelected);
        }
        // Hide the Progress bar as the ui elements is setup correctly
        // Stop the progress dialog

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_deal_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        if (id == R.id.filter) {
            showSingleChoice();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Localytics.openSession();
        Localytics.tagScreen("Deal List");
        Localytics.upload();

        if(Constants.SELECTED_CATEGORY.isEmpty())
            getDealListForSelectedTag();
        else
            getDealsByCategory();
    }

    @Override
    public void onResponse(IDataModel dataModel) {
        if (dataModel instanceof DealsListResponse) {
            mDealsListResponse = (DealsListResponse) dataModel;
            if (mDealsListResponse.getResult() != null && mDealsListResponse.getResult().getDeals().size() > 0) {
                loadListViewWithData(mDealsListResponse);

                retryConnectionLayout.setVisibility(View.GONE);
                noDataLayout.setVisibility(View.GONE);
            } else {
                noDataLayout.setVisibility(View.VISIBLE);
            }
        } else {
            if (searchOperation) {
                noDataLayout.setVisibility(View.VISIBLE);
            } else {
                // API call hasn't gone as expected, enable the retry UI
                retryConnectionLayout.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }


    //region Event Item On click Listener
    AdapterView.OnItemClickListener dealsListItemClickListener = new AdapterView.OnItemClickListener() {



        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Toast.makeText(getBaseContext(), "WOOO, its working", Toast.LENGTH_LONG).show();
            //Save clicked deal in Constants and use it later
            Deals dealItem = listItem.get(position);//mDealsListResponse.getResult().getDeals().get(position);

            if (dealItem != null) {
                Intent intent = new Intent(DealsListActivity.this, DealDetailActivity.class);
                intent.putExtra(Constants.DEAL_ID_KEY, dealItem.getDealId());
                intent.putExtra("dealItem", dealItem);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Error in selection. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }

    };

    //endregion
    private void showSingleChoice() {
        new MaterialDialog.Builder(this)
                .title("Sort the list by:")
                .items(R.array.preference_values)
                .itemsCallbackSingleChoice(selectedFilter, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        selectedFilter = which;
                        sortDealListBasedOnSelectedFilter(which);
                        return true; // allow selection
                    }
                })
                .positiveText("Ok")
                .titleColorRes(R.color.material_indigo_500)
                .contentColor(R.color.material_indigo_500) // notice no 'res' postfix for literal color
                .dividerColorRes(R.color.material_indigo_500)
                .positiveColorRes(R.color.material_blue_grey_800)
                .neutralColorRes(R.color.material_blue_grey_800)
                .negativeColorRes(R.color.material_blue_grey_800)
                .widgetColorRes(R.color.material_blue_grey_800)
                .show();
    }

    public void retryConnectionToServer(View v) {

        // Call to retry the earlier server call
        if(Constants.SELECTED_CATEGORY.isEmpty())
            getDealListForSelectedTag();
        else
            getDealsByCategory();
    }

    private void loadListViewWithData(DealsListResponse dealsListResponse) {
        listItem.clear();
        for (Deals deals:dealsListResponse.getResult().getDeals()) {
            listItem.add(deals);
        }

        if (listItem != null) {
            sortDealListBasedOnSelectedFilter(selectedFilter);
        }
        HBLog.i("Info", dealsListResponse.toString());

        mDealsListAdapter.notifyDataSetChanged();

        //Load next page
        //pageNumber++;

      //  getDealListForSelectedTag();
        if (progressDialog!=null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void getDealListForSelectedTag() {
        if (isNetworkConnected()) {
            if (mDealsListResponse == null) {
                // Show Pregress Dialog before any setup
               showProgressDialog();
                // Set default filter to 3
                selectedFilter = 0;
                // Make call to get the Deals List
                DealsListRequest dealsListRequest = new DealsListRequest();
                dealsListRequest.setLongitude(Constants.LONGITUDE);
                dealsListRequest.setLatitude(Constants.LATITUDE);
                dealsListRequest.setUser_id(Constants.USER_ID);
                dealsListRequest.setSearchString(tagSelected);
                dealsListRequest.setPageNumber(pageNumber + "");
                dealsListRequest.setPageSize(pageSize + "");



                if(!Constants.SELECTED_CATEGORY.isEmpty()) {
                    dealsListRequest.setCategory_name(Constants.SELECTED_CATEGORY);

                }

                RequestObj req = new RequestObj();
                req.setBody(dealsListRequest);
                Gson gson = new Gson();
                HBLog.i("Info", gson.toJson(req));

                if (searchOperation) {
                    apiString = Constants.SEARCH_TAGS_URL;
                }else{
                    apiString = Constants.DEALS_LIST_URL;
                }

                HBVolley.getRequestQueue(getApplicationContext()).add(
                        new HBGsonRequest(
                                apiString,
                                this,
                                this,
                                new DealsListResponse(),
                                null,
                                null,
                                gson.toJson(req),
                                Request.Method.POST
                        )).setShouldCache(true);
                // Create a time-out. This is used to check wheather the data has been returned from server or not and if not would dismiss the loading
                // screen and show the retry layout
                handler.postDelayed(runnable, Constants.INTERVAL);
            } else {
                // Load Ui with the previous data
                loadListViewWithData(mDealsListResponse);
            }
        } else {
            retryConnectionLayout.setVisibility(View.VISIBLE);
        }
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    private void sortDealListBasedOnSelectedFilter(int filter) {
        switch (filter) {
            case 0:
                // Sort by nearest distance
                if (listItem != null) {
                    Collections.sort(listItem, new Comparator<Deals>() {
                        @Override
                        public int compare(Deals d1, Deals d2) {
                            return d1.getDistance().compareTo(d2.getDistance());
                        }
                    });
                    t.send(new HitBuilders.EventBuilder()
                            .setCategory("FILTER_OPTION")
                            .setAction("Sort by")
                            .setLabel("Distance")
                            .build());
                }
            case 1:
                //By price - descending
                if (listItem != null) {
                    Collections.sort(listItem, new Comparator<Deals>() {
                        @Override
                        public int compare(Deals d1, Deals d2) {
                            return Double.parseDouble(d1.getDealPrice()) > Double.parseDouble(d2.getDealPrice()) ? +1 : -1;
                        }
                    });
                    t.send(new HitBuilders.EventBuilder()
                            .setCategory("FILTER_OPTION")
                            .setAction("Sort by")
                            .setLabel("Price")
                            .build());
                }
                break;
            case 2:
                //By brand
                if (listItem != null) {
                   /* Collections.sort(listItem, new Comparator<Deals>() {
                        @Override
                        public int compare(Deals d1, Deals d2) {
                            return d2.getCan_buy().compareTo(d1.getCan_buy());
                        }
                    });*/

                    Collections.sort(listItem, new Comparator<Deals>() {
                        @Override
                        public int compare(Deals d1, Deals d2) {
                            return d1.getMerchantName().compareToIgnoreCase(d2.getMerchantName());
                        }
                    });t.send(new HitBuilders.EventBuilder()
                            .setCategory("FILTER_OPTION")
                            .setAction("Sort by")
                            .setLabel("Brand")
                            .build());
                }
                break;
            case 3:
                //By most ordered
                if (listItem != null) {
                    Collections.sort(listItem, new Comparator<Deals>() {
                        @Override
                        public int compare(Deals d1, Deals d2) {
                            return d2.getDealOrderedCount().compareTo(d1.getDealOrderedCount());
                        }
                    });
                    t.send(new HitBuilders.EventBuilder()
                            .setCategory("FILTER_OPTION")
                            .setAction("Sort by")
                            .setLabel("Most Ordered")
                            .build());
                }
                break;
            case 4:
                // Sort by most shared
                if (listItem != null) {
                    Collections.sort(listItem, new Comparator<Deals>() {
                        public int compare(Deals d1, Deals d2) {

                            return d2.getDealshare_count().compareTo(d1.getDealshare_count());
                        }
                    });

                    t.send(new HitBuilders.EventBuilder()
                            .setCategory("FILTER_OPTION")
                            .setAction("Sort by")
                            .setLabel("Most Shared")
                            .build());
                }
                break;
            case 5:
                // Sort by most liked
                if (listItem != null) {
                    Collections.sort(listItem, new Comparator<Deals>() {
                        public int compare(Deals d1, Deals d2) {

                            return d2.getDeallike_count().compareTo(d1.getDeallike_count());
                        }
                    });

                    t.send(new HitBuilders.EventBuilder()
                            .setCategory("FILTER_OPTION")
                            .setAction("Sort by")
                            .setLabel("Most Liked")
                            .build());
                }
                break;
            case 6:
                // Sort by most viewed
                if (listItem != null) {
                    Collections.sort(listItem, new Comparator<Deals>() {
                        public int compare(Deals d1, Deals d2) {

                            return d2.getDealview_count().compareTo(d1.getDealview_count());
                        }
                    });

                    t.send(new HitBuilders.EventBuilder()
                            .setCategory("FILTER_OPTION")
                            .setAction("Sort by")
                            .setLabel("Most Viewed")
                            .build());
                }
                break;
        }
        mDealsListAdapter.notifyDataSetChanged();
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

    public void getDealsByCategory() {
        if (isNetworkConnected()) {
            if (mDealsListResponse == null) {
                // Show Pregress Dialog before any setup
                showProgressDialog();
                // Set default filter to 3
                selectedFilter = 0;
                // Make call to get the Deals List
                CategoryDealsRequest categoryDealsRequest = new CategoryDealsRequest();
                categoryDealsRequest.setLongitude(Constants.LONGITUDE);
                categoryDealsRequest.setLatitude(Constants.LATITUDE);
                categoryDealsRequest.setUserId(Constants.USER_ID);
                categoryDealsRequest.setCategory_name(Constants.SELECTED_CATEGORY);

                /*RequestObj req = new RequestObj();
                req.setBody(categoryDealsRequest);*/
                Gson gson = new Gson();
                HBLog.i("Info", gson.toJson(categoryDealsRequest));

                String jsonStr = gson.toJson(categoryDealsRequest);
                apiString = Constants.DEALS_BY_CATEGORY_URL;

                HBVolley.getRequestQueue(getApplicationContext()).add(
                        new HBGsonRequest(
                                apiString,
                                this,
                                this,
                                new DealsListResponse(),
                                null,
                                null,
                                jsonStr,
                                Request.Method.POST
                        )).setShouldCache(true);
                mToolbarTitle.setText(Constants.SELECTED_CATEGORY);
                // Create a time-out. This is used to check wheather the data has been returned from server or not and if not would dismiss the loading
                // screen and show the retry layout
                handler.postDelayed(runnable, Constants.INTERVAL);
            } else {
                // Load Ui with the previous data
                loadListViewWithData(mDealsListResponse);
            }
        } else {
            retryConnectionLayout.setVisibility(View.VISIBLE);
        }
    }
}
