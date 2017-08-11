package hungrybells.com.hungrybells_v20.Activity.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hungrybells.com.hungrybells_v20.Activity.utils.Constants;
import hungrybells.com.hungrybells_v20.R;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;

public class ReferralActivity extends AppCompatActivity {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    TextView mToolbarTitle;

    TextView mHBMoneyTextView;

    Button mFBShareButton;
    Button mWhatsappShareButton;
    Button mEmailShareButton;
    Button mOtherShareButton;

    String urlStr = "";
    private String shareTextStr = "";
    private String creditStr = "";

    String android_url = "http://49apps.net/apk/hb2.2.2.4.apk";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral);

        ButterKnife.inject(this);
        setupToolbar();

        setupShareButtons();

        mHBMoneyTextView = (TextView) findViewById(R.id.hb_money_textview);

        shareTextStr = "Hey,check out Hungry Bells food Ordering App. Download now and earn 10 points worth of credits. Make your first order using coupon - MYFIRSTORDER (T&C applied).";

        try {


            getHungryBellsMoney();

            Branch.getInstance().loadRewards(new Branch.BranchReferralStateChangedListener() {
                @Override
                public void onStateChanged(boolean b, BranchError branchError) {
                    int credits = Branch.getInstance().getCredits();


                    updateHungryBellsMoney(credits);

                }
            });
        } catch (Exception e) {



        }


    }


    private void setupToolbar() {


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back);
        getSupportActionBar().setTitle("");

        mToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        mToolbarTitle.setText("Refer & Earn");

    }


    private void setupShareButtons() {

        mFBShareButton = (Button) findViewById(R.id.fb_share_btn);
        mWhatsappShareButton = (Button) findViewById(R.id.whatsapp_share_btn);
        mEmailShareButton = (Button) findViewById(R.id.email_share_btn);
        mOtherShareButton = (Button) findViewById(R.id.others_share_btn);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_referral, menu);
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

    public void whatsappShareClicked(View v) {


        JSONObject obj = new JSONObject();

        /*
        try {


            obj.put("$android_url", android_url);

        } catch (JSONException e) {
        }
        */


        Branch.getInstance().getShortUrl(obj, new Branch.BranchLinkCreateListener() {
            @Override
            public void onLinkCreate(String url, BranchError error) {

                String shareString = shareTextStr + " Link- " + url;

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareString);
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.whatsapp");
                startActivity(sendIntent);
            }
        });


    }


    public void facebookShareClicked(View v) {


        JSONObject obj = new JSONObject();

        /*
        try {


            obj.put("$android_url", android_url);

        } catch (JSONException e) {
        }
        */


        Branch.getInstance().getShortUrl(obj, new Branch.BranchLinkCreateListener() {
            @Override
            public void onLinkCreate(String url, BranchError error) {
                //Log.i(TAG, "Ready to share my link = " + url);

                String shareString = shareTextStr + url;


                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(url))
                        .setContentTitle("Hungry Bells")
                        .setContentDescription(shareTextStr)
                        .build();

                ShareDialog.show(ReferralActivity.this, content);


                /*
                ShareDialog shareDialog = new ShareDialog(ReferralActivity.this);;

                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentTitle("Hello Facebook")
                            .setContentDescription(
                                    "The 'Hello Facebook' sample  showcases simple Facebook integration")
                            .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
                            .build();

                    shareDialog.show(linkContent);
                }
                */


            }
        });


    }

    public void emailShareClicked(View v) {


        JSONObject obj = new JSONObject();

        /*
        try {


            obj.put("$android_url", android_url);

        } catch (JSONException e) {
        }
        */


        Branch.getInstance().getShortUrl(obj, new Branch.BranchLinkCreateListener() {
            @Override
            public void onLinkCreate(String url, BranchError error) {
                //Log.i(TAG, "Ready to share my link = " + url);

                String shareString = shareTextStr + " Link- " + url;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setType("plain/text");
                i.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                i.putExtra(Intent.EXTRA_SUBJECT, "Hungry Bells app");
                i.putExtra(Intent.EXTRA_TEXT, shareString);
                checkIfAppExists(i, "Gmail");
            }
        });


    }

    public void othersShareClicked(View v) {


        JSONObject obj = new JSONObject();
        /*
        try {


            obj.put("$android_url", android_url);

        } catch (JSONException e) {
        }
        */


        Branch.getInstance().getShortUrl(obj, new Branch.BranchLinkCreateListener() {
            @Override
            public void onLinkCreate(String url, BranchError error) {
                //Log.i(TAG, "Ready to share my link = " + url);

                String shareString = shareTextStr + url;
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareString);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share via"));
            }
        });

    }


    // method to check whether an app exists or not
    public void checkIfAppExists(Intent appIntent, String appName) {

        if (appIntent.resolveActivity(getApplicationContext().getPackageManager()) != null) {

            // start the activity if the app exists in the system
            startActivity(appIntent);

        } else {

            // tell the user the app does not exist
            Toast.makeText(getApplicationContext(), appName + " app does not exist!", Toast.LENGTH_LONG).show();
        }
    }


    private void updateHungryBellsMoney(int money) {

        Constants.HB_MONEY = money;

        ((LinearLayout) findViewById(R.id.money_balance_layout)).setVisibility(View.VISIBLE);
        mHBMoneyTextView.setText(money+" Point(s)");

        //save first launch as false now
        SharedPreferences sharedPref1 = ReferralActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref1.edit();
        editor.putString(getString(R.string.hb_money), money + "");
        editor.commit();

    }

    private void getHungryBellsMoney() {

        SharedPreferences sharedPref = ReferralActivity.this.getPreferences(Context.MODE_PRIVATE);
        String moneyStr = sharedPref.getString(getString(R.string.hb_money), "0");

        ((LinearLayout) findViewById(R.id.money_balance_layout)).setVisibility(View.VISIBLE);
        mHBMoneyTextView.setText(moneyStr+" Point(s)");


    }

}
