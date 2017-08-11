package hungrybells.com.hungrybells_v20.Activity.entity.feedback;

import java.util.Date;

import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;

/**
 * Created by ajeetkumar on 27/05/15.
 */
public class FeedbackRequest extends IDataModel {

    private String timestamp;

    private String feedback;

    private String user_id;

    private String rating1,rating2,rating3,rating4,rating5;

    private String order_id;

    public String getTimestamp ()
    {
        return timestamp;
    }

    public void setTimestamp (String timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getFeedback ()
    {
        return feedback;
    }

    public String getRating2() {
        return rating2;
    }

    public void setRating2(String rating2) {
        this.rating2 = rating2;
    }

    public String getRating3() {
        return rating3;
    }

    public void setRating3(String rating3) {
        this.rating3 = rating3;
    }

    public String getRating4() {
        return rating4;
    }

    public void setRating4(String rating4) {
        this.rating4 = rating4;
    }

    public String getRating5() {
        return rating5;
    }

    public void setRating5(String rating5) {
        this.rating5 = rating5;
    }

    public String getRating1() {
        return rating1;
    }

    public void setRating1(String rating1) {
        this.rating1 = rating1;
    }

    public void setFeedback (String feedback)
    {
        this.feedback = feedback;
    }

    public String getUser_id ()
    {
        return user_id;
    }


    public void setUser_id (String user_id)
    {
        this.user_id = user_id;
    }

    public String getOrder_id ()
    {
        return order_id;
    }

    public void setOrder_id (String order_id)
    {
        this.order_id = order_id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [timestamp = "+timestamp+", feedback = "+feedback+", user_id = "+user_id+", rating = { rating1 = "+rating1+", rating2 = "+rating2+", rating3 = "+rating3+", rating4 = "+rating4+", order_id = "+order_id+"]";
    }
}
