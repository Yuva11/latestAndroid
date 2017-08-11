package hungrybells.com.hungrybells_v20.Activity.entity.food_tags_list;

import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;

/**
 * Created by ajeetkumar on 12/05/15.
 */
public class Results extends IDataModel{

    private FoodTag[] favourites;

    private FoodTag[] trending;

    private FoodTag[] recomended;

    private String location;

    private String total_food_items;

    public FoodTag[] getFavourites ()
    {
        return favourites;
    }

    public void setFavourites (FoodTag[] favourites)
    {
        this.favourites = favourites;
    }

    public FoodTag[] getTrending ()
    {
        return trending;
    }

    public void setTrending (FoodTag[] trending)
    {
        this.trending = trending;
    }

    public FoodTag[] getRecomended ()
    {
        return recomended;
    }

    public void setRecomended (FoodTag[] recomended)
    {
        this.recomended = recomended;
    }

    public String getLocation ()
    {
        return location;
    }

    public void setLocation (String location)
    {
        this.location = location;
    }

    public String getTotal_food_items ()
    {
        return total_food_items;
    }

    public void setTotal_food_items (String total_food_items) {
        this.total_food_items = total_food_items;
    }

    @Override
    public String toString() {
        return "ClassPojo [favourites = "+favourites+", trending = "+trending+", recomended = "+recomended+", location = "+location+", total_food_items = "+total_food_items+"]";
    }
}
