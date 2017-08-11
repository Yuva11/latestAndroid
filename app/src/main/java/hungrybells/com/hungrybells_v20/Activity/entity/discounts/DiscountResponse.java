package hungrybells.com.hungrybells_v20.Activity.entity.discounts;

import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;

/**
 * Created by ajeetkumar on 23/07/15.
 */
public class DiscountResponse extends IDataModel {

    private String end_date;

    private String percentage;

    private String status;

    private String coupon_code;

    private String max_value;

    private String start_date;

    private String city;

    private String error;

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCoupon_code() {
        return coupon_code;
    }

    public void setCoupon_code(String coupon_code) {
        this.coupon_code = coupon_code;
    }

    public String getMax_value() {
        return max_value;
    }

    public void setMax_value(String max_value) {
        this.max_value = max_value;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [end_date = "+end_date+", percentage = "+percentage+", status = "+status+", coupon_code = "+coupon_code+", max_value = "+max_value+", start_date = "+start_date+", city = "+city+"]";
    }
}
