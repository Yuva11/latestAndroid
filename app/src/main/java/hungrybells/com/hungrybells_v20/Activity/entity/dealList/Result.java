package hungrybells.com.hungrybells_v20.Activity.entity.dealList;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;

/**
 * Created by ajeetkumar on 17/05/15.
 */
public class Result extends IDataModel {

    @SerializedName("deals")
    private List<Deals> deals;

    @SerializedName("total_food_items")
    private String total_food_items;

    public List<Deals> getDeals ()
    {
        return deals;
    }

    public void setDeals (List<Deals> deals)
    {
        this.deals = deals;
    }

    public String getTotal_food_items ()
    {
        return total_food_items;
    }

    public void setTotal_food_items (String total_food_items)
    {
        this.total_food_items = total_food_items;
    }

}
