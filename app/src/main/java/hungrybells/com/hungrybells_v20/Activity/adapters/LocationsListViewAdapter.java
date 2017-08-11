package hungrybells.com.hungrybells_v20.Activity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import hungrybells.com.hungrybells_v20.Activity.entity.change_location.Location;
import hungrybells.com.hungrybells_v20.Activity.entity.old_api.location.NearestLocation;
import hungrybells.com.hungrybells_v20.R;

/**
 * Created by ajeetkumar on 21/05/15.
 */
public class LocationsListViewAdapter extends ArrayAdapter<Location>  implements Filterable {

    private LayoutInflater mInflater;
    private List<Location> locationList;
    private ArrayList<Location> arraylist;
    private Context mContext;
    private int resourceId;

    //private ItemFilter mFilter = new ItemFilter();
    //private List<Location>originalData = null;
    //private List<Location>filteredData = null;

    public LocationsListViewAdapter(Context context, int resource, List<Location> locationList) {
        super(context, resource, locationList);
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.locationList = locationList;
        this.resourceId = resource;

        this.arraylist = new ArrayList<Location>();
        this.arraylist.addAll(this.locationList);
    }

    public Location getItem(int position) {
        return locationList.get(position);
    }

    @Override
    public int getCount() {
        return locationList.size();
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

            holder.mLocationName = (TextView) convertView.findViewById(R.id.location_name_text_view);

            convertView.setTag(holder);
        }

        Location nearestLocation = locationList.get(position);
        holder.mLocationName.setText(nearestLocation.getLocationName());



        return convertView;
    }

    // This class would contain all the ui elements which are there in the list item xml which would needs reseting
    class ViewHolder {
        private TextView mLocationName;


    }

    public void addToArrayList(List<Location> data) {

        this.arraylist.addAll(data);

    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        locationList.clear();
        if (charText.length() == 0) {
            locationList.addAll(arraylist);
        }
        else
        {
            for (Location nearestLocation : arraylist)
            {
                if (nearestLocation.getLocationName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    this.locationList.add(nearestLocation);
                }
            }
        }

        notifyDataSetChanged();
    }

    /*

    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<NearestLocation> list = originalData;

            int count = list.size();
            final ArrayList<NearestLocation> nlist = new ArrayList<NearestLocation>(count);

            NearestLocation filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i);
                if (filterableString.getName().toLowerCase().contains(filterString)) {
                    nlist.add(filterableString);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<NearestLocation>) results.values;
            notifyDataSetChanged();
        }
    }
    */
}
