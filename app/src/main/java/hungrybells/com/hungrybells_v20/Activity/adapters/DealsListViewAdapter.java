package hungrybells.com.hungrybells_v20.Activity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.text.DecimalFormat;
import java.util.List;

import hungrybells.com.hungrybells_v20.Activity.app.HBLog;
import hungrybells.com.hungrybells_v20.Activity.app.HBVolley;
import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;
import hungrybells.com.hungrybells_v20.Activity.entity.dealList.Deals;
import hungrybells.com.hungrybells_v20.Activity.utils.Constants;
import hungrybells.com.hungrybells_v20.R;

/**
 * Created by riteshranjan on 29/05/15.
 */
public class DealsListViewAdapter extends ArrayAdapter<Deals> {

    private LayoutInflater mInflater;
    private List<Deals> dealsList;
    private Context mContext;
    private int resourceId;

    public DealsListViewAdapter(Context context, int resource, List<Deals> dealsList) {
        super(context, resource, dealsList);
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.dealsList = dealsList;
        this.resourceId = resource;
    }

    @Override
    public int getCount() {
        return dealsList.size();
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
            //convertView = mInflater.inflate(R.id.de, null);

            // Creates a ViewHolder and store references to the two children
            // views
            // we want to bind data to.
            holder = new ViewHolder();

            holder.mDealName = (TextView) convertView.findViewById(R.id.deal_name);
            holder.mDealPrice = (TextView) convertView.findViewById(R.id.deal_price);
            holder.mDealMerchantAndDistance = (TextView) convertView.findViewById(R.id.deal_merchant_and_distance);

            holder.mDealImage = (NetworkImageView) convertView.findViewById(R.id.deal_image);

            holder.mDealLikeCount = (TextView) convertView.findViewById(R.id.deal_like_count);
            holder.mDealShareCount = (TextView) convertView.findViewById(R.id.deal_share_count);
            holder.mDealViewCount = (TextView) convertView.findViewById(R.id.deal_view_count);

            convertView.setTag(holder);
        }

        Deals deals = dealsList.get(position);
        holder.mDealName.setText(deals.getName());

        //process deal price
        HBLog.d("dealListAdapter", deals.toString());
        String priceInInt = String.valueOf((int) Float.parseFloat(deals != null && deals.getDealPrice() != null ? deals.getDealPrice() : "0"));
        holder.mDealPrice.setText("Rs " + priceInInt);






        //Set like, view & share count
        holder.mDealViewCount.setText(deals.getDealview_count().toString());
        holder.mDealLikeCount.setText(deals.getDeallike_count().toString());
        holder.mDealShareCount.setText(deals.getDealshare_count().toString());

        // load deal image
        makeImageRequest(holder.mDealImage, deals.getImageUrl());



        // set distance to restarunt

        String distanceString = "";

        if (Constants.DEVICE_LAT.equals("") || Constants.DEVICE_LONG.equals("")) {

        } else {
            double lat1 = Double.parseDouble(Constants.DEVICE_LAT);
            double lng1 = Double.parseDouble(Constants.DEVICE_LONG);
            double lat2 = Double.parseDouble(deals.getMerchantLatitude());
            double lng2 = Double.parseDouble(deals.getMerchantLongitude());
            double distanceInKm = distanceLatLong2(lat1, lng1, lat2, lng2);


            if (distanceInKm < 1.0)
            {
                distanceString = "approx "+ (int)(distanceInKm* 1000) + " m away";
            }
            else
            {
                DecimalFormat twoDForm = new DecimalFormat("#0.00");

                // used 1.1 as to get better result for travel distance by road
                distanceString = "approx "+ twoDForm.format(distanceInKm* 1.1) + " Km away";
            }
        }

        String merchentAndDistanceStr = deals.getMerchantName() + ", " + distanceString;
        holder.mDealMerchantAndDistance.setText(merchentAndDistanceStr);

        return convertView;
    }

    // This class would contain all the ui elements which are there in the list item xml which would needs reseting
    class ViewHolder {
        private TextView mDealName;
        private NetworkImageView mDealImage;
        private TextView mDealMerchantAndDistance;
        private TextView mDealDistnceText;
        private TextView mDealPrice;

        private TextView mDealViewCount;
        private TextView mDealLikeCount;
        private TextView mDealShareCount;


    }

    private void makeImageRequest(NetworkImageView img, String url) {

        //url = url + ".png";

        ImageLoader imageLoader = HBVolley.getImageLoader();
        // If you are using NetworkImageView
        if (url != null) {
            img.setImageUrl(url, imageLoader);
        }
    }

    public static double distanceLatLong2(double lat1, double lng1, double lat2, double lng2)
    {
        double earthRadius = 6371.0d; // KM: use mile here if you want mile result

        double dLat = toRadian(lat2 - lat1);
        double dLng = toRadian(lng2 - lng1);

        double a = Math.pow(Math.sin(dLat/2), 2)  +
                Math.cos(toRadian(lat1)) * Math.cos(toRadian(lat2)) *
                        Math.pow(Math.sin(dLng/2), 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return earthRadius * c; // returns result kilometers
    }

    public static double toRadian(double degrees)
    {
        return (degrees * Math.PI) / 180.0d;
    }
}
