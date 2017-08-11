package hungrybells.com.hungrybells_v20.Activity.entity.category;

import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;

/**
* Created by Ramzi on 18/02/2016
*/
public class CategoryListRequest extends IDataModel {
    private String userId;

    private String longitude;

    private String latitude;

    public String getUserId ()
    {
        return userId;
    }

    public void setUserId (String userId)
    {
        this.userId = userId;
    }

    public String getLongitude ()
    {
        return longitude;
    }

    public void setLongitude (String longitude)
    {
        this.longitude = longitude;
    }

    public String getLatitude ()
    {
        return latitude;
    }

    public void setLatitude (String latitude)
    {
        this.latitude = latitude;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [userId = "+userId+", longitude = "+longitude+", latitude = "+latitude+"]";
    }

}
