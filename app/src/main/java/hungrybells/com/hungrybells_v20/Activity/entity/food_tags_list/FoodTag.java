package hungrybells.com.hungrybells_v20.Activity.entity.food_tags_list;

import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;

/**
 * Created by ajeetkumar on 12/05/15.
 */
public class FoodTag extends IDataModel {

    private String tag_name;

    private String tag_id;

    public String getTag_name ()
    {
        return tag_name;
    }

    public void setTag_name (String tag_name)
    {
        this.tag_name = tag_name;
    }

    public String getTag_id ()
    {
        return tag_id;
    }

    public void setTag_id (String tag_id)
    {
        this.tag_id = tag_id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [tag_name = "+tag_name+", tag_id = "+tag_id+"]";
    }

}
