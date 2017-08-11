package hungrybells.com.hungrybells_v20.Activity.entity.food_tags_list;

import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;

/**
 * Created by ajeetkumar on 12/05/15.
 */
public class FavTagList extends IDataModel {

    private Favourites[] favourites;

    public Favourites[] getFavourites ()
    {
        return favourites;
    }

    public void setFavourites (Favourites[] favourites)
    {
        this.favourites = favourites;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [favourites = "+favourites+"]";
    }

}
