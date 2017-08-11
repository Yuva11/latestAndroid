package hungrybells.com.hungrybells_v20.Activity.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hungrybells.com.hungrybells_v20.Activity.adapters.MyOrdersListViewAdapter;
import hungrybells.com.hungrybells_v20.Activity.app.HBLog;
import hungrybells.com.hungrybells_v20.Activity.app.HBVolley;
import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;
import hungrybells.com.hungrybells_v20.Activity.entity.dealList.Deals;
import hungrybells.com.hungrybells_v20.Activity.entity.feedback.FeedbackResponse;
import hungrybells.com.hungrybells_v20.Activity.entity.my_orders.MyOrdersRequest;
import hungrybells.com.hungrybells_v20.Activity.entity.my_orders.MyOrdersResponse;
import hungrybells.com.hungrybells_v20.Activity.entity.my_orders.Myorderdetails;
import hungrybells.com.hungrybells_v20.Activity.net.HBGsonRequest;
import hungrybells.com.hungrybells_v20.Activity.utils.Constants;
import hungrybells.com.hungrybells_v20.R;

public class MyOrdersActivity extends ActionBarActivity implements Response.Listener<IDataModel>, Response.ErrorListener {


    private MyOrdersResponse myOrdersResponse;
    //region Event Item On click Listener
    AdapterView.OnItemClickListener ordersListItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            //Toast.makeText(getBaseContext(), "WOOO, its working", Toast.LENGTH_LONG).show();

            //Save clicked deal in Constants and use it later
            Myorderdetails myorderdetails = myOrdersResponse.getMyorderdetails().get(position);//mDealsListResponse.getResult().getDeals().get(position);

            if (myorderdetails != null) {
                // perform some action on touch of order item

            } else {
                Toast.makeText(getApplicationContext(), "Error in selection. Please try again.", Toast.LENGTH_SHORT).show();
            }


        }

    };

    @InjectView(R.id.my_orders_list_view)
    ListView myOrdersListView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        ButterKnife.inject(this);

        GetMyOrders();
    }

    //region API Response Handler

    @Override
    public void onResponse(IDataModel dataModel) {

        if (dataModel instanceof MyOrdersResponse) {
            myOrdersResponse = (MyOrdersResponse) dataModel;
            Log.d(MyOrdersActivity.class.getSimpleName().toString(), myOrdersResponse.toString());

            myOrdersListView.setAdapter(new MyOrdersListViewAdapter(this, R.layout.my_order_list_item, myOrdersResponse.getMyorderdetails()));
            myOrdersListView.setOnItemClickListener(ordersListItemClickListener);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    //endregion

    //region Helper Methods

    private void GetMyOrders() {

        MyOrdersRequest request = new MyOrdersRequest();
        request.setUser_id(Integer.parseInt(Constants.USER_ID));

        Gson gson = new Gson();
        String jsonStr = gson.toJson(request);
        HBLog.d(MyOrdersActivity.class.getSimpleName().toString(), jsonStr);

        HBVolley.getRequestQueue(getApplicationContext()).add(
                new HBGsonRequest(
                        Constants.POST_FETCH_MY_ORDERS_URL,
                        this,
                        this,
                        new MyOrdersResponse(),
                        null,
                        null,
                        jsonStr,
                        Request.Method.POST
                )).setShouldCache(true);
    }

    //endregion

}
