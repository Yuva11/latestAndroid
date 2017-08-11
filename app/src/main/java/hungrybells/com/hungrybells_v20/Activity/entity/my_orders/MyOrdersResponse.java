package hungrybells.com.hungrybells_v20.Activity.entity.my_orders;

import java.util.List;

import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;

/**
 * Created by dev on 14/11/15.
 */
public class MyOrdersResponse extends IDataModel {

    private String status;

    private List<Myorderdetails> myorderdetails;

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public List<Myorderdetails> getMyorderdetails ()
    {
        return myorderdetails;
    }

    public void setMyorderdetails (List<Myorderdetails> myorderdetails)
    {
        this.myorderdetails = myorderdetails;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [status = "+status+", myorderdetails = "+myorderdetails+"]";
    }
}
