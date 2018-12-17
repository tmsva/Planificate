package tmsva.org.free.planificate.webservice;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import tmsva.org.free.planificate.model.ArrivalsRs;
import tmsva.org.free.planificate.model.BipRs;
import tmsva.org.free.planificate.model.MapRs;

public interface TransantiagoService {

    @GET("v2/stops/{stop_id}/next_arrivals")
    Call<ArrivalsRs> getNextArrivalsFor(@Path("stop_id") String stopId);

    @GET("v2/map?radius=300")
    Call<MapRs> getMapForLocation(@Query("center_lon") double longitude, @Query("center_lat") double latitude);

    @GET("api/v1/solicitudes.json")
    Call<BipRs> getBipBalance(@Query("bip")int bipId);
}
