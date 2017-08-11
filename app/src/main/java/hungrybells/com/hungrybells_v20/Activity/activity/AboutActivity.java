package hungrybells.com.hungrybells_v20.Activity.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.plus.PlusOneButton;


import butterknife.ButterKnife;
import butterknife.InjectView;
import hungrybells.com.hungrybells_v20.Activity.app.HungryBellsApplication;
import hungrybells.com.hungrybells_v20.Activity.utils.Constants;
import hungrybells.com.hungrybells_v20.BuildConfig;
import hungrybells.com.hungrybells_v20.R;

public class AboutActivity extends ActionBarActivity {

    PlusOneButton mPlusOneButton;

    // The request code must be 0 or greater.
    private static final int PLUS_ONE_REQUEST_CODE = 0;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @InjectView(R.id.app_version)
    TextView mAppVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ButterKnife.inject(this);
        setupToolbar();

        String versionName = BuildConfig.VERSION_NAME;

        if(Constants.DEBUG)
            mAppVersion.setText("Test version "+ versionName);
        else
            mAppVersion.setText("version "+ versionName);

        mPlusOneButton = (PlusOneButton) findViewById(R.id.plus_one_button);



        // Initialize Google Analytics
        try {

            Tracker t = ((HungryBellsApplication) getApplication()).getTracker(HungryBellsApplication.TrackerName.APP_TRACKER);
            t.setScreenName("About View");
            // Enable Advertising Features.
            t.send(new HitBuilders.AppViewBuilder().build());
        }catch(Exception  e)
        {
            //Toast.makeText(getApplicationContext(), "Error"+e.getMessage(), 1).show();
        }
    }

    private void setupToolbar() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back);
        getSupportActionBar().setTitle("About");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
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


    public void contactUsBtnClicked(View v){

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "contactus@getwise.in", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contact Us");
        startActivity(Intent.createChooser(emailIntent, "Contact Us"));

    }

    public void tosBtnClicked(View v){


        String url = "https://s3-ap-southeast-1.amazonaws.com/hungrybells.mobi/legal/TOS/HB_TOS_Web.htm";

        //Constants.FOOD_TAG_VARIABLE = foodTagTextBtn.getText().toString();

        //Go to Deal List Activity
        Intent intent = new Intent(AboutActivity.this, WebViewActivity_app.class);
        intent.putExtra("WEBVIEW", url);
        startActivity(intent);

    }

    public void privacyBtnClicked(View v){

        String url = "https://s3-ap-southeast-1.amazonaws.com/hungrybells.mobi/legal/PP/HB_PP_Web.htm";

        //Intent intent = new Intent(AboutActivity.this, WebViewActivity_app.class);
        //intent.putExtra("WEBVIEW", url);
        //startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Refresh the state of the +1 button each time the activity receives focus.
        mPlusOneButton.initialize("https://play.google.com/store/apps/details?id=com.HungryBells.activity", PLUS_ONE_REQUEST_CODE);
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }
}
