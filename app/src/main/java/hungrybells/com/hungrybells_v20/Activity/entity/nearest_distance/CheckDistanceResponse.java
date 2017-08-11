package hungrybells.com.hungrybells_v20.Activity.entity.nearest_distance;

import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;

/**
 * Created by dev on 16/09/15.
 */
public class CheckDistanceResponse extends IDataModel {

    private String message;

    private String hbMoneyMaxLimit;

    private String maximumDiscountValue;

    private String status;

    private String[] destination_addresses;


    private String customerAddressLandMark;

    private String[] origin_addresses;

    private String value;

    private String userName;

    private String userEmail;

    private String type;

    private String userMob;

    private String minimumOrderValue;

    private String free_delivery_minimum_order_value;

    private String deliveryCharge;

    private Rows[] rows;

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public String getHbMoneyMaxLimit ()
    {
        return hbMoneyMaxLimit;
    }

    public void setHbMoneyMaxLimit (String hbMoneyMaxLimit)
    {
        this.hbMoneyMaxLimit = hbMoneyMaxLimit;
    }

    public String getMaximumDiscountValue ()
    {
        return maximumDiscountValue;
    }

    public void setMaximumDiscountValue (String maximumDiscountValue)
    {
        this.maximumDiscountValue = maximumDiscountValue;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String[] getDestinationAddresses ()
    {
        return destination_addresses;
    }

    public void setDestination_addresses (String[] destination_addresses)
    {
        this.destination_addresses = destination_addresses;
    }

    public String[] getOriginAddresses ()
    {
        return origin_addresses;
    }

    public void setOriginAddresses (String[] origin_addresses)
    {
        this.origin_addresses = origin_addresses;
    }

    public String getValue ()
    {
        return value;
    }

    public void setValue (String value)
    {
        this.value = value;
    }

    public String getUserName ()
    {
        return userName;
    }

    public void setUserName (String userName)
    {
        this.userName = userName;
    }

    public String getUserEmail ()
    {
        return userEmail;
    }

    public void setUserEmail (String userEmail)
    {
        this.userEmail = userEmail;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    public String getUserMob ()
    {
        return userMob;
    }

    public void setUserMob (String userMob)
    {
        this.userMob = userMob;
    }

    public String getMinimumOrderValue ()
    {
        return minimumOrderValue;
    }

    public void setMinimumOrderValue (String minimumOrderValue)
    {
        this.minimumOrderValue = minimumOrderValue;
    }

    public String getMinimumOrderValueForFreeDelivery ()
    {
        return free_delivery_minimum_order_value;
    }

    public void setMinimumOrderValueForFreeDelivery (String free_delivery_minimum_order_value)
    {
        this.free_delivery_minimum_order_value = free_delivery_minimum_order_value;
    }

    public String getDeliveryCharge ()
    {
        return deliveryCharge;
    }

    public void setDeliveryCharge (String deliveryCharge)
    {
        this.deliveryCharge = deliveryCharge;
    }


    public String getCustomerAddressLandMark() {
        return customerAddressLandMark;
    }

    public void setCustomerAddressLandMark(String customerAddressLandMark) {
        this.customerAddressLandMark = customerAddressLandMark;
    }

    public Rows[] getRows ()
    {
        return rows;
    }

    public void setRows (Rows[] rows)
    {
        this.rows = rows;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [message = "+message+", hbMoneyMaxLimit = "+hbMoneyMaxLimit+", maximumDiscountValue = "+maximumDiscountValue+", status = "+status+", destination_addresses = "+destination_addresses+", origin_addresses = "+origin_addresses+", value = "+value+", userName = "+userName+", userEmail = "+userEmail+", type = "+type+", userMob = "+userMob+", minimumOrderValue = "+minimumOrderValue+", free_delivery_minimum_order_value = "+free_delivery_minimum_order_value+", deliveryCharge = "+deliveryCharge+", rows = "+rows+"]";
    }

}
