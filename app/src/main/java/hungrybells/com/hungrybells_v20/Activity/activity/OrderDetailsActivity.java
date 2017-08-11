package hungrybells.com.hungrybells_v20.Activity.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.analytics.ecommerce.ProductAction;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.payUMoney.sdk.Session;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hungrybells.com.hungrybells_v20.Activity.app.HBVolley;
import hungrybells.com.hungrybells_v20.Activity.app.HungryBellsApplication;
import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;
import hungrybells.com.hungrybells_v20.Activity.entity.common.RequestObj;
import hungrybells.com.hungrybells_v20.Activity.entity.dealList.Deals;
import hungrybells.com.hungrybells_v20.Activity.entity.discounts.DiscountRequest;
import hungrybells.com.hungrybells_v20.Activity.entity.discounts.DiscountResponse;
import hungrybells.com.hungrybells_v20.Activity.entity.order_processing.CartRequestObject;
import hungrybells.com.hungrybells_v20.Activity.entity.order_processing.GetOrderIDResponse;
import hungrybells.com.hungrybells_v20.Activity.entity.order_processing.OrderItem;
import hungrybells.com.hungrybells_v20.Activity.net.HBGsonRequest;
import hungrybells.com.hungrybells_v20.Activity.utils.Constants;
import hungrybells.com.hungrybells_v20.R;

//TODO add timeout for the create order api of 10 sec

