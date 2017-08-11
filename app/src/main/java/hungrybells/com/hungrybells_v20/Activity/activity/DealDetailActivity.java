package hungrybells.com.hungrybells_v20.Activity.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.localytics.android.Localytics;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hungrybells.com.hungrybells_v20.Activity.app.HBLog;
import hungrybells.com.hungrybells_v20.Activity.app.HBVolley;
import hungrybells.com.hungrybells_v20.Activity.app.HungryBellsApplication;
import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;
import hungrybells.com.hungrybells_v20.Activity.entity.common.RequestObj;
import hungrybells.com.hungrybells_v20.Activity.entity.dealList.Deals;
import hungrybells.com.hungrybells_v20.Activity.entity.dealList.DealsActionsLogRequest;
import hungrybells.com.hungrybells_v20.Activity.entity.nearest_distance.CheckDistanceRequest;
import hungrybells.com.hungrybells_v20.Activity.entity.nearest_distance.CheckDistanceResponse;
import hungrybells.com.hungrybells_v20.Activity.entity.order_processing.OrderItem;
import hungrybells.com.hungrybells_v20.Activity.entity.other_items_from_same_merchant.GetItemsFromSameMerchantResonse;
import hungrybells.com.hungrybells_v20.Activity.entity.other_items_from_same_merchant.GetItemsFromSingleMerchantRequest;
import hungrybells.com.hungrybells_v20.Activity.entity.other_items_from_same_merchant.ListOfItemsFromMerchant;
import hungrybells.com.hungrybells_v20.Activity.net.HBGsonRequest;
import hungrybells.com.hungrybells_v20.Activity.utils.Constants;
import hungrybells.com.hungrybells_v20.Activity.utils.GPSTracker;
import hungrybells.com.hungrybells_v20.R;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;

