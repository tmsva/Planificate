package tmsva.org.free.planificate.data.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {

    public static final String TRANSANTIAGO_API_URL = "https://api.scltrans.it/";
    public static final String BIP_BALANCE_API_URL = "http://bip-servicio.herokuapp.com/";

    public static Retrofit getInstance(String url) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
