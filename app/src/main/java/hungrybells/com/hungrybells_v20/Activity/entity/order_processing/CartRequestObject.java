package hungrybells.com.hungrybells_v20.Activity.entity.order_processing;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;

/**
 * Created by ajeetkumar on 21/07/15.
 */
public class CartRequestObject extends IDataModel {

    @SerializedName("discount_amount")
    private String discount_amount;

    @SerializedName("first_name")
    private String first_name;

    @SerializedName("landmark")
    private String landmark;

    @SerializedName("order_amount")
    private String order_amount;

    @SerializedName("mobile_number")
    private String mobile_number;

    @SerializedName("discount_method")
    private String discount_method;

    @SerializedName("email")
    private String email;

    @SerializedName("address")
    private String address;

    @SerializedName("orders")
    private List<OrderItem> orders;

    @SerializedName("user_id")
    private String user_id;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("order_type")
    private String order_type;

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    @SerializedName("coupon_code")

    private String coupon_code;

    @SerializedName("delivery_date")
    private String deliveryDate;

    @SerializedName("delivery_time")
    private String deliveryTime;




    public String getDiscount_amount ()
    {
        return discount_amount;
    }

    public void setDiscount_amount (String discount_amount)
    {
        this.discount_amount = discount_amount;
    }

    public String getFirst_name ()
    {
        return first_name;
    }

    public void setFirst_name (String first_name)
    {
        this.first_name = first_name;
    }

    public String getLandmark ()
    {
        return landmark;
    }

    public void setLandmark (String landmark)
    {
        this.landmark = landmark;
    }

    public String getOrder_amount ()
    {
        return order_amount;
    }

    public void setOrder_amount (String order_amount)
    {
        this.order_amount = order_amount;
    }

    public String getMobile_number ()
    {
        return mobile_number;
    }

    public void setMobile_number (String mobile_number)
    {
        this.mobile_number = mobile_number;
    }

    public String getDiscount_method ()
    {
        return discount_method;
    }

    public void setDiscount_method (String discount_method)
    {
        this.discount_method = discount_method;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getAddress ()
    {
        return address;
    }

    public void setAddress (String address)
    {
        this.address = address;
    }

    public List<OrderItem> getOrders ()
    {
        return orders;
    }

    public void setOrders (List<OrderItem> orders)
    {
        this.orders = orders;
    }

    public String getUser_id ()
    {
        return user_id;
    }

    public void setUser_id (String user_id)
    {
        this.user_id = user_id;
    }

    public String getLongitude ()
    {
        return longitude;
    }

    public void setLongitude (String longitude)
    {
        this.longitude = longitude;
    }

    public String getLatitude ()
    {
        return latitude;
    }

    public void setLatitude (String latitude)
    {
        this.latitude = latitude;
    }

    public String getOrder_type ()
    {
        return order_type;
    }

    public void setOrder_type (String order_type)
    {
        this.order_type = order_type;
    }

    public String getCoupon_code() { return this.coupon_code; }

    public void setCoupon_code(String code) { this.coupon_code = code; }

    @Override
    public String toString()
    {
        return "ClassPojo [discount_amount = "+discount_amount+", first_name = "+first_name+", landmark = "+landmark+", order_amount = "+order_amount+", mobile_number = "+mobile_number+", discount_method = "+discount_method+", email = "+email+", address = "+address+", orders = "+orders+", user_id = "+user_id+", longitude = "+longitude+", latitude = "+latitude+", order_type = "+order_type+"]";
    }
}
