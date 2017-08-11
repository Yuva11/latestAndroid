package hungrybells.com.hungrybells_v20.Activity.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.localytics.android.Localytics;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hungrybells.com.hungrybells_v20.Activity.utils.Constants;
import hungrybells.com.hungrybells_v20.R;
import io.branch.referral.Branch;

public class ConformationActivity extends ActionBarActivity {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conformation);

        ButterKnife.inject(this);
        setupToolbar();

        // Localytics
        Localytics.tagEvent("Order Confirmation Viewed");


        if (Constants.PYMNT_MODE_HB_MONEY) {
            Branch.getInstance(getApplicationContext()).redeemRewards(Constants.HB_MONEY_USED_IN_TRANS);
        }


    }


    private void setupToolbar() {

        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back);
        getSupportActionBar().setTitle("Success");

    }


    @Override
    protected void onStart() {
        super.onStart();

        }

    @Override
    protected void onResume() {
        super.onResume();

        Localytics.openSession();
        Localytics.tagScreen("Order Confirmation");
        Localytics.upload();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_conformation, menu);
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
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(this, AppLaunchActivity.class);
        startActivity(intent);
        finish();
    }

    public void callBtnClicked(View view){

        String phone = "+918088002288";
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        startActivity(intent);
    }

    public void confirmBtnClicked(View v){

        Intent i = new Intent(ConformationActivity.this, AppLaunchActivity.class);
        i.putExtra("order_confirmation", true);
        startActivity(i);
        finish();

    }
}
