package hungrybells.com.hungrybells_v20.Activity.entity.dealList;

import com.google.gson.annotations.SerializedName;

import hungrybells.com.hungrybells_v20.Activity.entity.common.IDataModel;

/**
 * Created by ajeetkumar on 17/05/15.
 */
public class Deals extends IDataModel {


    @SerializedName("can_buy")
    private String can_buy;

    @SerializedName("merchantLogo")
    private String merchantLogo;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("original_price")
    private String original_price;

    @SerializedName("dealTag")
    private int dealTag;

    @SerializedName("deallike_count")
    private Integer deallike_count;



    @SerializedName("dealId")
    private String dealId;

    @SerializedName("dealshare_count")
    private Integer dealshare_count;

    @SerializedName("dealOrderedCount")
    private Integer dealOrderedCount;



    @SerializedName("merchantLongitude")
    private String merchantLongitude;

    @SerializedName("distance")
    private Double distance;

    @SerializedName("dealPrice")
    private String dealPrice;

    @SerializedName("name")
    private String name;

    @SerializedName("merchantName")
    private String merchantName;

    @SerializedName("merchantLatitude")
    private String merchantLatitude;

    @SerializedName("details")
    private String dealDetails;

    @SerializedName("detailText")
    private String detailText;

    @SerializedName("content_type")
    private String content_type;

    @SerializedName("merchantAddress")
    private String merchantAddress;

    @SerializedName("dealview_count")
    private Integer dealview_count;

    @SerializedName("deliveryTypes")
    private String[] deliveryTypes;

    @SerializedName("availability")
    private String availability;

    @SerializedName("merchantBranchid")
    private String merchantBranchid;


    public String getCan_buy ()
    {
        return can_buy;
    }

    public void setCan_buy (String can_buy)
    {
        this.can_buy = can_buy;
    }

    public String getMerchantLogo ()
    {
        return merchantLogo;
    }

    public void setMerchantLogo (String merchantLogo)
    {
        this.merchantLogo = merchantLogo;
    }

    public String getImageUrl ()
    {
        return imageUrl;
    }

    public void setImageUrl (String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public String getOriginal_price ()
    {
        return original_price;
    }

    public void setOriginal_price (String original_price)
    {
        this.original_price = original_price;
    }

    public int getDealTag ()
    {
        return dealTag;
    }

    public void setDealTag (int dealTag)
    {
        this.dealTag = dealTag;
    }

    public Integer getDeallike_count ()
    {
        return deallike_count;
    }

    public void setDeallike_count (Integer deallike_count)
    {
        this.deallike_count = deallike_count;
    }

    public String getDealId ()
    {
        return dealId;
    }

    public void setDealId (String dealId)
    {
        this.dealId = dealId;
    }

    public Integer getDealshare_count ()
    {
        return dealshare_count;
    }

    public void setDealshare_count (Integer dealshare_count)
    {
        this.dealshare_count = dealshare_count;
    }

    public String getMerchantLongitude ()
    {
        return merchantLongitude;
    }

    public void setMerchantLongitude (String merchantLongitude)
    {
        this.merchantLongitude = merchantLongitude;
    }

    public Double getDistance ()
    {
        return distance;
    }

    public void setDistance (Double distance)
    {
        this.distance = distance;
    }

    public String getDealPrice ()
    {
        return dealPrice;
    }

    public void setDealPrice (String dealPrice)
    {
        this.dealPrice = dealPrice;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getMerchantName ()
    {
        return merchantName;
    }

    public void setMerchantName (String merchantName)
    {
        this.merchantName = merchantName;
    }

    public String getMerchantLatitude ()
    {
        return merchantLatitude;
    }

    public void setMerchantLatitude (String merchantLatitude)
    {
        this.merchantLatitude = merchantLatitude;
    }


    public String getDealDetails(){ return dealDetails;}

    public void setDealDetails(String dealDetails){ this.dealDetails = dealDetails;}

    public String getDetailText ()
    {
        return detailText;
    }

    public void setDetailText (String detailText)
    {
        this.detailText = detailText;
    }

    public String getContent_type ()
    {
        return content_type;
    }

    public void setContent_type (String content_type)
    {
        this.content_type = content_type;
    }

    public String getMerchantAddress ()
    {
        return merchantAddress;
    }

    public void setMerchantAddress (String merchantAddress)
    {
        this.merchantAddress = merchantAddress;
    }

    public Integer getDealview_count ()
    {
        return dealview_count;
    }

    public void setDealview_count (Integer dealview_count)
    {
        this.dealview_count = dealview_count;
    }

    public String getAvailability ()
    {
        return availability;
    }

    public void setAvailability (String availability)
    {
        this.availability = availability;
    }

    public void setDeliveryTypes(String[] deliveryTypes) {
        this.deliveryTypes = deliveryTypes;
    }

    public String[] getDeliveryTypes() { return this.deliveryTypes; }

    public String getMerchantBranchid() { return this.merchantBranchid; }

    public void setMerchantBranchid(String merchantBranchid) { this.merchantBranchid = merchantBranchid; }

    public Integer getDealOrderedCount() {
        return dealOrderedCount;
    }

    public void setDealOrderedCount(Integer dealOrderedCount) {
        this.dealOrderedCount = dealOrderedCount;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [can_buy = "+can_buy+", merchantLogo = "+merchantLogo+", imageUrl = "+imageUrl+", original_price = "+original_price+", dealTag = "+dealTag+", deallike_count = "+deallike_count+", dealId = "+dealId+", dealshare_count = "+dealshare_count+", merchantLongitude = "+merchantLongitude+", distance = "+distance+", dealPrice = "+dealPrice+", name = "+name+", merchantName = "+merchantName+", merchantLatitude = "+merchantLatitude+", detailText = "+detailText+", content_type = "+content_type+", merchantAddress = "+merchantAddress+", dealview_count = "+dealview_count+", availability = "+availability+"]";
    }

}
