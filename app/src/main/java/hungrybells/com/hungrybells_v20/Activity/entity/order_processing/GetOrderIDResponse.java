package hungrybells.com.hungrybells_v20.Activity.entity.order_processing;

import com.google.gson.annotations.SerializedName;

import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;

/**
 * Created by dev on 19/06/15.
 */
public class GetOrderIDResponse extends IDataModel {

    @SerializedName("message")
    private String message;

    @SerializedName("code")
    private String code;

    @SerializedName("orderid")
    private String orderid;

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public String getCode ()
    {
        return code;
    }

    public void setCode (String code)
    {
        this.code = code;
    }

    public String getOrderid() { return this.orderid; }

    public void setOrderid(String order_id) { this.orderid = order_id; }

    @Override
    public String toString()
    {
        return "ClassPojo [message = "+message+", code = "+code+"]";
    }


}