public class OrderDetailsActivity extends ActionBarActivity implements
    Response.Listener<IDataModel>, Response.ErrorListener {
    private String dealId;
    private Deals deals;
    int quantity = 1;
    int priceOfFood = 0;
    Tracker t;
    ArrayList<OrderItem> orderItemList;

    String discountMethod = "";
    String orderid = "";
    String couponCodeApplied = "";
    String paymentId = "";
    int timeout = 10;
    Double minimumOrderValueForFreeDelivery = 0D, deliveryCharge = 0D;
    String customerName = "", customerMobile = "", customerEmail = "", customerAddress = "", customerAdderssLandMark = "";

    int HB_MONEY_USAGE_LIMIT = 300;

    String couponString = "";

    float variable;

    final Calendar deliveryTimeCalendar = Calendar.getInstance();

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        public void run() {
            // Check if progress dialog is till showing and whether or not data has been recieved by the app or not
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                // API call hasnt gone as expected, enable the retry UI
                Toast.makeText(OrderDetailsActivity.this, "Sorry couldn\'t connect to server. Please try again.", Toast.LENGTH_LONG).show();
            }
        }
    };

    boolean shouldPayThroughOnlineMethod = true;

    private ProgressDialog progressDialog;

    private SharedPreferences sharedPref;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @InjectView(R.id.coupon_edit_text)
    MaterialEditText mCouponEditText;

    @InjectView(R.id.coupon_button)
    Button mCouponButton;

    @InjectView(R.id.nameEditText)
    MaterialEditText mNameEditText;

    @InjectView(R.id.mobileEditText)
    MaterialEditText mMobileEditText;

    @InjectView(R.id.emailEditText)
    MaterialEditText mEmailEditText;

    @InjectView(R.id.landmarkEditText)
    MaterialEditText mLandmarkEditText;

    @InjectView(R.id.addressEditText)
    MaterialEditText mAddressEditText;

    //Total Price
    @InjectView(R.id.total_price_layout)
    LinearLayout totalPriceLayout;

    @InjectView(R.id.total_price_text)
    TextView totalPriceField;

    //Delivery charge
    @InjectView(R.id.delivery_charge_layout)
    LinearLayout deliveryChargeLayout;


    @InjectView(R.id.delivery_charge_value)
    TextView deliveryChargeField;

    //Discount
    @InjectView(R.id.discount_amount_layout)
    LinearLayout discountAmountLayout;

    @InjectView(R.id.discount_price_label)
    TextView dicountLabelField;

    @InjectView(R.id.discount_price_value)
    TextView discountAmountField;

    //Final Price
    @InjectView(R.id.final_price_layout)
    LinearLayout finalPriceLayout;

    @InjectView(R.id.final_price_value)
    TextView finalPriceField;

    //Disclaimer
    @InjectView(R.id.disclaimer)
    TextView disclaimerTextView;

    //HB money
    @InjectView(R.id.hbmoney_checkbox)
    CheckBox mHBMoneyCheckbox;

    @InjectView(R.id.discount_layout)
    LinearLayout mDiscountlayout;

    //Repeat order discount
    @InjectView(R.id.radio_repeat_discount)
    RadioButton repeatDiscountRadioButton;

    @InjectView(R.id.repeat_order_discount_layout)
    LinearLayout repeatOrderDiscountLayout;

    @InjectView(R.id.repeat_order_message_layout)
    LinearLayout repeatOrderDiscountMessageLayout;

    @InjectView(R.id.repeat_order_customer_addressing_text)
    TextView repeatOrderCustomerAddressingField;

    @InjectView(R.id.repeat_order_message)
    TextView repeatOrderDiscountMessageField;

    @InjectView(R.id.radio_coupon)
    RadioButton couponRadioButton;

    @InjectView(R.id.deliver_now)
    RadioButton deliverNowButton;

    @InjectView(R.id.deliver_later)
    RadioButton deliverlaterButton;

    int repeatDiscountType = 0;
    Float minimumRepeatOrderValue = 0F, repeatDiscountMaxValue = 0F, repeatDiscountValue = 0F;
    String repeatOrderDiscountMessage = "";
    boolean REPEAT_ORDER_DISCOUNT_AVAILABLE = false;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    NumberFormat numberFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Constants.HB_MONEY=1000;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        ButterKnife.inject(this);
        setupToolbar();

        numberFormat = NumberFormat.getCurrencyInstance();

        // Initialize Google Analytics
        try {
            t = ((HungryBellsApplication) getApplication()).getTracker(HungryBellsApplication.TrackerName.APP_TRACKER);
            t.setScreenName("Your Order View");
            t.send(new HitBuilders.AppViewBuilder().build());
        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(), "Error"+e.getMessage(), 1).show();
        }

        loadData();

        if (!REPEAT_ORDER_DISCOUNT_AVAILABLE) {
            repeatDiscountRadioButton.setVisibility(RadioButton.GONE);
            couponRadioButton.setChecked(true);
            showCoupon();
        } else {
            repeatOrderCustomerAddressingField.setText("Welcome back " + customerName.toUpperCase() + "!");
        }

        //Show / hide repeat order discount controls
        if (repeatDiscountType == 0) {
            repeatDiscountRadioButton.setVisibility(RadioButton.GONE);
            repeatOrderDiscountLayout.setVisibility(LinearLayout.GONE);
            repeatOrderDiscountMessageLayout.setVisibility(LinearLayout.GONE);
        }
    }

    private void loadData() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Constants.DEAL_ID_KEY)) {
            dealId = intent.getStringExtra(Constants.DEAL_ID_KEY);

            orderItemList = (ArrayList<OrderItem>) intent.getSerializableExtra(Constants.ORDER_LIST);

            //Total Price
            Float totalPrice = 0F;
            try {
                totalPrice = (float) intent.getDoubleExtra(Constants.TOTAL_PRICE, 0.0);
            } catch (Exception e) {
                Log.i("Total Price parse error", e.getMessage());
            }
            totalPriceField.setText(format(totalPrice + "") + "");

            this.deals = (Deals) intent.getSerializableExtra("dealItem");

            customerAddress = intent.getStringExtra(Constants.CUSTOMER_ADDRESS);
            customerMobile = intent.getStringExtra(Constants.CUSTOMER_MOBILE);
            customerEmail = intent.getStringExtra(Constants.CUSTOMER_EMAIL);
            customerName = intent.getStringExtra(Constants.CUSTOMER_NAME);
            customerAdderssLandMark = intent.getStringExtra(Constants.CUSTOMER_ADDRESS_LANDMARK);

            String repeatDiscountTypeText = intent.getStringExtra(Constants.REPEAT_DISCOUNT_TYPE);
            String minimumRepeatOrderValueText = intent.getStringExtra(Constants.REPEAT_MIN_ORDER_VALUE);
            String repeatDiscountValueText = intent.getStringExtra(Constants.REPEAT_DISCOUNT_VALUE);
            String repeatDiscountMaxValueText = intent.getStringExtra(Constants.REPEAT_DISCOUNT_MAX_VALUE);
            repeatOrderDiscountMessage = intent.getStringExtra(Constants.REPEAT_DISCOUNT_MSG);

            repeatDiscountType = 0;
            try {
                repeatDiscountType = Integer.parseInt(repeatDiscountTypeText);
            } catch (Exception e) {
                repeatDiscountType = 0;
            }

            repeatDiscountValue = 0F;
            try {
                repeatDiscountValue = Float.parseFloat(repeatDiscountValueText);
            } catch (Exception e) {
                repeatDiscountValue = 0F;
            }

            try {
                minimumRepeatOrderValue = Float.parseFloat(minimumRepeatOrderValueText);
            } catch (Exception e) {
                minimumRepeatOrderValue = 0F;
            }

            repeatDiscountMaxValue = 0F;
            try {
                repeatDiscountMaxValue = Float.parseFloat(repeatDiscountMaxValueText);
            } catch (Exception e) {
                repeatDiscountMaxValue = 0F;
            }

            //Delivery charge
            try {
                minimumOrderValueForFreeDelivery = Double.parseDouble(intent.getStringExtra(Constants.MINIMUM_ORDER_VALUE_FOR_FREE_DELIVERY));
            } catch (Exception e) {
                minimumOrderValueForFreeDelivery = 0D;
            }

            //Toast.makeText(getApplicationContext(), intent.getStringExtra(Constants.DELIVERY_CHARGE), Toast.LENGTH_LONG).show();

            try {
                deliveryCharge = Double.parseDouble(intent.getStringExtra(Constants.DELIVERY_CHARGE));
            } catch (Exception e) {
                deliveryCharge = 0D;
            }

            if (repeatDiscountType > 0)
                REPEAT_ORDER_DISCOUNT_AVAILABLE = true;

            //Apply delivery charge for small orders
            refreshFinalPrice();

            preFillUserDetails();
        }
    }

    private void refreshFinalPrice() {
        applyDeliveryCharge();
        finalPriceField.setText(getFinalPrice() + "");
        // Toast.makeText(getApplicationContext(),"T:"+getTotalPrice()+" F:"+getFinalPrice(),Toast.LENGTH_LONG).show();
        if (getFinalPrice() == getTotalPrice()) {
            finalPriceLayout.setVisibility(View.GONE);
        } else
            finalPriceLayout.setVisibility(View.VISIBLE);
    }

    private float getDeliveryCharge() {
        return Float.parseFloat(deliveryChargeField.getText().toString());
    }

    private float getDiscountAmount() {
        return Float.parseFloat(discountAmountField.getText().toString());
    }

    private float getFinalPrice() {
        return getTotalPrice() + getDeliveryCharge() - getDiscountAmount();
    }

    private String format(String number) {
        float value = Float.parseFloat(number);
        int integer = 0;
        if(number.indexOf(".")>0)
            integer=Integer.parseInt(number.substring(0,number.indexOf(".")-1));

        float fraction = value - integer;

        //Toast.makeText(getApplicationContext(),"INT:"+integer+" FRAC:"+fraction,Toast.LENGTH_LONG).show();
        if (fraction == 0)
            return integer + "";
        return number + "";
    }

    //Check if eligible for repeat order discount and apply discount
    public void availRepeatOrderDiscount(View view) {
        Log.i("Repeat disount", repeatDiscountType + "");
        // 0 = %, 1 = Rs
        if (repeatDiscountType > 0) {
            float discountAmount = 0;

            if (repeatOrderDiscountMessage != null && !repeatOrderDiscountMessage.trim().isEmpty()) {
                showMessage("Congratulations!", repeatOrderDiscountMessage + "\n\nMinimum order value - Rs." + format(minimumRepeatOrderValue + "") + "\nMaximum discount upto - Rs." + format(repeatDiscountMaxValue + ""), "Great!");
            }

            //Check discount type - percentage or Rs
            if (repeatDiscountType == 1 && getTotalPrice() >= minimumRepeatOrderValue) {
                //calculation discount %
                discountAmount = getTotalPrice() * repeatDiscountValue / 100;
                //Limit to maximum discount value
                discountAmount = (discountAmount > repeatDiscountMaxValue) ? repeatDiscountMaxValue : discountAmount;
            } else if (repeatDiscountType == 2 && getTotalPrice() >= minimumRepeatOrderValue) {
                //Direct discount
                discountAmount = (repeatDiscountMaxValue > discountAmount) ? repeatDiscountValue : repeatDiscountMaxValue;
            }

            if (getTotalPrice() >= minimumRepeatOrderValue) {
                //Doscount applied
                setDisclaimer("");

                //Show repeat order discount message
                repeatOrderDiscountMessageField.setText(repeatOrderDiscountMessage);

                //apply discount
                discountMethod = "repeat_order";

                discountAmountField.setText(discountAmount + "");
                discountAmountLayout.setVisibility(LinearLayout.VISIBLE);
            } else {
                repeatOrderDiscountMessageField.setText(repeatOrderDiscountMessage);
                setDisclaimer("Add something for Rs." + format((minimumRepeatOrderValue - getTotalPrice()) + "") + " to avail the SPECIAL OFFER!");
                discountAmountField.setText("0");
                discountAmountLayout.setVisibility(LinearLayout.GONE);
            }


            repeatOrderDiscountLayout.setVisibility(LinearLayout.GONE);
            repeatOrderDiscountMessageLayout.setVisibility(View.VISIBLE);
        } else {
            discountAmountField.setText("0");
            discountAmountLayout.setVisibility(LinearLayout.GONE);
        }

        refreshFinalPrice();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back);
        getSupportActionBar().setTitle("");
        TextView mTextTitle = (TextView) findViewById(R.id.toolbar_title);
        mTextTitle.setText("Your Order");
    }

    private void preFillUserDetails() {
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        mHBMoneyCheckbox.setText("Use Hungry Bells Money ( Rs " + Constants.HB_MONEY + " )");

        if (deals.getDealPrice() != null) {
            priceOfFood = (int) Float.parseFloat(deals.getDealPrice());
        } else {
            priceOfFood = 0;
        }

        // pre-fill user data
        /*String name = sharedPref.getString("userName", "");
        String email = Constants.DEVICE_EMAIL == null || Constants.DEVICE_EMAIL.equals("") ? sharedPref.getString("email", "") : Constants.DEVICE_EMAIL;
        String mobile = Constants.DEVICE_PHONE_NO == null || Constants.DEVICE_PHONE_NO.equals("") ? sharedPref.getString("mobile", "") : Constants.DEVICE_PHONE_NO;*/

        mNameEditText.setText(customerName == null ? "" : customerName);
        mEmailEditText.setText(customerEmail == null ? "" : customerEmail);
        mMobileEditText.setText(customerMobile == null ? "" : customerMobile);
        mAddressEditText.setText(customerAddress == null ? "" : customerAddress);
        //Toast.makeText(getApplicationContext(),"Landmark:"+customerAdderssLandMark,Toast.LENGTH_LONG).show();
        mLandmarkEditText.setText(customerAdderssLandMark == null ? "" : customerAdderssLandMark);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_order_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    //region Web Request Handlers

    @Override
    public void onResponse(IDataModel dataModel) {
        if (progressDialog != null && progressDialog.isShowing()) {
            try {
                progressDialog.dismiss();
            } catch (Exception e) {
                //TODO
            }
        }

        if (dataModel instanceof GetOrderIDResponse) {
            GetOrderIDResponse getOrderIDResponse = (GetOrderIDResponse) dataModel;
            orderid = getOrderIDResponse.getOrderid();

            if (getOrderIDResponse.getCode().equals("1")) {
                if (shouldPayThroughOnlineMethod) {
                    if (getFinalPrice() == 0) {
                        navigateUserToConfirmationPage();
                    } else {
                        createPayUPaymentRequest(getOrderIDResponse);
                    }
                } else {
                    navigateUserToConfirmationPage();
                }
            } else {
                showErrorMessage(getOrderIDResponse.getMessage());
            }

        } else if (dataModel instanceof DiscountResponse) {
            DiscountResponse discountResponse = (DiscountResponse) dataModel;
            couponCodeApplied = discountResponse.getCoupon_code();

            if (discountResponse.getStatus().equals("failure")) {
                Toast.makeText(this,
                        discountResponse.getError() == null ? "Invalid or Expired Coupon. Please try with another one." : discountResponse.getError(),
                        Toast.LENGTH_LONG).show();
            } else {
                Double per = Double.parseDouble(discountResponse.getPercentage());
                Double maxAmount = Double.parseDouble(discountResponse.getMax_value());
                Double amt = getTotalPrice() * per / 100;
                Double discountAmount = amt > maxAmount ? maxAmount : amt;
                discountMethod = "discount_code";

                discountAmountField.setText(discountAmount + "");
                //dicountLabelField.setText("Discount ( "+couponString.toUpperCase() + " )");
                discountAmountLayout.setVisibility(LinearLayout.VISIBLE);
                setDisclaimer("");
                refreshFinalPrice();
                preFillUserDetails();
            }
        }
    }

    private float getTotalPrice() {
        float totalPrice = Float.parseFloat(totalPriceField.getText().toString());
        return totalPrice;
    }

    public void onErrorResponse(VolleyError error) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    //endregion

    // Buy Button Click Handler
    public void initiateOrderProcessing(View v) {
        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder()
                .setCategory("ORDER")
                .setAction("Checkout Clicked")
                .build());


        if (isNetworkConnected()) {
           /* // Saver User details
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("userName", mNameEditText.getText().toString());
            editor.putString("email", mEmailEditText.getText().toString());
            editor.putString("mobile", mMobileEditText.getText().toString());
            editor.commit();*/

            boolean isDataValid = false;

            // Check for validity of the data entered by the user
            if (mNameEditText.getText().toString().isEmpty()) {
                mNameEditText.setError("Name must not be empty");
                return;
            } else if (mMobileEditText.getText().toString().isEmpty()) {
                mMobileEditText.setError("Phone number should not be empty");
                return;
            } else if (!Patterns.PHONE.matcher(mMobileEditText.getText().toString()).matches()) {
                mMobileEditText.setError("Phone number seems to be incorrect");
                return;
            } else if (mMobileEditText.getText().toString().length() != 10) {
                mMobileEditText.setError("Phone number must be 10 digits only");
                return;
            } else if (mEmailEditText.getText().toString().isEmpty()) {
                mEmailEditText.setError("Email must not be empty");
                return;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(mEmailEditText.getText().toString()).matches()) {
                mEmailEditText.setError("Email format seems to be incorrect");
                return;
            } else if (mAddressEditText.getText().toString().isEmpty()) {
                mAddressEditText.setError("Address shouldn't be empty");
                return;
            }

            // Set HBMoney deduction value
            if (discountMethod != null && discountMethod.equals("hb_money")) {
                Constants.HB_MONEY_USED_IN_TRANS = (int) getDiscountAmount();
                Constants.PYMNT_MODE_HB_MONEY = true;
            } else {
                Constants.PYMNT_MODE_HB_MONEY = false;
            }

            // Creating the body of request object
            CartRequestObject cartRequestObject = new CartRequestObject();
            cartRequestObject.setUser_id(Constants.USER_ID);
            cartRequestObject.setLatitude(Constants.DEVICE_LAT.equals("") ? "0" : Constants.DEVICE_LAT);
            cartRequestObject.setLongitude(Constants.DEVICE_LONG.equals("") ? "0" : Constants.DEVICE_LONG);
            cartRequestObject.setFirst_name(mNameEditText.getText().toString());
            cartRequestObject.setEmail(mEmailEditText.getText().toString());
            cartRequestObject.setMobile_number(mMobileEditText.getText().toString());
            cartRequestObject.setAddress(mAddressEditText.getText().toString());
            cartRequestObject.setLandmark(mLandmarkEditText.getText().toString());
            cartRequestObject.setOrder_amount(getFinalPrice() + "");
            cartRequestObject.setOrder_type(shouldPayThroughOnlineMethod ? (getFinalPrice() == 0 ? "FO" : "PAYU") : "COD");
            cartRequestObject.setOrders(orderItemList);

            //Delivery date / time
            cartRequestObject.setDeliveryDate(new SimpleDateFormat("dd-MM-yyyy").format(deliveryTimeCalendar.getTime()));
            cartRequestObject.setDeliveryTime(new SimpleDateFormat("HH:mm a").format(deliveryTimeCalendar.getTime()));

            cartRequestObject.setDiscount_amount(getDiscountAmount() == 0 ? "" : getDiscountAmount() + "");
            cartRequestObject.setDiscount_method(getDiscountAmount() == 0 ? "none" : (getDiscountAmount() > 0 ? discountMethod : "none"));
            cartRequestObject.setCoupon_code(couponCodeApplied == null || couponCodeApplied.isEmpty() ? "none" : couponCodeApplied);

            // Forming the request object
            RequestObj requestObj = new RequestObj();
            requestObj.setBody(cartRequestObject);

            Gson gson = new Gson();
            String jsonString = gson.toJson(requestObj);

            // Initiating request to server for the order processing with order processing type
            HBVolley.getRequestQueue(getApplicationContext()).add(
                    new HBGsonRequest(
                            Constants.GET_ORDER_ID_FOR_CART_URL,
                            this,
                            this,
                            new GetOrderIDResponse(),
                            null,
                            null,
                            jsonString,
                            Request.Method.POST
                    )).setShouldCache(true);

            // Show the processign screen
            // Show Pregress Dialog before any setup
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait while we process your order..");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            createTimeoutTimer();
        } else {
            showErrorMessage("No network available");
        }
    }

    public void stepDownCounterClicked(View v) {
        if (quantity > 1) {
            quantity--;
            //mQuantityTextView.setText("x"+quantity);
            //mPriceTextView.setText("Total : Rs "+ (priceOfFood*quantity));
        }
    }

    public void stepUpCounterClicked(View v) {
        if (quantity < 10 && quantity > 0) {
            quantity++;
            //mQuantityTextView.setText("x"+quantity);
            //mPriceTextView.setText("Total : Rs " + (priceOfFood * quantity));
        }
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

    private void showErrorMessage(String msg) {
        new MaterialDialog.Builder(this)
                .title("Alert")
                .content(msg.isEmpty() ? "Opps! Unable to connect to server right now. Please try again." : msg)
                .positiveText("Retry")
                .negativeText("Cancel")
                .positiveColor(Color.BLUE)
                .negativeColor(Color.GRAY)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        retryServerConnection();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                    }
                })
                .show();
    }

    private void retryServerConnection() {
        initiateOrderProcessing(null);
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    private void setDisclaimer(String text) {
        disclaimerTextView.setText(text);
        disclaimerTextView.setVisibility(View.VISIBLE);
    }


    public void couponApplyClicked(View view) {
        if (mCouponEditText.getText().toString() == null || mCouponEditText.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter a coupon code", Toast.LENGTH_LONG).show();
            return;
        }

        //Clear HB Money selection
        mHBMoneyCheckbox.setChecked(false);

        // dissmiss keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mCouponEditText.getWindowToken(), 0);

        // Show Pregress Dialog before any setup
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait while we are checking the code..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        couponString = mCouponEditText.getText().toString();

        DiscountRequest dealDiscountRequest = new DiscountRequest();
        dealDiscountRequest.setCoupanCode(couponString);
        dealDiscountRequest.setMerchantbranch_id(deals.getMerchantBranchid());
        dealDiscountRequest.setTotal_order_value(getFinalPrice() + "");
        dealDiscountRequest.setUser_id(Constants.USER_ID);

        RequestObj requestObj = new RequestObj();
        requestObj.setBody(dealDiscountRequest);

        Gson gson = new Gson();

        // Inform server that this deal is being viewed
        HBVolley.getRequestQueue(getApplicationContext()).add(
                new HBGsonRequest(
                        Constants.GET_DISCOUNT_DETAILS,
                        this,
                        this,
                        new DiscountResponse(),
                        null,
                        null,
                        gson.toJson(dealDiscountRequest),
                        Request.Method.POST
                )).setShouldCache(true);
    }

    private void createPayUPaymentRequest(GetOrderIDResponse getOrderIDResponse) {

        String key = false ? "xzG06e" : "QHC8Wx";
        String salt = false ? "01qCBz1g" : "QTiOlCOR";
        String merchantId = false ? "4929141" : "5112684";

        String amt = Constants.DEBUG ? "1" : (getFinalPrice() + "");
        String firstName = customerName;
        String email = mEmailEditText.getText().toString();

        String hashSequence = key + "|" + getOrderIDResponse.getOrderid() + "|" + amt + "|" + "Online Product" + "|" + firstName + "|"
                + email + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + "" + "|" + salt;
        String hash = hashCal("SHA-512", hashSequence);
        Log.i("hash", hash);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("TxnId", getOrderIDResponse.getOrderid());
        params.put("key", key);
        params.put("MerchantId", merchantId);
        params.put("Amount", amt);
        params.put("ProductInfo", "Online Product");
        params.put("SURL", Constants.POST_TRANSACTION_DATA_TO_SERVER_URL);//"https://www.payumoney.com/mobileapp/payumoney/success.php");
        params.put("FURL", Constants.POST_TRANSACTION_DATA_TO_SERVER_URL);//"https://www.payumoney.com/mobileapp/payumoney/failure.php");
        //params.put("CURL", Constants.POST_TRANSACTION_DATA_TO_SERVER_URL);
        params.put("firstName", firstName);
        params.put("Email", email);
        params.put("Phone", mMobileEditText.getText().toString());
        params.put("hash", hash);
        params.put("udf1", "");
        params.put("udf2", "");
        params.put("udf3", "");
        params.put("udf4", "");
        params.put("udf5", "");


        if (Session.getInstance(this) == null) {
            Session.startPaymentProcess(this, params);
        } else {
            Session.createNewInstance(this);
        }

        Session.startPaymentProcess(this, params);

    }

    public static String hashCal(String type, String str) {

        byte[] hashseq = str.getBytes();

        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest algorithm = MessageDigest.getInstance(type);
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();
            for (int i = 0; i < messageDigest.length; i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException nsae) {
        }
        return hexString.toString();
    }

    // Recevie the result once PayUActivity finishes
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Session.PAYMENT_SUCCESS) {
            if (resultCode == RESULT_OK) {
                //success
                Toast.makeText(this, "success", Toast.LENGTH_LONG).show();

                paymentId = data.getStringExtra("paymentId");

                navigateUserToConfirmationPage();

            }
            if (resultCode == RESULT_CANCELED) { //failed
                Toast.makeText(this, "failed", Toast.LENGTH_LONG).show();
            }
        }
    }

    //region Radio button handler

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_pirates:
                if (checked)
                    // Pirates are the best
                    shouldPayThroughOnlineMethod = false;
                hideCoupon();
                ((TextView) findViewById(R.id.no_discount_cod_tv)).setVisibility(View.VISIBLE);
                mDiscountlayout.setVisibility(View.GONE);

                int discountAmount = 0;

                // Set the disocunt and final price text
                discountAmountField.setText(discountAmount + "");
                finalPriceField.setText(format(getFinalPrice() - discountAmount + ""));
                finalPriceField.setVisibility(View.GONE);
                finalPriceField.setVisibility(View.GONE);
                dicountLabelField.setVisibility(View.GONE);
                discountAmountField.setVisibility(View.GONE);
                break;
            case R.id.radio_ninjas:
                if (checked)
                    shouldPayThroughOnlineMethod = true;
                showCoupon();
                ((TextView) findViewById(R.id.no_discount_cod_tv)).setVisibility(View.GONE);
                mDiscountlayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void tabClicked(View v) {
        // Is the button now checked?
        boolean checked = ((RadioButton) v).isChecked();

        // Check which radio button was clicked
        switch (v.getId()) {
            case R.id.radio_repeat_discount:
                if (checked) {
                    //Hide BH money checkbox
                    mHBMoneyCheckbox.setVisibility(View.GONE);

                    //show gift icon and text
                    if (discountMethod.equals("repeat_order")) {
                        repeatOrderDiscountLayout.setVisibility(LinearLayout.GONE);
                        repeatOrderDiscountMessageLayout.setVisibility(LinearLayout.VISIBLE);
                    } else {
                        repeatOrderDiscountLayout.setVisibility(LinearLayout.VISIBLE);
                        repeatOrderDiscountMessageLayout.setVisibility(LinearLayout.GONE);
                    }

                    hideCoupon();
                }
                break;
            case R.id.radio_coupon:
                if (checked) {
                    mHBMoneyCheckbox.setVisibility(View.GONE);

                    //hide repeat discount
                    repeatOrderDiscountLayout.setVisibility(LinearLayout.GONE);
                    repeatOrderDiscountMessageLayout.setVisibility(LinearLayout.GONE);

                    showCoupon();
                    setDisclaimer("");
                }
                break;
            case R.id.radio_hbmoney:
                if (checked) {
                    mHBMoneyCheckbox.setVisibility(View.VISIBLE);
                    setDisclaimer("Maximum redemption upto Rs." + format(HB_MONEY_USAGE_LIMIT + "") + " or 50% of total price.");
                    hideCoupon();
                    repeatOrderDiscountLayout.setVisibility(LinearLayout.GONE);
                    repeatOrderDiscountMessageLayout.setVisibility(LinearLayout.GONE);
                }
                break;
        }
    }

    private void hideCoupon() {
        ((RelativeLayout) findViewById(R.id.coupon_layout)).setVisibility(View.GONE);
    }

    private void showCoupon() {
        ((RelativeLayout) findViewById(R.id.coupon_layout)).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.no_discount_cod_tv)).setVisibility(View.GONE);
        hideRepeatOrderDiscount();
    }

    private void hideRepeatOrderDiscount() {
        repeatOrderDiscountLayout.setVisibility(LinearLayout.GONE);
        repeatOrderDiscountMessageLayout.setVisibility(LinearLayout.GONE);
    }

    private void showRepeatOrderDiscount() {
        repeatOrderDiscountLayout.setVisibility(LinearLayout.VISIBLE);
        repeatOrderDiscountMessageLayout.setVisibility(LinearLayout.VISIBLE);
    }


    public void hbmoneyChcekboxClicked(View view) {
        if (mHBMoneyCheckbox.isChecked()) {

            //Clear coupon
            mCouponEditText.setText("");

            //Calculate HB money to be deducted
            Double discountAmount = (double) Constants.HB_MONEY;

            //Not maximum than total
            discountAmount = discountAmount > getTotalPrice() ? getTotalPrice() : discountAmount;

            //only upto 50% of total
            discountAmount = (discountAmount >= getTotalPrice() / 2) ? getTotalPrice() / 2 : discountAmount;

            //Limit max HB money usage
            discountAmount = discountAmount < HB_MONEY_USAGE_LIMIT ? discountAmount : HB_MONEY_USAGE_LIMIT;

            discountMethod = "hb_money";
            //Show discount type
            //dicountLabelField.setText("Discount ( HB MONEY )");

            discountAmountField.setText(discountAmount + "");

            discountAmountLayout.setVisibility(LinearLayout.VISIBLE);
        } else {
            discountAmountField.setText("0");
            discountAmountLayout.setVisibility(LinearLayout.GONE);
        }
        refreshFinalPrice();
    }

    //region Helper Methods
    private void navigateUserToConfirmationPage() {
        onPurchaseCompleted();
        // Order has been placed successfully. Show some confirmation screen to the user
        Intent i = new Intent(this, ConformationActivity.class);
        startActivity(i);
    }

    /**
     * The purchase was processed. We will send the transaction and its associated line items to Google Analytics,
     * but only if the purchase has been confirmed.
     */
    public void onPurchaseCompleted() {

        ProductAction productAction = new ProductAction(ProductAction.ACTION_PURCHASE)
                .setTransactionId(paymentId.isEmpty() ? orderid : paymentId)
                .setTransactionAffiliation("Hungry Bells App - Android")
                .setTransactionRevenue(getTotalPrice())
                .setTransactionTax(0.0)
                .setTransactionShipping(0.0)
                .setTransactionCouponCode(couponCodeApplied);

        HitBuilders.ScreenViewBuilder builder = new HitBuilders.ScreenViewBuilder()
                .setProductAction(productAction);

        int counter = 0;
        for (OrderItem orderItem : orderItemList) {
            Product product = new Product()
                    .setId(orderItem.getDeal_id().toString())
                    .setName(orderItem.getDeal_name())
                    .setBrand(orderItem.getMerchant_name())
                    .setPrice(orderItem.getAmount())
                    .setQuantity(orderItem.getQuantity());

            builder.addProduct(product);
        }

        Tracker t = ((HungryBellsApplication) getApplication()).getTracker(HungryBellsApplication.TrackerName.APP_TRACKER);
        //t.setScreenName("OrderDetails");
        t.send(builder.build());
    }

    private void createTimeoutTimer() {
        handler.postDelayed(runnable, Constants.INTERVAL);
    }

    //endregion

    private void showMessage(String heading, String message, String buttonText) {
        new MaterialDialog.Builder(this)
                .title(heading)
                .content(message)
                .positiveText(buttonText)
                .positiveColor(Color.BLUE)
                .show();
    }

    private void applyDeliveryCharge() {
        if (getTotalPrice() < minimumOrderValueForFreeDelivery) {
            deliveryChargeField.setText(format(deliveryCharge + "") + "");
            deliveryChargeLayout.setVisibility(LinearLayout.VISIBLE);
            setDisclaimer("Add something for Rs." + format((minimumOrderValueForFreeDelivery - getTotalPrice()) + "") + " and get FREE DELIVERY!");
            finalPriceField.setText(format(getFinalPrice() + "") + "");
        } else {
            deliveryChargeField.setText("0");
            deliveryChargeLayout.setVisibility(LinearLayout.GONE);
        }
    }

    public void onDeliverLaterClicked(View view) {
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
                // TODO Auto-generated method stub
                deliveryTimeCalendar.set(Calendar.YEAR, year);
                deliveryTimeCalendar.set(Calendar.MONTH, monthOfYear);
                deliveryTimeCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                TimePickerDialog.OnTimeSetListener time=new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        deliveryTimeCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        deliveryTimeCalendar.set(Calendar.MINUTE, minute);

                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd MMM, hh:mm a");
                        String deliveryTime=simpleDateFormat.format(deliveryTimeCalendar.getTime());

                        deliverlaterButton.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                        deliverlaterButton.setText(deliveryTime);
                    }
                };
                new TimePickerDialog(OrderDetailsActivity.this,time, deliveryTimeCalendar.get(Calendar.HOUR_OF_DAY), deliveryTimeCalendar.get(Calendar.HOUR_OF_DAY),false).show();
            }
        };

        DatePickerDialog datePickerDialog=new DatePickerDialog(this, date, deliveryTimeCalendar
                .get(Calendar.YEAR), deliveryTimeCalendar.get(Calendar.MONTH),
                deliveryTimeCalendar.get(Calendar.DAY_OF_MONTH));
        //Max 3 days from now
        datePickerDialog.getDatePicker().setMaxDate(deliveryTimeCalendar.getTimeInMillis()+3*24*60*60);
        datePickerDialog.getDatePicker().setMinDate(deliveryTimeCalendar.getTimeInMillis());
        datePickerDialog.show();
    }

    public void onDeliverNowClicked(View view) {
        deliverlaterButton.setText("Later");
        deliverlaterButton.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
    }
}
