package hungrybells.com.hungrybells_v20.Activity.entity.dealList;

import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;

/**
 * Created by ajeetkumar on 28/05/15.
 */
public class DealsActionsLogRequest extends IDataModel {

    private String userId;

    private String dealId;

    public String getUserId ()
    {
        return userId;
    }

    public void setUserId (String userId)
    {
        this.userId = userId;
    }

    public String getDealId ()
    {
        return dealId;
    }

    public void setDealId (String dealId)
    {
        this.dealId = dealId;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [userId = "+userId+", dealId = "+dealId+"]";
    }

}
