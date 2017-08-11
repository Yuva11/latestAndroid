package hungrybells.com.hungrybells_v20.Activity.entity.old_api.location;

import com.google.gson.annotations.SerializedName;

import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;

/**
 * Created by ajeetkumar on 21/05/15.
 */
public class NearestLocation extends IDataModel {

    @SerializedName("id")
    private String id;

    @SerializedName("lastUpdate")
    private String lastUpdate;

    @SerializedName("name")
    private String name;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("deletestatus")
    private String deletestatus;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("user")
    private User user;

    @SerializedName("createdDate")
    private String createdDate;

    @SerializedName("city")
    private City city;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getLastUpdate ()
    {
        return lastUpdate;
    }

    public void setLastUpdate (String lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getLongitude ()
    {
        return longitude;
    }

    public void setLongitude (String longitude)
    {
        this.longitude = longitude;
    }

    public String getDeletestatus ()
    {
        return deletestatus;
    }

    public void setDeletestatus (String deletestatus)
    {
        this.deletestatus = deletestatus;
    }

    public String getLatitude ()
    {
        return latitude;
    }

    public void setLatitude (String latitude)
    {
        this.latitude = latitude;
    }

    public User getUser ()
    {
        return user;
    }

    public void setUser (User user)
    {
        this.user = user;
    }

    public String getCreatedDate ()
    {
        return createdDate;
    }

    public void setCreatedDate (String createdDate)
    {
        this.createdDate = createdDate;
    }

    public City getCity ()
    {
        return city;
    }

    public void setCity (City city)
    {
        this.city = city;
    }

}
