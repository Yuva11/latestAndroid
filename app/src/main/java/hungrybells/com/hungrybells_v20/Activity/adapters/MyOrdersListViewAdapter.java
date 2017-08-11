package hungrybells.com.hungrybells_v20.Activity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import hungrybells.com.hungrybells_v20.Activity.entity.my_orders.Myorderdetails;
import hungrybells.com.hungrybells_v20.Activity.entity.my_orders.Orders;
import hungrybells.com.hungrybells_v20.R;

/**
 * Created by dev on 14/11/15.
 */
public class MyOrdersListViewAdapter extends ArrayAdapter<Myorderdetails> {


    private LayoutInflater mInflater;
    private List<Myorderdetails> ordersList;
    private Context mContext;
    private int resourceId;

    public MyOrdersListViewAdapter(Context context, int resource, List<Myorderdetails> ordersList) {
        super(context, resource, ordersList);
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.ordersList = ordersList;
        this.resourceId = resource;
    }

    @Override
    public int getCount() {
        return ordersList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null) {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) convertView.getTag();
        } else {

            convertView = mInflater.inflate(this.resourceId, null);

            // Creates a ViewHolder and store references to the two children
            // views
            // we want to bind data to.
            holder = new ViewHolder();
            holder.orderTitle = (TextView) convertView.findViewById(R.id.order_item_title);
            holder.orderDate = (TextView)convertView.findViewById(R.id.order_date);
            holder.orderTotalPrice = (TextView)convertView.findViewById(R.id.order_price);
            holder.orderedItems=(TextView)convertView.findViewById(R.id.ordered_items);
        }

        Myorderdetails order = ordersList.get(position);
        holder.orderTitle.setText(order.getBody().getOrder_type());
        holder.orderTotalPrice.setText(order.getBody().getOrder_amount());





        return convertView;
    }

    // This class would contain all the ui elements which are there in the list item xml which would needs reseting
    class ViewHolder {
        TextView orderTitle;
        TextView orderDate;
        TextView orderTotalPrice;
        TextView orderedItems;
    }
}
