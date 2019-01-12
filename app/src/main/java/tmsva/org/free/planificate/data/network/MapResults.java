package tmsva.org.free.planificate.data.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MapResults {
    @SerializedName("bip_spots")
    @Expose
    private List<BipSpot> bipSpots = null;
    @SerializedName("stops")
    @Expose
    private List<Stop> stops = null;
    @SerializedName("buses")
    @Expose
    private List<Bus> buses = null;

    public List<BipSpot> getBipSpots() {
        return bipSpots;
    }

    public void setBipSpots(List<BipSpot> bipSpots) {
        this.bipSpots = bipSpots;
    }

    public List<Stop> getStops() {
        return stops;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

    public List<Bus> getBuses() {
        return buses;
    }

    public void setBuses(List<Bus> buses) {
        this.buses = buses;
    }
}
