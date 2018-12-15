package tmsva.org.free.planificate.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArrivalsRs {

    @SerializedName("results")
    @Expose
    private List<Arrival> arrivals;

    public List<Arrival> getArrivals() {
        return arrivals;
    }

    public void setArrivals(List<Arrival> arrivals) {
        this.arrivals = arrivals;
    }
}
