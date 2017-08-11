package hungrybells.com.hungrybells_v20.Activity.entity.my_orders;

import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;

/**
 * Created by dev on 14/11/15.
 */
public class MyOrdersRequest extends IDataModel {

    private int user_id;

    public int getUser_id ()
    {
        return user_id;
    }

    public void setUser_id (int user_id)
    {
        this.user_id = user_id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [user_id = "+user_id+"]";
    }
}
