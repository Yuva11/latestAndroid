package hungrybells.com.hungrybells_v20.Activity.entity.category;

import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;
import hungrybells.com.hungrybells_v20.Activity.entity.food_tags_list.CategoryList;

/**
 * Created by Bheem SIngh on 2/25/2016.
 */
public class CategoryListResponse extends IDataModel {

    private CategoryList[] categoryList;

    public CategoryList[] getCategoryList ()
    {
        return categoryList;
    }

    public void setCategoryList (CategoryList[] categoryList)
    {
        this.categoryList = categoryList;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [categoryList = "+categoryList+"]";
    }
}
