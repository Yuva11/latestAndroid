package hungrybells.com.hungrybells_v20.Activity.entity.common;

/**
 * Created by dev on 02/06/15.
 */
public class NoDataFoundResponse extends IDataModel {

    private int statusCode;

    public NoDataFoundResponse(int val) {
        this.statusCode = val;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

}
