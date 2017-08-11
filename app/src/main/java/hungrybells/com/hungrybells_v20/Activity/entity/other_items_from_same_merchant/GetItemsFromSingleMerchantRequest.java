package hungrybells.com.hungrybells_v20.Activity.entity.other_items_from_same_merchant;

import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;

/**
 * Created by ajeetkumar on 22/07/15.
 */
public class GetItemsFromSingleMerchantRequest extends IDataModel {

    private String longitude;

    private String user_id;

    private String latitude;

    private Integer merchantbranch_id;

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

    public Integer getMerchantbranch_id ()
    {
        return merchantbranch_id;
    }

    public void setMerchantbranch_id (Integer merchantbranch_id)
    {
        this.merchantbranch_id = merchantbranch_id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [longitude = "+longitude+", user_id = "+user_id+", latitude = "+latitude+", merchantbranch_id = "+merchantbranch_id+"]";
    }
}
