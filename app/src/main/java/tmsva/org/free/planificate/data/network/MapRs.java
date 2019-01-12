package tmsva.org.free.planificate.data.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MapRs {
    @SerializedName("results")
    @Expose
    private MapResults results;

    public MapResults getResults() {
        return results;
    }

    public void setResults(MapResults results) {
        this.results = results;
    }
}
