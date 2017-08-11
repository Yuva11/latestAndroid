package hungrybells.com.hungrybells_v20.Activity.entity.food_tags_list;

import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;

/**
 * Created by ajeetkumar on 12/05/15.
 */
public class TagsList extends IDataModel {
    private Results result;

    private String status;

    private String user_id;

    private String device_id;

    public Results getResult ()
    {
        return result;
    }

    public void setResult (Results result)
    {
        this.result = result;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String getUser_id ()
    {
        return user_id;
    }

    public void setUser_id (String user_id)
    {
        this.user_id = user_id;
    }

    public void setDevice_id(String deviceId) { this.device_id = deviceId; }

    public String getDevice_id() { return this.device_id; }

    @Override
    public String toString()
    {
        return "ClassPojo [result = "+result+", status = "+status+"]";
    }
}