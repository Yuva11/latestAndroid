package hungrybells.com.hungrybells_v20.Activity.entity.feedback;

import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;

/**
 * Created by dev on 15/09/15.
 */
public class CheckFeedbackRequest extends IDataModel {

    private String user_id;

    public String getUser_id ()
    {
        return user_id;
    }

    public void setUser_id (String user_id)
    {
        this.user_id = user_id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [user_id = "+user_id+"]";
    }
}
