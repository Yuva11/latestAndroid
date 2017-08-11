package hungrybells.com.hungrybells_v20.Activity.entity.nearest_distance;

import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;

/**
 * Created by dev on 16/09/15.
 */
public class Elements extends IDataModel {

    private Distance distance;

    private Duration duration;

    private String status;

    public Distance getDistance ()
    {
        return distance;
    }

    public void setDistance (Distance distance)
    {
        this.distance = distance;
    }

    public Duration getDuration ()
    {
        return duration;
    }

    public void setDuration (Duration duration)
    {
        this.duration = duration;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [distance = "+distance+", duration = "+duration+", status = "+status+"]";
    }
}
