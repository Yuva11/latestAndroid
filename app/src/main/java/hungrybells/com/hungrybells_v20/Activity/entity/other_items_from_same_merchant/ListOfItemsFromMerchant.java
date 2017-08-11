package hungrybells.com.hungrybells_v20.Activity.entity.other_items_from_same_merchant;

import java.util.List;

import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;
import hungrybells.com.hungrybells_v20.Activity.entity.dealList.Deals;

/**
 * Created by ajeetkumar on 22/07/15.
 */
public class ListOfItemsFromMerchant extends IDataModel {

    private List<Deals> deals;

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

    @Override
    public String toString()
    {
        return "ClassPojo [deals = "+deals+", total_food_items = "+total_food_items+"]";
    }
}