public class DealDetailActivity extends ActionBarActivity implements
        Response.Listener<IDataModel>, Response.ErrorListener {

    //region Instance Variables
    Boolean isDealLiked = false;
    private Deals deals;
    private double distanceInKm;
    Tracker t;

    ArrayList<OrderItem> orderItemList = new ArrayList<OrderItem>();
    Double totalOrderAmount = 0.0;

    private String customerAddress;

    GetItemsFromSameMerchantResonse response = null;
    ListOfItemsFromMerchant dealsList;
    private SharedPreferences sharedPref;
    Branch branch;
    ProgressDialog progressDialog;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable(){
        public void run() {
            // Check if progress dialog is till showing and whether or not data has been recieved by the app or not
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    };

    //endregion
    //region InjectView Elements
    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @InjectView(R.id.deal_name)
    TextView mDealName;

    @InjectView(R.id.deal_big_image)
    NetworkImageView mDealImage;

    @InjectView(R.id.store_name)
    TextView mStoreName;

    @InjectView(R.id.hero_deal_qty)
    TextView heroDealQuantity;

    //@InjectView(R.id.add_to_fav_btn)
    //Button mAddToFavBtn;

    @InjectView(R.id.deal_price)
    TextView mDealPrice;

    @InjectView(R.id.deal_details)
    TextView mDealDetails;

    @InjectView(R.id.detail_text)
    TextView mDetailText;

    @InjectView(R.id.merchant_logo)
    NetworkImageView mMerchantLogo;

    @InjectView(R.id.ordernow_btn)
    Button mCheckoutBtn;

    @InjectView(R.id.checkout_btn)
    LinearLayout mCheckoutLayout;

    @InjectView(R.id.final_price_checkout_text)
    TextView finalPriceTextView;

    @InjectView(R.id.item_0)
    LinearLayout itemLayout_0;

    @InjectView(R.id.dish_id_0)
    TextView dishId_0;

    @InjectView(R.id.dish_title_0)
    TextView dishTitle_0;

    @InjectView(R.id.dish_price_0)
    TextView dishPrice_0;

    @InjectView(R.id.deal_qty_0)
    TextView dishQuantity_0;

    @InjectView(R.id.item_1)
    LinearLayout itemLayout_1;

    @InjectView(R.id.dish_id_1)
    TextView dishId_1;

    @InjectView(R.id.dish_title_1)
    TextView dishTitle_1;

    @InjectView(R.id.dish_price_1)
    TextView dishPrice_1;

    @InjectView(R.id.deal_qty_1)
    TextView dishQuantity_1;

    @InjectView(R.id.item_2)
    LinearLayout itemLayout_2;

    @InjectView(R.id.dish_id_2)
    TextView dishId_2;

    @InjectView(R.id.dish_title_2)
    TextView dishTitle_2;

    @InjectView(R.id.dish_price_2)
    TextView dishPrice_2;

    @InjectView(R.id.deal_qty_2)
    TextView dishQuantity_2;

    @InjectView(R.id.item_3)
    LinearLayout itemLayout_3;

    @InjectView(R.id.dish_id_3)
    TextView dishId_3;

    @InjectView(R.id.dish_title_3)
    TextView dishTitle_3;

    @InjectView(R.id.dish_price_3)
    TextView dishPrice_3;

    @InjectView(R.id.deal_qty_3)
    TextView dishQuantity_3;

    @InjectView(R.id.item_4)
    LinearLayout itemLayout_4;

    @InjectView(R.id.dish_id_4)
    TextView dishId_4;

    @InjectView(R.id.dish_title_4)
    TextView dishTitle_4;

    @InjectView(R.id.dish_price_4)
    TextView dishPrice_4;

    @InjectView(R.id.deal_qty_4)
    TextView dishQuantity_4;

    @InjectView(R.id.item_5)
    LinearLayout itemLayout_5;

    @InjectView(R.id.dish_id_5)
    TextView dishId_5;

    @InjectView(R.id.dish_title_5)
    TextView dishTitle_5;

    @InjectView(R.id.dish_price_5)
    TextView dishPrice_5;

    @InjectView(R.id.deal_qty_5)
    TextView dishQuantity_5;

    @InjectView(R.id.item_6)
    LinearLayout itemLayout_6;

    @InjectView(R.id.dish_id_6)
    TextView dishId_6;

    @InjectView(R.id.dish_title_6)
    TextView dishTitle_6;

    @InjectView(R.id.dish_price_6)
    TextView dishPrice_6;

    @InjectView(R.id.deal_qty_6)
    TextView dishQuantity_6;

    @InjectView(R.id.item_7)
    LinearLayout itemLayout_7;

    @InjectView(R.id.dish_id_7)
    TextView dishId_7;

    @InjectView(R.id.dish_title_7)
    TextView dishTitle_7;

    @InjectView(R.id.dish_price_7)
    TextView dishPrice_7;

    @InjectView(R.id.deal_qty_7)
    TextView dishQuantity_7;

    @InjectView(R.id.item_8)
    LinearLayout itemLayout_8;

    @InjectView(R.id.dish_id_8)
    TextView dishId_8;

    @InjectView(R.id.dish_title_8)
    TextView dishTitle_8;

    @InjectView(R.id.dish_price_8)
    TextView dishPrice_8;

    @InjectView(R.id.deal_qty_8)
    TextView dishQuantity_8;

    @InjectView(R.id.choose_more)
    TextView chooseMoreText;

    @InjectView(R.id.closed_textview)
    TextView mClosedTextView;

    //endregion

    //region Activity Related Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_detail);

        ButterKnife.inject(this);
        setupToolbar();

        checkHungryBellsOperationTime();

        sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        branch = Branch.getInstance(getApplicationContext());

        // Initialize Google Analytics
        try {
            t = ((HungryBellsApplication) getApplication()).getTracker(HungryBellsApplication.TrackerName.APP_TRACKER);
            t.setScreenName("Deal Details View");
            t.send(new HitBuilders.AppViewBuilder().build());
        }catch(Exception  e)
        {
            //Toast.makeText(getApplicationContext(), "Error"+e.getMessage(), 1).show();
        }

        // Localytics
        Localytics.tagEvent("DealDetails Viewed");


        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Constants.DEAL_ID_KEY)) {

            // delete all the previously saved settings
            SharedPreferences.Editor editor = sharedPref.edit();
            try {
                editor.putString(deals.getDealId(), null);
                editor.commit();
            }
            catch (Exception e) {

            }
        }

        if (intent != null && intent.hasExtra("dealItem")) {
            deals = (Deals)intent.getSerializableExtra("dealItem");
            Constants.DEAL_ITEM = deals;
        } else {
            deals = Constants.DEAL_ITEM;
        }

        //deals = Constants.DEAL_ITEM;
        updateUI();

        DealsActionsLogRequest dealsActionsLogRequest = new DealsActionsLogRequest();
        dealsActionsLogRequest.setDealId(deals.getDealId());
        dealsActionsLogRequest.setUserId(Constants.USER_ID);

        RequestObj requestObj = new RequestObj();
        requestObj.setBody(dealsActionsLogRequest);
        Gson gson = new Gson();

        // Inform server that this deal is being viewed
        HBVolley.getRequestQueue(getApplicationContext()).add(
                new HBGsonRequest(
                        Constants.DEAL_VIEW_COUNT_LOG_URL,
                        this,
                        this,
                        null,
                        null,
                        null,
                        gson.toJson(requestObj),
                        Request.Method.POST
                )).setShouldCache(true);

        // Add the deal to the cart automatically
        stepUpCounterClicked(null);
    }


    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back);
        getSupportActionBar().setTitle("");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_deal_detail, menu);
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

        if (id == R.id.like)
        {
            if (!isDealLiked )
            {
                // Send like to server
                recordUserAction(Constants.SEND_LIKE_URL);
                // Like the deal and change the icon to full one
                item.setIcon(R.drawable.ic_action_likefull);
                isDealLiked = true;
            }
            else {
                // Unlike the deal and change the icon to empty one
                item.setIcon(R.drawable.ic_action_likeempty);
                isDealLiked = false;
            }

        }

        if (id == R.id.share)
        {
            // Send share action
            recordUserAction(Constants.DEAL_SHARE_LOG_URL);

            try {

                JSONObject obj = new JSONObject();
                obj.put("referringUserName", "John");
                obj.put("referringUserId", "1234");

                branch.getShortUrl(obj, new Branch.BranchLinkCreateListener() {
                    @Override
                    public void onLinkCreate(String url, BranchError branchError) {
                        Log.i(DealDetailActivity.class.getSimpleName(), "Ready to share my link = " + url);
                    }
                });
            }
            catch (Exception e) {
            }

            String shareString = "Hey!! I discovered " + mDealName.getText() + " in "+ Constants.LOCATION_NAME+ " through Hungry Bells app. It's so cool. Check it out now - http://bit.ly/1Jadx0u";

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareString);
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Share via"));

            t.send(new HitBuilders.EventBuilder()
                    .setCategory("SHARE")
                    .setLabel("Share Clicked")
                    .build());

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();

        Localytics.openSession();
        Localytics.tagScreen("Deal Detail");
        Localytics.upload();
    }

    //endregion

    // region APIs response handler

    @Override
    public void onResponse(IDataModel dataModel) {

        if (dataModel instanceof GetItemsFromSameMerchantResonse) {

            //region Deal from same merchant

            HBLog.d("time 2", new Date().toString());

            response = (GetItemsFromSameMerchantResonse) dataModel;
            dealsList = response.getResult();

            // check n removed the current displaying item from list
            Deals currentDeal = null;
            for (Deals dealItem:dealsList.getDeals()) {
                if (dealItem.getDealId().equals(this.deals.getDealId())) currentDeal = dealItem;
            }
            if (currentDeal != null) dealsList.getDeals().remove(currentDeal);


            int i = 0;
            for (Deals deal : dealsList.getDeals()) {

                Double price = Double.parseDouble(deal.getDealPrice());

                switch (i++) {
                    case 0:

                        itemLayout_0.setVisibility(View.VISIBLE);
                        dishTitle_0.setText(deal.getName());
                        dishPrice_0.setText("Rs. " + price.intValue());
                        //dishQuantity_0.setText("0");
                        dishId_0.setText(deal.getDealId());

                        break;
                    case 1:

                        itemLayout_1.setVisibility(View.VISIBLE);
                        dishTitle_1.setText(deal.getName());
                        dishPrice_1.setText("Rs " + price.intValue());
                        //dishQuantity_1.setText("0");
                        dishId_1.setText(deal.getDealId());

                        break;
                    case 2:
                        itemLayout_2.setVisibility(View.VISIBLE);
                        dishTitle_2.setText(deal.getName());
                        dishPrice_2.setText("Rs " + price.intValue());
                        //dishQuantity_2.setText("0");
                        dishId_2.setText(deal.getDealId());

                        break;
                    case 3:
                        itemLayout_3.setVisibility(View.VISIBLE);
                        dishTitle_3.setText(deal.getName());
                        dishPrice_3.setText("Rs " + price.intValue());
                        //dishQuantity_3.setText("0");
                        dishId_3.setText(deal.getDealId());

                        break;
                    case 4:
                        itemLayout_4.setVisibility(View.VISIBLE);
                        dishTitle_4.setText(deal.getName());
                        dishPrice_4.setText("Rs " + price.intValue());
                        //dishQuantity_4.setText("0");
                        dishId_4.setText(deal.getDealId());

                        break;
                    case 5:
                        itemLayout_5.setVisibility(View.VISIBLE);
                        dishTitle_5.setText(deal.getName());
                        dishPrice_5.setText("Rs " + price.intValue());
                        //dishQuantity_5.setText("0");
                        dishId_5.setText(deal.getDealId());

                        break;
                    case 6:
                        itemLayout_6.setVisibility(View.VISIBLE);
                        dishTitle_6.setText(deal.getName());
                        dishPrice_6.setText("Rs " + price.intValue());
                        //dishQuantity_6.setText("0");
                        dishId_6.setText(deal.getDealId());

                        break;
                    case 7:
                        itemLayout_7.setVisibility(View.VISIBLE);
                        dishTitle_7.setText(deal.getName());
                        dishPrice_7.setText("Rs " + price.intValue());
                        //dishQuantity_7.setText("0");
                        dishId_7.setText(deal.getDealId());

                        break;
                    case 8:
                        itemLayout_8.setVisibility(View.VISIBLE);
                        dishTitle_8.setText(deal.getName());
                        dishPrice_8.setText("Rs " + price.intValue());
                        //dishQuantity_8.setText("0");
                        dishId_8.setText(deal.getDealId());

                        break;
                }
            }

            // Check and restore the previous user selection
            restoreUserSelectionOfDishes();
            //endregion
        } else if (dataModel instanceof CheckDistanceResponse) {
            CheckDistanceResponse response = (CheckDistanceResponse) dataModel;
            if (response.getStatus() != null && response.getStatus().equals("success") && response.getRows().length > 0) {
                Double distance = Double.parseDouble(response.getRows()[0].getElements()[0].getDistance().getValue());


                Double distance_in_km = distance/1000;
                if (distance_in_km > 5) {
                    showErrorMessage("The kitchen seems too far away. Try to order from within your locality for best experience");
                } else {
                    if (totalOrderAmount > 0) {
                        customerAddress=response.getDestinationAddresses()[0];

                        Intent intent = new Intent(this, OrderDetailsActivity.class);
                        intent.putExtra(Constants.DEAL_ID_KEY, this.deals.getDealId());
                        intent.putExtra(Constants.ORDER_LIST, orderItemList);
                        intent.putExtra(Constants.TOTAL_PRICE, totalOrderAmount);

                        intent.putExtra(Constants.DELIVERY_CHARGE,response.getDeliveryCharge());
                        intent.putExtra(Constants.MINIMUM_ORDER_VALUE_FOR_FREE_DELIVERY,response.getMinimumOrderValueForFreeDelivery());
                        intent.putExtra(Constants.REPEAT_DISCOUNT_TYPE,response.getType());
                        intent.putExtra(Constants.REPEAT_DISCOUNT_VALUE,response.getValue());
                        intent.putExtra(Constants.REPEAT_MIN_ORDER_VALUE,response.getMinimumOrderValue());
                        intent.putExtra(Constants.REPEAT_DISCOUNT_MAX_VALUE,response.getMaximumDiscountValue());
                        intent.putExtra(Constants.REPEAT_DISCOUNT_MSG,response.getMessage());

                        //Customer details
                        intent.putExtra(Constants.CUSTOMER_NAME,response.getUserName());
                        intent.putExtra(Constants.CUSTOMER_MOBILE,response.getUserMob());
                        intent.putExtra(Constants.CUSTOMER_EMAIL,response.getUserEmail());
                        intent.putExtra(Constants.CUSTOMER_ADDRESS_LANDMARK,response.getCustomerAddressLandMark());
                        intent.putExtra(Constants.CUSTOMER_ADDRESS,customerAddress);



                        intent.putExtra("dealItem", deals);
                        startActivity(intent);

                    } else {
                        showErrorMessage("No food item selected. Please select at least one food item.");
                    }
                }
            } else {if (response.getStatus() == null) {
                    GPSTracker gps = new GPSTracker(DealDetailActivity.this);
                    gps.showSettingsAlert();
                } else  {
                    showErrorMessage("Restaurant seems to be too far. Please select a nearer restaurant");
                }
            }
        }
        hideProgressDialog();
    }



    @Override
    public void onErrorResponse(VolleyError error) {
        showErrorMessage("Sorry, couldn't connect to server. Please try again");
        hideProgressDialog();
    }
    //endregion
    //region Helper Methods

    private void makeImageRequest(NetworkImageView img, String url) {

        ImageLoader imageLoader = HBVolley.getImageLoader();
        // If you are using NetworkImageView
        if (url != null) {
            img.setImageUrl(url, imageLoader);
        }
    }

    public void addDealToFavourites(View v) {

        // Send server to favruoite the deal
        recordUserAction(Constants.ADD_FAV_URL);

        Toast.makeText(this, "Added to Favourites", Toast.LENGTH_SHORT).show();

    }

    private void recordUserAction(String url) {

        DealsActionsLogRequest dealsActionsLogRequest = new DealsActionsLogRequest();
        dealsActionsLogRequest.setUserId(Constants.USER_ID);
        dealsActionsLogRequest.setDealId(deals.getDealId());

        RequestObj requestObj = new RequestObj();
        requestObj.setBody(dealsActionsLogRequest);

        Gson gson = new Gson();

        HBVolley.getRequestQueue(getApplicationContext()).add(
                new HBGsonRequest(
                        url,
                        this,
                        this,
                        null,
                        null,
                        null,
                        gson.toJson(requestObj),
                        Request.Method.POST
                )
        );

    }

    public void directionClicked(View v){


        String geoloactionStr = "http://maps.google.com/maps?daddr="+ deals.getMerchantLatitude() +","+deals.getMerchantLongitude();

        // "geo:" + deals.getMerchantLatitude() +","+deals.getMerchantLongitude() ;

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoloactionStr));
        startActivity(intent);
    }


    public static double distanceLatLong2(double lat1, double lng1, double lat2, double lng2)
    {
        double earthRadius = 6371.0d; // KM: use mile here if you want mile result

        double dLat = toRadian(lat2 - lat1);
        double dLng = toRadian(lng2 - lng1);

        double a = Math.pow(Math.sin(dLat/2), 2)  +
                Math.cos(toRadian(lat1)) * Math.cos(toRadian(lat2)) *
                        Math.pow(Math.sin(dLng/2), 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return earthRadius * c; // returns result kilometers
    }

    public static double toRadian(double degrees)
    {
        return (degrees * Math.PI) / 180.0d;
    }



    private void showErrorMessage(String msg) {
        new MaterialDialog.Builder(this)
                .title("Alert")
                .content(msg.isEmpty() ? "Opps! Unable to connect to server right now. Please try again." : msg)
                .positiveText("Ok")
                .positiveColor(Color.BLUE)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                    }
                })
                .show();
    }

    private void fetchAllDealsFromThisMerchant() {

        HBLog.d("time", new Date().toString());

        GetItemsFromSingleMerchantRequest getItemsFromSingleMerchantRequest = new GetItemsFromSingleMerchantRequest();
        getItemsFromSingleMerchantRequest.setUser_id(Constants.USER_ID);
        getItemsFromSingleMerchantRequest.setLatitude(Constants.DEVICE_LAT);
        getItemsFromSingleMerchantRequest.setLongitude(Constants.DEVICE_LONG);
        getItemsFromSingleMerchantRequest.setMerchantbranch_id(Integer.parseInt(deals.getMerchantBranchid()));

        RequestObj requestObj = new RequestObj();
        requestObj.setBody(getItemsFromSingleMerchantRequest);

        Gson gson = new Gson();

        // Get all deals form the same merchant
        HBVolley.getRequestQueue(getApplicationContext()).add(
                new HBGsonRequest(
                        Constants.GET_ALL_ORDER_FROM_SINGLE_MERCHANT_URL,
                        this,
                        this,
                        new GetItemsFromSameMerchantResonse(),
                        null,
                        null,
                        gson.toJson(requestObj),
                        Request.Method.POST
                )).setShouldCache(true);

    }

    //endregion

    //region btn click handler

    public void buyBtnClicked(View v)
    {
        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder()
                .setCategory("ORDER")
                .setAction("Order Now Clicked")
                .build());

        // Show progress dialog before hitting the server to calculate the driving distance using Google API
        showProgressDialog();

        CheckDistanceRequest request = new CheckDistanceRequest();
        request.setMerchantbranch_id(this.deals.getMerchantBranchid());
        request.setLatitude(Constants.DEVICE_LAT);
        request.setLongitude(Constants.DEVICE_LONG);
        request.setUserId(Constants.USER_ID);

        Gson gson = new Gson();
        String jsonStr = gson.toJson(request);

        HBLog.d("DealDetailsActivity", Constants.GET_DRIVING_DISTANCE_TO_RESTAURANT);
        HBVolley.getRequestQueue(getApplicationContext()).add(
                new HBGsonRequest(
                        Constants.GET_DRIVING_DISTANCE_TO_RESTAURANT,
                        this,
                        this,
                        new CheckDistanceResponse(),
                        null,
                        null,
                        jsonStr,
                        Request.Method.POST
                )).setShouldCache(true);
    }

    //region Increment / Decrement Button Handlers

    public void stepDownCounterClicked(View view) {

        Integer value = Integer.parseInt(heroDealQuantity.getText().toString());
        if (value > 0) {
            value--;
            heroDealQuantity.setText(value.toString());
            if (deals != null) addOrUpdateOrderList(Integer.parseInt(deals.getDealId()), deals.getDealPrice(), value, heroDealQuantity,
                    deals.getName(), deals.getMerchantName());
        } else {
            heroDealQuantity.setVisibility(View.GONE);
        }
    }

    public void stepUpCounterClicked(View view) {

        Integer value = Integer.parseInt(heroDealQuantity.getText().toString());
        value++;
        heroDealQuantity.setText(value.toString());
        if (deals != null) addOrUpdateOrderList(Integer.parseInt(deals.getDealId()), deals.getDealPrice(), value, heroDealQuantity,
                deals.getName(), deals.getMerchantName());

    }

    public void stepDownCounterClicked_0(View view) {

        Deals item = dealsList.getDeals().get(0);

        Integer value = Integer.parseInt(dishQuantity_0.getText().toString());
        if (value > 0) {
            value--;
            dishQuantity_0.setText(value.toString());
            if (dealsList != null) addOrUpdateOrderList(Integer.parseInt(dealsList.getDeals().get(0).getDealId()), dealsList.getDeals().get(0).getDealPrice(), value, dishQuantity_0,
                    item.getName(), item.getMerchantName());
        } else {
            dishQuantity_0.setVisibility(View.INVISIBLE);
        }
    }

    public void stepUpCounterClicked_0(View view) {

        Deals item = dealsList.getDeals().get(0);

        Integer value = Integer.parseInt(dishQuantity_0.getText().toString());
        value++;
        dishQuantity_0.setText(value.toString());
        if (dealsList != null) addOrUpdateOrderList(Integer.parseInt(dealsList.getDeals().get(0).getDealId()), dealsList.getDeals().get(0).getDealPrice(), value, dishQuantity_0,
                item.getName(), item.getMerchantName());

    }

    public void stepDownCounterClicked_1(View view) {

        Deals item = dealsList.getDeals().get(1);

        Integer value = Integer.parseInt(dishQuantity_1.getText().toString());
        if (value > 0) {
            value--;
            dishQuantity_1.setText(value.toString());
            if (dealsList != null) addOrUpdateOrderList(Integer.parseInt(dealsList.getDeals().get(1).getDealId()), dealsList.getDeals().get(1).getDealPrice(), value, dishQuantity_1,
                    item.getName(), item.getMerchantName());
        } else {
            dishQuantity_1.setVisibility(View.INVISIBLE);
        }
    }

    public void stepUpCounterClicked_1(View view) {

        Deals item = dealsList.getDeals().get(1);

        Integer value = Integer.parseInt(dishQuantity_1.getText().toString());
        value++;
        dishQuantity_1.setText(value.toString());
        if (dealsList != null) addOrUpdateOrderList(Integer.parseInt(dealsList.getDeals().get(1).getDealId()), dealsList.getDeals().get(1).getDealPrice(), value, dishQuantity_1,
                item.getName(), item.getMerchantName());

    }

    public void stepDownCounterClicked_2(View view) {

        Deals item = dealsList.getDeals().get(2);

        Integer value = Integer.parseInt(dishQuantity_2.getText().toString());
        if (value > 0) {
            value--;
            dishQuantity_2.setText(value.toString());
            if (dealsList != null) addOrUpdateOrderList(Integer.parseInt(dealsList.getDeals().get(2).getDealId()), dealsList.getDeals().get(2).getDealPrice(), value, dishQuantity_2,
                    item.getName(), item.getMerchantName());
        } else {
            dishQuantity_2.setVisibility(View.INVISIBLE);
        }
    }

    public void stepUpCounterClicked_2(View view) {

        Deals item = dealsList.getDeals().get(2);

        Integer value = Integer.parseInt(dishQuantity_2.getText().toString());
        value++;
        dishQuantity_2.setText(value.toString());
        if (dealsList != null) addOrUpdateOrderList(Integer.parseInt(dealsList.getDeals().get(2).getDealId()), dealsList.getDeals().get(2).getDealPrice(), value, dishQuantity_2,
                item.getName(), item.getMerchantName());

    }

    public void stepDownCounterClicked_3(View view) {

        Deals item = dealsList.getDeals().get(3);

        Integer value = Integer.parseInt(dishQuantity_3.getText().toString());
        if (value > 0) {
            value--;
            dishQuantity_3.setText(value.toString());
            if (dealsList != null) addOrUpdateOrderList(Integer.parseInt(dealsList.getDeals().get(3).getDealId()), dealsList.getDeals().get(3).getDealPrice(), value, dishQuantity_3,
                    item.getName(), item.getMerchantName());
        } else {
            dishQuantity_3.setVisibility(View.INVISIBLE);
        }
    }

    public void stepUpCounterClicked_3(View view) {

        Deals item = dealsList.getDeals().get(3);

        Integer value = Integer.parseInt(dishQuantity_3.getText().toString());
        value++;
        dishQuantity_3.setText(value.toString());
        if (dealsList != null) addOrUpdateOrderList(Integer.parseInt(dealsList.getDeals().get(3).getDealId()), dealsList.getDeals().get(3).getDealPrice(), value, dishQuantity_3,
                item.getName(), item.getMerchantName());

    }

    public void stepDownCounterClicked_4(View view) {

        Deals item = dealsList.getDeals().get(4);

        Integer value = Integer.parseInt(dishQuantity_4.getText().toString());
        if (value > 0) {
            value--;
            dishQuantity_4.setText(value.toString());
            if (dealsList != null) addOrUpdateOrderList(Integer.parseInt(dealsList.getDeals().get(4).getDealId()), dealsList.getDeals().get(4).getDealPrice(), value, dishQuantity_4,
                    item.getName(), item.getMerchantName());
        } else {
            dishQuantity_4.setVisibility(View.INVISIBLE);
        }
    }

    public void stepUpCounterClicked_4(View view) {

        Deals item = dealsList.getDeals().get(4);

        Integer value = Integer.parseInt(dishQuantity_4.getText().toString());
        value++;
        dishQuantity_4.setText(value.toString());
        if (dealsList != null) addOrUpdateOrderList(Integer.parseInt(dealsList.getDeals().get(4).getDealId()), dealsList.getDeals().get(4).getDealPrice(), value, dishQuantity_4,
                item.getName(), item.getMerchantName());

    }

    public void stepDownCounterClicked_5(View view) {

        Deals item = dealsList.getDeals().get(5);

        Integer value = Integer.parseInt(dishQuantity_5.getText().toString());
        if (value > 0) {
            value--;
            dishQuantity_5.setText(value.toString());
            if (dealsList != null) addOrUpdateOrderList(Integer.parseInt(dealsList.getDeals().get(5).getDealId()), dealsList.getDeals().get(5).getDealPrice(), value, dishQuantity_5,
                    item.getName(), item.getMerchantName());
        } else {
            dishQuantity_5.setVisibility(View.INVISIBLE);
        }
    }

    public void stepUpCounterClicked_5(View view) {

        Deals item = dealsList.getDeals().get(5);

        Integer value = Integer.parseInt(dishQuantity_5.getText().toString());
        value++;
        dishQuantity_5.setText(value.toString());
        if (dealsList != null) addOrUpdateOrderList(Integer.parseInt(dealsList.getDeals().get(5).getDealId()), dealsList.getDeals().get(5).getDealPrice(), value, dishQuantity_5,
                item.getName(), item.getMerchantName());

    }

    public void stepDownCounterClicked_6(View view) {

        Deals item = dealsList.getDeals().get(6);

        Integer value = Integer.parseInt(dishQuantity_6.getText().toString());
        if (value > 0) {
            value--;
            dishQuantity_6.setText(value.toString());
            if (dealsList != null) addOrUpdateOrderList(Integer.parseInt(dealsList.getDeals().get(6).getDealId()), dealsList.getDeals().get(6).getDealPrice(), value, dishQuantity_6,
                    item.getName(), item.getMerchantName());
        } else {
            dishQuantity_6.setVisibility(View.INVISIBLE);
        }
    }

    public void stepUpCounterClicked_6(View view) {

        Deals item = dealsList.getDeals().get(6);

        Integer value = Integer.parseInt(dishQuantity_6.getText().toString());
        value++;
        dishQuantity_6.setText(value.toString());
        if (dealsList != null) addOrUpdateOrderList(Integer.parseInt(dealsList.getDeals().get(6).getDealId()), dealsList.getDeals().get(6).getDealPrice(), value, dishQuantity_6,
                item.getName(), item.getMerchantName());

    }

    public void stepDownCounterClicked_7(View view) {

        Deals item = dealsList.getDeals().get(7);

        Integer value = Integer.parseInt(dishQuantity_7.getText().toString());
        if (value > 0) {
            value--;
            dishQuantity_7.setText(value.toString());
            if (dealsList != null) addOrUpdateOrderList(Integer.parseInt(dealsList.getDeals().get(7).getDealId()), dealsList.getDeals().get(7).getDealPrice(), value, dishQuantity_7,
                    item.getName(), item.getMerchantName());
        } else {
            dishQuantity_7.setVisibility(View.INVISIBLE);
        }
    }

    public void stepUpCounterClicked_7(View view) {

        Deals item = dealsList.getDeals().get(7);

        Integer value = Integer.parseInt(dishQuantity_7.getText().toString());
        value++;
        dishQuantity_7.setText(value.toString());
        if (dealsList != null) addOrUpdateOrderList(Integer.parseInt(dealsList.getDeals().get(7).getDealId()), dealsList.getDeals().get(7).getDealPrice(), value, dishQuantity_7,
                item.getName(), item.getMerchantName());

    }

    public void stepDownCounterClicked_8(View view) {

        Deals item = dealsList.getDeals().get(8);

        Integer value = Integer.parseInt(dishQuantity_8.getText().toString());
        if (value > 0) {
            value--;
            dishQuantity_8.setText(value.toString());
            if (dealsList != null) addOrUpdateOrderList(Integer.parseInt(item.getDealId()), item.getDealPrice(), value, dishQuantity_8,
                    item.getName(), item.getMerchantName());
        } else {
            dishQuantity_8.setVisibility(View.INVISIBLE);
        }
    }

    public void stepUpCounterClicked_8(View view) {

        Deals item = dealsList.getDeals().get(8);

        Integer value = Integer.parseInt(dishQuantity_8.getText().toString());
        value++;
        dishQuantity_8.setText(value.toString());
        if (dealsList != null) addOrUpdateOrderList(Integer.parseInt(item.getDealId()), item.getDealPrice(), value, dishQuantity_8,
                item.getName(), item.getMerchantName());
    }

    //endregion

    //endregion

    //region Helper Methods

    private void updateUI()
    {
        //Deals deals = Constants.DEAL_ITEM;

        mDealName.setText(deals.getName());
        mStoreName.setText(deals.getMerchantName());

        mDealPrice.setText("Rs" + (int) Float.parseFloat(deals.getDealPrice()));

        //region previous UI code
        // Deal price original
        //mDealPriceOriginal.setText("Rs" + (int) Float.parseFloat(deals.getOriginal_price()));
        //mDealPriceOriginal.setPaintFlags(mDealPriceOriginal.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        //set Discount
        /*
        int discountPercentage = calculateDealDiscount(deals.getDealPrice(), deals.getOriginal_price());
        if (discountPercentage == 0)
        {
            mDealPriceOriginal.setVisibility(View.GONE);
            mDealDiscount.setVisibility(View.GONE);
            mDiscountLayout.setVisibility(View.GONE);
        }
        else {
            mDealDiscount.setText(discountPercentage + "%");
            mDealPriceOriginal.setVisibility(View.VISIBLE);
            mDealDiscount.setVisibility(View.VISIBLE);
            mDiscountLayout.setVisibility(View.VISIBLE);
        }
        */


        // Dilivery Options

        /*
        String[] divileryOptions = deals.getDeliveryTypes();

        for ( String option: divileryOptions)
        {
            switch (option){
                case "HOMEDELIVERY" : mDeliveryImage.setImageResource(R.drawable.ic_action_tick);
                    break;
                case "DINEIN" : mDininImage.setImageResource(R.drawable.ic_action_tick);
                    break;
                case "PICKUP" : mTakeawayImage.setImageResource(R.drawable.ic_action_tick);
                    break;
                default:
                    break;
            }
        }
        */
        //endregion

        //set detail text
        if(deals.getDetailText().isEmpty()) {
            mDetailText.setVisibility(View.GONE);

        }
        else {
            mDetailText.setVisibility(View.VISIBLE);
            mDetailText.setText(deals.getDetailText());
        }

        //set deal details
        if (deals.getDealDetails().isEmpty())
        {
            mDealDetails.setVisibility(View.GONE);
        }else {

            mDealDetails.setVisibility(View.VISIBLE);
            mDealDetails.setText(deals.getDealDetails());
        }


        //mAvailabilityText.setText(deals.getAvailability());

        //load merchant logo
        makeImageRequest(mMerchantLogo, deals.getMerchantLogo());

        // load deal image
        makeImageRequest(mDealImage, deals.getImageUrl());


        // set distance to restarunt
        if (Constants.DEVICE_LAT.equals("") || Constants.DEVICE_LONG.equals("")) {
        } else {

            double lat1 = Double.parseDouble(Constants.DEVICE_LAT);
            double lng1 = Double.parseDouble(Constants.DEVICE_LONG);
            double lat2 = Double.parseDouble(deals.getMerchantLatitude());
            double lng2 = Double.parseDouble(deals.getMerchantLongitude());
            distanceInKm = distanceLatLong2(lat1, lng1, lat2, lng2);

            String distanceString = "";

            if (distanceInKm < 1.0)
            {
                distanceString = "approx "+ (int)(distanceInKm* 1000) + " m away";

            }
            else
            {
                DecimalFormat twoDForm = new DecimalFormat("#0.00");

                // used 1.1 as to get better result for travel distance by road
                distanceString = "approx "+ twoDForm.format(distanceInKm* 1.1) + " Km away";
            }
        }



        //mDistanceToRestaurant.setText(distanceString);

        if (deals.getCan_buy().toLowerCase().equals("yes"))
        {
            mCheckoutBtn.setVisibility(View.VISIBLE);
            mCheckoutLayout.setVisibility(View.VISIBLE);
            chooseMoreText.setVisibility(View.VISIBLE);

            // Fetch all deals from the same merchant
            fetchAllDealsFromThisMerchant();


            showMoreDishes();

            // show increment decrement steeper
            findViewById(R.id.stepper_layout).setVisibility(View.VISIBLE);

            //hide deal not available text
            findViewById(R.id.non_partner_text).setVisibility(View.GONE);

        }
        else {

            mCheckoutBtn.setVisibility(View.GONE);
            mCheckoutLayout.setVisibility(View.GONE);
            chooseMoreText.setVisibility(View.GONE);
            hideMoreDishes();

            // hide increment decrement steeper
            findViewById(R.id.stepper_layout).setVisibility(View.GONE);

            //show deal not available text
            findViewById(R.id.non_partner_text).setVisibility(View.VISIBLE);

        }

    }

    private int calculateDealDiscount(String priceStr, String origPriceStr){

        Float price = Float.parseFloat(priceStr);
        Float origPrice = Float.parseFloat(origPriceStr);

        int discount = (int) ((1 -(price/origPrice))*100);

        return discount;
    }

    private void addOrUpdateOrderList(Integer dealID, String dealAmount, Integer quantity, TextView textView, String dealName, String merchantName) {

        OrderItem order_item = null;

        for (OrderItem orderItem : orderItemList) {
            if (orderItem.getDeal_id().equals(dealID)) {
                order_item = orderItem;
            }
        }

        // If order_item is null, that means the deal is not in the list so add it to the list
        if (order_item == null) {
            order_item = new OrderItem();
            order_item.setQuantity(quantity);
            order_item.setAmount(Double.parseDouble(dealAmount));
            order_item.setDeal_id(dealID);
            order_item.setDeal_name(dealName);
            order_item.setMerchant_name(merchantName);

            orderItemList.add(order_item);
        } else {
            // If order_item is not null then check what is quantitiy.
            // If quantity == 0, then remove the order_item from the list

            orderItemList.remove(order_item);

            if (quantity > 0) {
                order_item.setQuantity(quantity);
                orderItemList.add(order_item);
            }
        }

        // Update the total price
        Double totalPrice = 0.0;
        for (OrderItem orderItem : orderItemList) {
            totalPrice += orderItem.getQuantity() * orderItem.getAmount();
        }

        totalOrderAmount = totalPrice;
        finalPriceTextView.setText("Rs. " + totalPrice.intValue());

        // update the saved data with new changes
        SharedPreferences.Editor editor = sharedPref.edit();
        try {
            editor.putString(deals.getDealId(), getStringForObject(orderItemList));
            editor.commit();
        }
        catch (Exception e) {

        }

        // enable disable text view based on quantity
        if (quantity <= 0) {
            textView.setVisibility(View.INVISIBLE);
        } else {
            textView.setVisibility(View.VISIBLE);
        }
    }

    private void restoreUserSelectionOfDishes() {

        // retrieve data from shared preferences
        ArrayList<OrderItem> orderList = getSavedOrderItemsList();

        if (orderList != null && orderList.size() > 0) {

            //  this means data for this deal was earlier saved
            orderItemList.clear();
            //orderItemList.addAll(orderList);
            int counter = 0;
            for (OrderItem orderItem : orderList) {

                if (orderItem.getDeal_id() == Integer.parseInt(deals.getDealId())) {
                    // Main dish
                    heroDealQuantity.setText(orderItem.getQuantity().toString());

                    addOrUpdateOrderList(orderItem.getDeal_id(), orderItem.getAmount().toString(), orderItem.getQuantity(), heroDealQuantity, orderItem.getDeal_name(), orderItem.getMerchant_name());
                } else {
                    // Assuming that the order of data returned from the server will always be the same
                    TextView[] dishIdTextViews = {dishId_0, dishId_1, dishId_2, dishId_3, dishId_4, dishId_5, dishId_6, dishId_7, dishId_8};
                    TextView[] dishQuantityViews = {dishQuantity_0, dishQuantity_1, dishQuantity_2, dishQuantity_3, dishQuantity_4, dishQuantity_5, dishQuantity_6, dishQuantity_7, dishQuantity_8};
                    int i = 0;
                    for (TextView idTextView : dishIdTextViews) {
                        if (idTextView.getText().equals(orderItem.getDeal_id().toString())) {
                            dishQuantityViews[i].setText(orderItem.getQuantity().toString());
                            addOrUpdateOrderList(orderItem.getDeal_id(), orderItem.getAmount().toString(), orderItem.getQuantity(), dishQuantityViews[i], orderItem.getDeal_name(), orderItem.getMerchant_name());
                        }
                        i++;
                    }
                }

            }

        }
    }

    private String getStringForObject(Object object) {

        Gson gson = new Gson();
        String jsonString = gson.toJson(object);

        return jsonString;
    }

    private ArrayList<OrderItem> getSavedOrderItemsList() {

        Gson gson = new Gson();
        String json = sharedPref.getString(deals.getDealId(), null);
        if (json != null && !json.isEmpty()) {
            Type type = new TypeToken<ArrayList<OrderItem>>() {}.getType();
            ArrayList<OrderItem> arrayList = gson.fromJson(json, type);

            return arrayList;
        }

        return null;
    }


    private void hideMoreDishes()
    {
        itemLayout_1.setVisibility(View.GONE);
        itemLayout_2.setVisibility(View.GONE);
        itemLayout_3.setVisibility(View.GONE);
        itemLayout_4.setVisibility(View.GONE);
        itemLayout_5.setVisibility(View.GONE);
        itemLayout_6.setVisibility(View.GONE);
        itemLayout_7.setVisibility(View.GONE);
        itemLayout_8.setVisibility(View.GONE);
    }

    private void showMoreDishes()
    {
        /*
        itemLayout_1.setVisibility(View.VISIBLE);
        itemLayout_2.setVisibility(View.VISIBLE);
        itemLayout_3.setVisibility(View.VISIBLE);
        itemLayout_4.setVisibility(View.VISIBLE);
        itemLayout_5.setVisibility(View.VISIBLE);
        itemLayout_6.setVisibility(View.VISIBLE);
        itemLayout_7.setVisibility(View.VISIBLE);
        itemLayout_8.setVisibility(View.VISIBLE);
        */
    }

    private void showProgressDialog() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait while we are calcualting the estimated deliver time..");
        progressDialog.setIndeterminate(true);
        //progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    private void hideProgressDialog() {

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.hide();
            progressDialog.dismiss();
        }
    }

    //endregion

    public void checkHungryBellsOperationTime(){

        Time now = new Time();
        now.setToNow();

        int hr = now.hour;
        int min = now.minute;

        if (hr < 9 || (hr >= 21 && min > 30) )
        {
            mClosedTextView.setVisibility(View.VISIBLE);
            mCheckoutLayout.setVisibility(View.GONE);

        }
        else {
            mClosedTextView.setVisibility(View.GONE);
            mCheckoutLayout.setVisibility(View.VISIBLE);
        }
    }

}
