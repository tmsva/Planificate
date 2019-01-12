package tmsva.org.free.planificate.data.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TransantiagoService {

    @GET("v2/stops/{stop_id}/next_arrivals")
    Call<ArrivalsRs> getNextArrivalsBy(@Path("stop_id") String stopId);

    @GET("v2/map?radius=300")
    Call<MapRs> getMapForLocation(@Query("center_lon") double longitude, @Query("center_lat") double latitude);

    @GET("api/v1/solicitudes.json")
    Call<BipRs> getBipBalance(@Query("bip")int bipId);
}
