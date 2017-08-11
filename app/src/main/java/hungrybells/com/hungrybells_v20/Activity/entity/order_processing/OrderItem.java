package hungrybells.com.hungrybells_v20.Activity.entity.order_processing;

import com.google.gson.annotations.SerializedName;

import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;

/**
 * Created by ajeetkumar on 21/07/15.
 */
public class OrderItem extends IDataModel {

    @SerializedName("amount")
    private Double amount;

    @SerializedName("quantity")
    private Integer quantity;

    @SerializedName("deal_id")
    private Integer deal_id;


    private String deal_name;

    private String merchant_name;


    public Double getAmount ()
    {
        return amount;
    }

    public void setAmount (Double amount)
    {
        this.amount = amount;
    }

    public Integer getQuantity ()
    {
        return quantity;
    }

    public void setQuantity (Integer quantity)
    {
        this.quantity = quantity;
    }

    public Integer getDeal_id ()
    {
        return deal_id;
    }

    public void setDeal_id (Integer deal_id)
    {
        this.deal_id = deal_id;
    }

    public void setDeal_name(String name) { this.deal_name = name; }
    public String getDeal_name() { return this.deal_name; }

    public void setMerchant_name(String name) { this.merchant_name = name; }
    public String getMerchant_name() { return this.merchant_name; }

    @Override
    public String toString()
    {
        return "ClassPojo [amount = "+amount+", quantity = "+quantity+", deal_id = "+deal_id+"]";
    }
}
