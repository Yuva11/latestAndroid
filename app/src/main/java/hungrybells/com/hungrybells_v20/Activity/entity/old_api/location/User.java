package hungrybells.com.hungrybells_v20.Activity.entity.old_api.location;

import com.google.gson.annotations.SerializedName;

import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;

/**
 * Created by ajeetkumar on 21/05/15.
 */
public class User extends IDataModel {

    @SerializedName("deleteStatus")
    private String deleteStatus;

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("userName")
    private String userName;

    public String getDeleteStatus ()
    {
        return deleteStatus;
    }

    public void setDeleteStatus (String deleteStatus)
    {
        this.deleteStatus = deleteStatus;
    }

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

    public String getUserName ()
    {
        return userName;
    }

    public void setUserName (String userName)
    {
        this.userName = userName;
    }

}
