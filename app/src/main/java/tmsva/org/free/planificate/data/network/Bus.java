package tmsva.org.free.planificate.data.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bus {
    @SerializedName("bus_plate_number")
    @Expose
    private String busPlateNumber;
    @SerializedName("operator_number")
    @Expose
    private Integer operatorNumber;
    @SerializedName("direction_id")
    @Expose
    private Integer directionId;
    @SerializedName("bus_movement_orientation")
    @Expose
    private Integer busMovementOrientation;
    @SerializedName("added_at")
    @Expose
    private String addedAt;
    @SerializedName("bus_lon")
    @Expose
    private String busLon;
    @SerializedName("route_id")
    @Expose
    private String routeId;
    @SerializedName("bus_speed")
    @Expose
    private Integer busSpeed;
    @SerializedName("bus_lat")
    @Expose
    private String busLat;
    @SerializedName("captured_at")
    @Expose
    private String capturedAt;

    public String getBusPlateNumber() {
        return busPlateNumber;
    }

    public void setBusPlateNumber(String busPlateNumber) {
        this.busPlateNumber = busPlateNumber;
    }

    public Integer getOperatorNumber() {
        return operatorNumber;
    }

    public void setOperatorNumber(Integer operatorNumber) {
        this.operatorNumber = operatorNumber;
    }

    public Integer getDirectionId() {
        return directionId;
    }

    public void setDirectionId(Integer directionId) {
        this.directionId = directionId;
    }

    public Integer getBusMovementOrientation() {
        return busMovementOrientation;
    }

    public void setBusMovementOrientation(Integer busMovementOrientation) {
        this.busMovementOrientation = busMovementOrientation;
    }

    public String getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(String addedAt) {
        this.addedAt = addedAt;
    }

    public String getBusLon() {
        return busLon;
    }

    public void setBusLon(String busLon) {
        this.busLon = busLon;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public Integer getBusSpeed() {
        return busSpeed;
    }

    public void setBusSpeed(Integer busSpeed) {
        this.busSpeed = busSpeed;
    }

    public String getBusLat() {
        return busLat;
    }

    public void setBusLat(String busLat) {
        this.busLat = busLat;
    }

    public String getCapturedAt() {
        return capturedAt;
    }

    public void setCapturedAt(String capturedAt) {
        this.capturedAt = capturedAt;
    }
}
