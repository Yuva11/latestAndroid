package hungrybells.com.hungrybells_v20.Activity.entity.food_tags_list;

import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;

/**
 * Created by ramzi on 12/12/2015.
 */
public class Favourites   extends IDataModel {
    private String tag_name;

    private String deal_id;

    public String getTag_name ()
    {
        return tag_name;
    }

    public void setTag_name (String tag_name)
    {
        this.tag_name = tag_name;
    }

    public String getDeal_id ()
    {
        return deal_id;
    }

    public void setDeal_id (String deal_id)
    {
        this.deal_id = deal_id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [tag_name = "+tag_name+", deal_id = "+deal_id+"]";
    }
}
