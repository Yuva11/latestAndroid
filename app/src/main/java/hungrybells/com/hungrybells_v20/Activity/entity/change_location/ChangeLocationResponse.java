package hungrybells.com.hungrybells_v20.Activity.entity.change_location;

import java.util.List;

import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;

/**
 * Created by ajeetkumar on 17/07/15.
 */
public class ChangeLocationResponse extends IDataModel {

    private String status;

    private List<Location> changeLocation;

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public List<Location> getChangeLocation ()
    {
        return changeLocation;
    }

    public void setChangeLocation (List<Location> changeLocation)
    {
        this.changeLocation = changeLocation;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [status = "+status+", changeLocation = "+changeLocation+"]";
    }
}
