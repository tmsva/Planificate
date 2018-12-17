package tmsva.org.free.planificate.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Stop {
    @SerializedName("stop_lat")
    @Expose
    private String stopLat;
    @SerializedName("stop_code")
    @Expose
    private String stopCode;
    @SerializedName("stop_lon")
    @Expose
    private String stopLon;
    @SerializedName("agency_id")
    @Expose
    private String agencyId;
    @SerializedName("stop_id")
    @Expose
    private String stopId;
    @SerializedName("stop_name")
    @Expose
    private String stopName;

    public String getStopLat() {
        return stopLat;
    }

    public void setStopLat(String stopLat) {
        this.stopLat = stopLat;
    }

    public String getStopCode() {
        return stopCode;
    }

    public void setStopCode(String stopCode) {
        this.stopCode = stopCode;
    }

    public String getStopLon() {
        return stopLon;
    }

    public void setStopLon(String stopLon) {
        this.stopLon = stopLon;
    }

    public String getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(String agencyId) {
        this.agencyId = agencyId;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }
}
