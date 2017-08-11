package hungrybells.com.hungrybells_v20.Activity.entity.food_tags_list;

import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;

/**
 * Created by ajeetkumar on 17/05/15.
 */
public class FoodTagListReqest extends IDataModel {

    private String longitude;

    private String user_id;

    private String latitude;

    private String device_id;

    private String email;

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

    public String getDevice_id() { return this.device_id; }

    public void setDevice_id(String deviceId) { this.device_id = deviceId; }

    public String getPrimary_email() { return this.email; }

    public void setPrimary_email(String email) { this.email = email; }
}
