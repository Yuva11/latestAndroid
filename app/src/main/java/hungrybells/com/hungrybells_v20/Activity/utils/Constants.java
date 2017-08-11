package hungrybells.com.hungrybells_v20.Activity.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Debug;

import hungrybells.com.hungrybells_v20.Activity.entity.dealList.Deals;

/**
 * Created by ajeetkumar on 17/05/15.
 */
public class Constants {
    public static final String MIXPANEL_TOKEN = "d42d7c478b85c3c6123c6b13722bf80e";

    public static String USER_ID = null;
    public static String LONGITUDE = "";   // this changes when user changes location
    public static String LATITUDE = "";   // this changes when user changes location

    public static String DEVICE_LONG = ""; // this changes only when the device changes the location
    public static String DEVICE_LAT = ""; // this changes only when the device changes the location
    public static String DEVICE_EMAIL = "";
    public static String DEVICE_PHONE_NO = "";

    public static final boolean DEBUG = false;
    public static final boolean ANALYTICS_DEBUG = DEBUG;

    public static boolean IS_APP_SESSION_ACTIVE = false;
    public static boolean shouldUpldatePlaceLatLong = true;   // this values allows the app to udpate Constants.Lattitude, Constans.Longitude. This should not be update when user is coming from change locaiton activity
    public static final String PARSE_ERROR = "Parse Error";

    public static final String FOOD_TAG = "FOOD_TAG_STRING";

    public static String FOOD_TAG_VARIABLE = "";

    public static Deals DEAL_ITEM = null;

    //place name
    public static String LOCATION_NAME = "";

    // Keys for extras passed between activities
    public static final String locationsList = "locationsList";
    public static final String selectedLocation = "selectedLocation";
    // URLs Used
    public static final String TEST_ENV = "http://testservice.hungrybells.in:9091/HBAppService";
    public static final String PROD_ENV = "http://service.hungrybells.in:9090/HBAppService";
    public static final String TAGS_LIST_URL =  (DEBUG ? TEST_ENV : PROD_ENV) + "/newHomePagejson.do";
    public static final String CATEGORY_LIST_URL =  (DEBUG ? TEST_ENV : PROD_ENV) + "/getCategory.do";
    public static final String FAV_TAGS_LIST_URL =  (DEBUG ? TEST_ENV : PROD_ENV) + "/newHomePagejsonForFavTag.do";
    public static final String SEARCH_TAGS_URL = (DEBUG ? TEST_ENV : PROD_ENV) + "/searchjson.do";
    public static final String DEALS_LIST_URL = (DEBUG ? TEST_ENV : PROD_ENV) + "/tagListDealsjson.do";
    public static final String DEALS_BY_CATEGORY_URL = (DEBUG ? TEST_ENV : PROD_ENV) + "/getCategoryDeals.do";
    public static final String SEND_LIKE_URL = (DEBUG ? TEST_ENV : PROD_ENV) + "/userLikejson.do";
    public static final String CHECK_LIKE_URL = "";
    public static final String ADD_FAV_URL = (DEBUG ? TEST_ENV : PROD_ENV) + "/userFavjson.do";
    public static final String DEAL_SHARE_LOG_URL = (DEBUG ? TEST_ENV : PROD_ENV) + "/userSharejson.do";
    public static final String DEAL_VIEW_COUNT_LOG_URL = (DEBUG ? TEST_ENV : PROD_ENV) + "/userViewjson.do";
    public static final String FEEDBACK_URL = (DEBUG ? TEST_ENV : PROD_ENV) + "/feedback/add/124";
    public static final String GET_ORDER_ID_URL = (DEBUG ? TEST_ENV : PROD_ENV) + "/orderDetails.do";
    public static final String GET_LOCATIONS_WITH_ACTIVE_DEALS = (DEBUG ? TEST_ENV : PROD_ENV) + "/changeLocation.do";
    public static final String GET_ALL_ORDER_FROM_SINGLE_MERCHANT_URL = (DEBUG ? TEST_ENV : PROD_ENV) + "/addToCartOrderDeal.do";
    public static final String GET_ORDER_ID_FOR_CART_URL = (DEBUG ? TEST_ENV : PROD_ENV) + "/addToCartOrderDetails.do";
    public static final String POST_TRANSACTION_DATA_TO_SERVER_URL = (DEBUG ? TEST_ENV : PROD_ENV) +"/success.jsp";
    public static final String GET_DISCOUNT_DETAILS = (DEBUG ? TEST_ENV : PROD_ENV) + "/checkDiscountCode.do";
    public static final String CHECK_FEEDBACK_URL = (DEBUG ? TEST_ENV : PROD_ENV) + "/checkFeedback.do";
    // Old-URLs used
    public static final String GET_NEAREST_LOCATION_URL = "http://service.hungrybells.in:9090/location/getnearestlocation/";
    public static final String GET_DRIVING_DISTANCE_TO_RESTAURANT = (DEBUG ? TEST_ENV : PROD_ENV) + "/checkDistance.do";
    public static final String POST_FEEDBACK_TO_SERVER_URL = (DEBUG ? TEST_ENV : PROD_ENV) + "/postFeedback.do";
    public static final String POST_FETCH_MY_ORDERS_URL = (DEBUG ? TEST_ENV : PROD_ENV) + "/myorders.do";

    // Timer parameters
    public static final int INTERVAL = 1200 * 20 ;  // 10 sec delay

    // Keys passed in intents
    public static final String DEAL_ID_KEY = "deal_id";
    public static final String ORDER_LIST = "order_list";
    public static final String TOTAL_PRICE = "totalPrice";
    public static final String ORDER_ID = "orderId";

    public static final String REPEAT_DISCOUNT_TYPE = "repeatDiscountCount";
    public static final String REPEAT_DISCOUNT_VALUE = "repeatDiscountValue";
    public static final String REPEAT_MIN_ORDER_VALUE = "repeatMinimumOrderValue";
    public static final String REPEAT_DISCOUNT_MAX_VALUE = "repeatDiscountMaxValue";
    public static final String REPEAT_DISCOUNT_MSG = "repeatDiscountMsg";
    public static final String DELIVERY_CHARGE = "deliveryCharge";
    public static final String MINIMUM_ORDER_VALUE_FOR_FREE_DELIVERY = "minimumOrderValueForFreeDelivery";
    public static final String HB_MONEY_MAX_LIMIT = "hbMoneyMaxLimit";
    public static final String CUSTOMER_NAME = "customerName";
    public static final String CUSTOMER_MOBILE = "customerMobile";
    public static final String CUSTOMER_EMAIL = "customerEmail";
    public static final String CUSTOMER_ADDRESS = "customerAddress";
    public static final String CUSTOMER_ADDRESS_LANDMARK = "customerAddressLandMark";

    // MAT SDK values
    public static final String MAT_ADVERTISING_KEY = "174262";
    public static final String MAT_CONVERSION_KEY = "0275ecc3449ce2aed43bcfd5bd8cc6de";

    // HBMoney
    public static int HB_MONEY = 0;

    public static int HB_MONEY_USED_IN_TRANS = 0;

    //Selected food category
    public static String SELECTED_CATEGORY="";

    public static boolean PYMNT_MODE_HB_MONEY = false;
}