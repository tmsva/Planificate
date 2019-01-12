package tmsva.org.free.planificate.data.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BipSpot {
    @SerializedName("bip_spot_lat")
    @Expose
    private String bipSpotLat;
    @SerializedName("bip_spot_code")
    @Expose
    private String bipSpotCode;
    @SerializedName("bip_spot_fantasy_name")
    @Expose
    private String bipSpotFantasyName;
    @SerializedName("bip_spot_commune")
    @Expose
    private String bipSpotCommune;
    @SerializedName("bip_spot_lon")
    @Expose
    private String bipSpotLon;
    @SerializedName("bip_spot_address")
    @Expose
    private String bipSpotAddress;
    @SerializedName("bip_spot_entity")
    @Expose
    private String bipSpotEntity;
    @SerializedName("bip_opening_time")
    @Expose
    private Object bipOpeningTime;

    public String getBipSpotLat() {
        return bipSpotLat;
    }

    public void setBipSpotLat(String bipSpotLat) {
        this.bipSpotLat = bipSpotLat;
    }

    public String getBipSpotCode() {
        return bipSpotCode;
    }

    public void setBipSpotCode(String bipSpotCode) {
        this.bipSpotCode = bipSpotCode;
    }

    public String getBipSpotFantasyName() {
        return bipSpotFantasyName;
    }

    public void setBipSpotFantasyName(String bipSpotFantasyName) {
        this.bipSpotFantasyName = bipSpotFantasyName;
    }

    public String getBipSpotCommune() {
        return bipSpotCommune;
    }

    public void setBipSpotCommune(String bipSpotCommune) {
        this.bipSpotCommune = bipSpotCommune;
    }

    public String getBipSpotLon() {
        return bipSpotLon;
    }

    public void setBipSpotLon(String bipSpotLon) {
        this.bipSpotLon = bipSpotLon;
    }

    public String getBipSpotAddress() {
        return bipSpotAddress;
    }

    public void setBipSpotAddress(String bipSpotAddress) {
        this.bipSpotAddress = bipSpotAddress;
    }

    public String getBipSpotEntity() {
        return bipSpotEntity;
    }

    public void setBipSpotEntity(String bipSpotEntity) {
        this.bipSpotEntity = bipSpotEntity;
    }

    public Object getBipOpeningTime() {
        return bipOpeningTime;
    }

    public void setBipOpeningTime(Object bipOpeningTime) {
        this.bipOpeningTime = bipOpeningTime;
    }
}
