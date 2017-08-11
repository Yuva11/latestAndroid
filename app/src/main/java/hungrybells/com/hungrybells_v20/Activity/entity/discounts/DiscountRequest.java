package hungrybells.com.hungrybells_v20.Activity.entity.discounts;

import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;

/**
 * Created by ajeetkumar on 23/07/15.
 */
public class DiscountRequest extends IDataModel {

    private String coupanCode;

    private String merchantbranch_id;

    private String user_id;

    private String total_order_value;

    public String getCoupanCode() {
        return coupanCode;
    }

    public void setCoupanCode(String coupanCode) {
        this.coupanCode = coupanCode;
    }

    public String getMerchantbranch_id() {
        return merchantbranch_id;
    }

    public void setMerchantbranch_id(String merchantbranch_id) {
        this.merchantbranch_id = merchantbranch_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTotal_order_value() {
        return total_order_value;
    }

    public void setTotal_order_value(String total_order_value) {
        this.total_order_value = total_order_value;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [coupanCode = "+coupanCode+"]";
    }
}
