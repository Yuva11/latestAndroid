package hungrybells.com.hungrybells_v20.Activity.entity.old_api.location;

import com.google.gson.annotations.SerializedName;

import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;

/**
 * Created by ajeetkumar on 21/05/15.
 */
public class City extends IDataModel {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("deletestatus")
    private String deletestatus;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getDeletestatus ()
    {
        return deletestatus;
    }

    public void setDeletestatus (String deletestatus)
    {
        this.deletestatus = deletestatus;
    }
}
