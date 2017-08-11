package hungrybells.com.hungrybells_v20.Activity.entity.my_orders;

import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;

/**
 * Created by dev on 14/11/15.
 */
public class Myorderdetails extends IDataModel {

    private Body body;

    public Body getBody ()
    {
        return body;
    }

    public void setBody (Body body)
    {
        this.body = body;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [body = "+body+"]";
    }
}
