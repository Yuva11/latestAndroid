package hungrybells.com.hungrybells_v20.Activity.entity.my_orders;

import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;

/**
 * Created by dev on 14/11/15.
 */
public class Orders extends IDataModel {

    private String amount;

    private String quantity;

    private String deal_id;

    public String getAmount ()
    {
        return amount;
    }

    public void setAmount (String amount)
    {
        this.amount = amount;
    }

    public String getQuantity ()
    {
        return quantity;
    }

    public void setQuantity (String quantity)
    {
        this.quantity = quantity;
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
        return "ClassPojo [amount = "+amount+", quantity = "+quantity+", deal_id = "+deal_id+"]";
    }
}
