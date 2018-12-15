package tmsva.org.free.planificate.webservice;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import tmsva.org.free.planificate.model.ArrivalsRs;
import tmsva.org.free.planificate.model.BipRs;

public interface TransantiagoService {

    @GET("v2/stops/{stop_id}/next_arrivals")
    Call<ArrivalsRs> getNextArrivalsFor(@Path("stop_id") String stopId);

    @GET("api/v1/solicitudes.json")
    Call<BipRs> getBipBalance(@Query("bip")int bipId);
}
