package tmsva.org.free.planificate.data.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Arrival {

    @SerializedName("bus_plate_number")
    @Expose
    private String busPlateNumber;
    @SerializedName("direction_id")
    @Expose
    private Integer directionId;
    @SerializedName("calculated_at")
    @Expose
    private String calculatedAt;
    @SerializedName("arrival_estimation")
    @Expose
    private String arrivalEstimation;
    @SerializedName("route_id")
    @Expose
    private String routeId;
    @SerializedName("is_live")
    @Expose
    private Boolean isLive;
    @SerializedName("bus_distance")
    @Expose
    private String busDistance;

    public String getBusPlateNumber() {
        return busPlateNumber == null ? "" : busPlateNumber;
    }

    public void setBusPlateNumber(String busPlateNumber) {
        this.busPlateNumber = busPlateNumber;
    }

    public Integer getDirectionId() {
        return directionId;
    }

    public void setDirectionId(Integer directionId) {
        this.directionId = directionId;
    }

    public String getCalculatedAt() {
        return calculatedAt;
    }

    public void setCalculatedAt(String calculatedAt) {
        this.calculatedAt = calculatedAt;
    }

    public String getArrivalEstimation() {
        return arrivalEstimation;
    }

    public void setArrivalEstimation(String arrivalEstimation) {
        this.arrivalEstimation = arrivalEstimation;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public Boolean getIsLive() {
        return isLive;
    }

    public void setIsLive(Boolean isLive) {
        this.isLive = isLive;
    }

    public String getBusDistance() {
        return busDistance == null ? "" : busDistance.concat(" mts.");
    }

    public void setBusDistance(String busDistance) {
        this.busDistance = busDistance;
    }
}
