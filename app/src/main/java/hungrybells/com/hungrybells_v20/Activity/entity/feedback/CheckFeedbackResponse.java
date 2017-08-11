package hungrybells.com.hungrybells_v20.Activity.entity.feedback;

import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;

/**
 * Created by dev on 15/09/15.
 */
public class CheckFeedbackResponse extends IDataModel {

    private String message;

    private String orderid;

    private String code;

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public String getOrderid ()
    {
        return orderid;
    }

    public void setOrderid (String orderid)
    {
        this.orderid = orderid;
    }

    public String getCode ()
    {
        return code;
    }

    public void setCode (String code)
    {
        this.code = code;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [message = "+message+", orderid = "+orderid+", code = "+code+"]";
    }
}
