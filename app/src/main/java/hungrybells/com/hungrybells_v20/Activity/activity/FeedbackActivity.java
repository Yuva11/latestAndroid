package hungrybells.com.hungrybells_v20.Activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.apache.http.entity.StringEntity;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hungrybells.com.hungrybells_v20.Activity.app.HBLog;
import hungrybells.com.hungrybells_v20.Activity.app.HBVolley;
import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;
import hungrybells.com.hungrybells_v20.Activity.entity.feedback.FeedbackRequest;
import hungrybells.com.hungrybells_v20.Activity.entity.feedback.FeedbackResponse;
import hungrybells.com.hungrybells_v20.Activity.net.HBGsonRequest;
import hungrybells.com.hungrybells_v20.Activity.utils.Constants;
import hungrybells.com.hungrybells_v20.R;

public class FeedbackActivity extends ActionBarActivity implements
        Response.Listener<IDataModel>, Response.ErrorListener {

    private String orderId;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @InjectView(R.id.feedback_edit_text)TextView mFeedbackEditText;

    private RatingBar tasteRatingBar;
    private RatingBar serviceRatingBar;
    private RatingBar packingRatingBar;
    private RatingBar deliveryRatingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        ButterKnife.inject(this);
        setupToolbar();

        tasteRatingBar = (RatingBar)(findViewById(R.id.tasteRatingBar));
        serviceRatingBar= (RatingBar)(findViewById(R.id.serviceRatingBar));
        packingRatingBar = (RatingBar)(findViewById(R.id.packingratingBar));
        deliveryRatingBar = (RatingBar)(findViewById(R.id.deliveryRatingBar));

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Constants.ORDER_ID)) {
            orderId = intent.getStringExtra(Constants.ORDER_ID);
        }


        mFeedbackEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mFeedbackEditText.setHint("");
            }
        });
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back);
        getSupportActionBar().setTitle("Feedback");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_feedback, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResponse(IDataModel dataModel) {
        if (dataModel instanceof FeedbackResponse) {
            FeedbackResponse feedbackResponse = (FeedbackResponse) dataModel;
            HBLog.i("Info", feedbackResponse.toString());
            if (feedbackResponse.getCode().equals("1")) {
                // It means the feedback was submitted successfully
                finish();
            } else {
                // Show user a toast that feedback was not submitted successfully on the server
                Toast.makeText(getApplicationContext(), "Feedback not submitted successfully", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
    }

    /*
    * Check the feedback user entered is not null
    * Then it send the feedback to the HB server
    *
    * */
    public void feedbackSubmit(View v) {

        EditText feedback = (EditText) findViewById(R.id.feedback_edit_text);
        FeedbackRequest feedbackRequest = new FeedbackRequest();
        String feedbackText = feedback.getText().toString().trim();
        if (feedbackText.isEmpty() || feedbackText.length() < 3) {
            // Show an erro message to user informing that edit text cannot be empty
            Toast.makeText(this, "Feedback should be more than 3 charracters long", Toast.LENGTH_LONG).show();
            return;
        }

        if (tasteRatingBar.getRating() == 0.0 || serviceRatingBar.getRating() == 0.0 || packingRatingBar.getRating() == 0.0 || deliveryRatingBar.getRating() == 0.0 ){
            Toast.makeText(this, "Please rate your experience for us to improve our service", Toast.LENGTH_LONG).show();
            return;
        }

        feedbackRequest.setOrder_id(orderId);

        feedbackRequest.setRating1(tasteRatingBar.getRating() + "");
        feedbackRequest.setRating2(serviceRatingBar.getRating()+"");
        feedbackRequest.setRating3(packingRatingBar.getRating()+"");
        feedbackRequest.setRating4(deliveryRatingBar.getRating()+"");

        feedbackRequest.setUser_id(Constants.USER_ID);
        //feedbackRequest.setTimestamp((new Date()).toString());
        feedbackRequest.setFeedback(feedbackText);


        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String dateStr = format.format(new Date());
        feedbackRequest.setTimestamp(dateStr);


        Gson gson = new Gson();
        String customerData = gson.toJson(feedbackRequest);

        String url = Constants.FEEDBACK_URL;// + Constants.USER_ID;

        HBVolley.getRequestQueue(getApplicationContext()).add(
                new HBGsonRequest(
                        Constants.POST_FEEDBACK_TO_SERVER_URL,
                        this,
                        this,
                        new FeedbackResponse(),
                        null,
                        null,
                        customerData,
                        Request.Method.POST
                )).setShouldCache(true);

    }
}
