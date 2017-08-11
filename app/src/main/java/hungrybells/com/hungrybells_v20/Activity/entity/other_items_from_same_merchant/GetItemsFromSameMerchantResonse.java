package hungrybells.com.hungrybells_v20.Activity.entity.other_items_from_same_merchant;

import com.google.gson.annotations.SerializedName;

import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;

/**
 * Created by ajeetkumar on 22/07/15.
 */
public class GetItemsFromSameMerchantResonse extends IDataModel {

    @SerializedName("result")
    private ListOfItemsFromMerchant result;

    @SerializedName("status")
    private String status;

    public ListOfItemsFromMerchant getResult ()
    {
        return result;
    }

    public void setResult (ListOfItemsFromMerchant result)
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

    @Override
    public String toString()
    {
        return "ClassPojo [result = "+result+", status = "+status+"]";
    }
}
