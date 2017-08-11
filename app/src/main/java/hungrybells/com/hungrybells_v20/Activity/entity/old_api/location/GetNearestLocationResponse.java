package hungrybells.com.hungrybells_v20.Activity.entity.old_api.location;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;

/**
 * Created by ajeetkumar on 21/05/15.
 */
public class GetNearestLocationResponse extends IDataModel {

    @SerializedName("locations")
    private List<NearestLocation> locations;

    public void setLocations(List<NearestLocation> locations) {

        this.locations = locations;
    }

    public List<NearestLocation> getLocations() {
        return this.locations;
    }
}
