package hungrybells.com.hungrybells_v20.Activity.entity.my_orders;

import java.util.List;

import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;

/**
 * Created by dev on 14/11/15.
 */
public class Body extends IDataModel {

    private String coupon_code;

    private String discount_amount;

    private String first_name;

    private String landmark;

    private String order_amount;

    private String delivery_status;

    private String mobile_number;

    private String discount_method;

    private String email;

    private String address;

    private String longitude;

    private List<Orders> orders;

    private String user_id;

    private String latitude;

    private String order_type;

    public String getCoupon_code ()
    {
        return coupon_code;
    }

    public void setCoupon_code (String coupon_code)
    {
        this.coupon_code = coupon_code;
    }

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

    public String getDelivery_status ()
    {
        return delivery_status;
    }

    public void setDelivery_status (String delivery_status)
    {
        this.delivery_status = delivery_status;
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

    public String getLongitude ()
    {
        return longitude;
    }

    public void setLongitude (String longitude)
    {
        this.longitude = longitude;
    }

    public List<Orders> getOrders ()
    {
        return orders;
    }

    public void setOrders (List<Orders> orders)
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

    @Override
    public String toString()
    {
        return "ClassPojo [coupon_code = "+coupon_code+", discount_amount = "+discount_amount+", first_name = "+first_name+", landmark = "+landmark+", order_amount = "+order_amount+", delivery_status = "+delivery_status+", mobile_number = "+mobile_number+", discount_method = "+discount_method+", email = "+email+", address = "+address+", longitude = "+longitude+", orders = "+orders+", user_id = "+user_id+", latitude = "+latitude+", order_type = "+order_type+"]";
    }
}
