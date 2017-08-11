package hungrybells.com.hungrybells_v20.Activity.entity.order_processing;

import com.google.gson.annotations.SerializedName;

import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;

/**
 * Created by dev on 19/06/15.
 */
public class GetOrderIDRequest extends IDataModel {


    @SerializedName("order_amount")
    private String order_amount;

    @SerializedName("landmark")
    private String landmark;

    @SerializedName("first_name")
    private String first_name;

    @SerializedName("order_quantity")
    private String order_quantity;

    @SerializedName("mobile_number")
    private String mobile_number;

    @SerializedName("address")
    private String address;

    @SerializedName("username_email")
    private String username_email;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("user_id")
    private String user_id;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("deal_id")
    private String deal_id;

    @SerializedName("order_type")
    private String order_type;

    public String getOrder_amount ()
    {
        return order_amount;
    }

    public void setOrder_amount (String order_amount)
    {
        this.order_amount = order_amount;
    }

    public String getLandmark ()
    {
        return landmark;
    }

    public void setLandmark (String landmark)
    {
        this.landmark = landmark;
    }

    public String getFirst_name ()
    {
        return first_name;
    }

    public void setFirst_name (String first_name)
    {
        this.first_name = first_name;
    }

    public String getOrder_quantity ()
    {
        return order_quantity;
    }

    public void setOrder_quantity (String order_quantity)
    {
        this.order_quantity = order_quantity;
    }

    public String getMobile_number ()
    {
        return mobile_number;
    }

    public void setMobile_number (String mobile_number)
    {
        this.mobile_number = mobile_number;
    }

    public String getAddress ()
    {
        return address;
    }

    public void setAddress (String address)
    {
        this.address = address;
    }

    public String getEmail ()
    {
        return username_email;
    }

    public void setUsername_email (String email)
    {
        this.username_email = email;
    }

    public String getLongitude ()
    {
        return longitude;
    }

    public void setLongitude (String longitude)
    {
        this.longitude = longitude;
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

    public String getDeal_id ()
    {
        return deal_id;
    }

    public void setDeal_id (String deal_id)
    {
        this.deal_id = deal_id;
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
        return "ClassPojo [order_amount = "+order_amount+", landmark = "+landmark+", first_name = "+first_name+", order_quantity = "+order_quantity+", mobile_number = "+mobile_number+", address = "+address+", email = "+username_email+", longitude = "+longitude+", user_id = "+user_id+", latitude = "+latitude+", deal_id = "+deal_id+", order_type = "+order_type+"]";
    }

}
