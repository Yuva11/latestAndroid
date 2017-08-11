package hungrybells.com.hungrybells_v20.Activity.entity.dealList;

import com.google.gson.annotations.SerializedName;

import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;

/**
 * Created by ajeetkumar on 17/05/15.
 */
public class DealsListResponse extends IDataModel {

    @SerializedName("result")
    private Result result;

    @SerializedName("status")
    private String status;

    public Result getResult ()
    {
        return result;
    }

    public void setResult (Result result)
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
}
