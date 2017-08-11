package hungrybells.com.hungrybells_v20.Activity.entity.dealList;

import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;

/**
 * Created by ramzi on 2/3/16.
 */
public class CategoryDealsRequest extends IDataModel{

    private String category_name;

    private String userId;

    private String longitude;

    private String latitude;

    public String getCategory_name ()
    {
        return category_name;
    }

    public void setCategory_name (String category_name)
    {
        this.category_name = category_name;
    }

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
        return "ClassPojo [category_name = "+category_name+", userId = "+userId+", longitude = "+longitude+", latitude = "+latitude+"]";
    }
}
