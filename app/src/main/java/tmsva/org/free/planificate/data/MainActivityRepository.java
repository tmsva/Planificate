package tmsva.org.free.planificate.data;

import android.app.Application;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tmsva.org.free.planificate.data.database.BipDao;
import tmsva.org.free.planificate.data.network.ArrivalsRs;
import tmsva.org.free.planificate.data.database.Bip;
import tmsva.org.free.planificate.data.network.BipRs;
import tmsva.org.free.planificate.data.network.MapRs;
import tmsva.org.free.planificate.data.network.RetrofitFactory;
import tmsva.org.free.planificate.data.database.PlanificateRoomDb;
import tmsva.org.free.planificate.data.network.TransantiagoService;

public class MainActivityRepository {

    private BipDao mBipDao;
    private TransantiagoService transantiagoService;
    private TransantiagoService bipService;
    private final MutableLiveData<ArrivalsRs> mArrivals;
    private LiveData<List<Bip>> mBipList;
    private final MutableLiveData<BipRs> mDefaultBip;

    private Application mApplication;

    private static int retryQueryNextArrivals = 0;

    public MainActivityRepository(Application application) {
        mApplication = application;
        final PlanificateRoomDb db = PlanificateRoomDb.getDatabase(application);

        mBipDao = db.bipDao();
        mBipList = mBipDao.getAllBips();
        mDefaultBip = new MutableLiveData<>();
        mArrivals = new MutableLiveData<>();

        bipService = RetrofitFactory
                .getInstance(RetrofitFactory.BIP_BALANCE_API_URL)
                .create(TransantiagoService.class);
        transantiagoService = RetrofitFactory
                .getInstance(RetrofitFactory.TRANSANTIAGO_API_URL)
                .create(TransantiagoService.class);
    }

    public void insertBip(Bip bip) {
        new insertAsyncTask(mBipDao).execute(bip);
    }
    private static class insertAsyncTask extends AsyncTask<Bip, Void, Void> {
        private BipDao mAsyncTaskDao;

        insertAsyncTask(BipDao bipDao) {
            mAsyncTaskDao = bipDao;
        }

        @Override
        protected Void doInBackground(final Bip... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public void getNextArrivalsBy(String stopId) {
        transantiagoService.getNextArrivalsBy(stopId).enqueue(new Callback<ArrivalsRs>() {
            @Override
            public void onResponse(Call<ArrivalsRs> call, Response<ArrivalsRs> response) {
                ArrivalsRs rsBody = response.body();
                if (rsBody != null && rsBody.getArrivals() != null
                        && rsBody.getArrivals().size() > 0)
                    mArrivals.setValue(response.body());
                else mArrivals.setValue(null);
            }

            @Override
            public void onFailure(Call<ArrivalsRs> call, Throwable t) {
                retryQueryNextArrivals++;
                if(retryQueryNextArrivals <= 2) getNextArrivalsBy(stopId);
                else {
                    retryQueryNextArrivals = 0;
                    mArrivals.setValue(null);
                }
            }
        });
    }

    public LiveData<BipRs> getDefaultBip() {
        return mDefaultBip;
    }

    public LiveData<List<Bip>> getBipList() {
        return mBipList;
    }

    public LiveData<ArrivalsRs> getArrivals() {
        return mArrivals;
    }

    public void getMapForLocation(double longitude, double latitude) {
        transantiagoService.getMapForLocation(longitude, latitude).enqueue(new Callback<MapRs>() {
            @Override
            public void onResponse(Call<MapRs> call, Response<MapRs> response) {
                if(response.isSuccessful() && response.body() != null) {
                    Toast.makeText(mApplication, "TOTAL: "+response.body().getResults().getStops().size(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(mApplication, response.body().getResults().getStops().get(0).getStopId(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(mApplication, response.body().getResults().getStops().get(response.body().getResults().getStops().size()-1).getStopId(), Toast.LENGTH_SHORT).show();
                    /*for (Stop stop : response.body().getResults().getStops()) {
                        Toast.makeText(mApplication, stop.getStopId(), Toast.LENGTH_SHORT).show();
                    }*/
                }
                else Toast.makeText(mApplication, "RS no Esitosa", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<MapRs> call, Throwable t) {
                Toast.makeText(mApplication, "FALLOOOOOOOOOOOOO", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getBipBalance() {
        List<Bip> bipList = mBipList.getValue();
        if(bipList == null || bipList.size() < 1) return;
        else {
            int bipId = bipList.get(0).getId();
            bipService.getBipBalance(bipId).enqueue(new Callback<BipRs>() {
                @Override
                public void onResponse(Call<BipRs> call, Response<BipRs> response) {
                    if (response.code() == 400) {
                        //TODO handle different response object from server
                        Toast.makeText(mApplication, response.code() + " " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                    if(response.isSuccessful()) mDefaultBip.setValue(response.body());
                    else Toast.makeText(mApplication, response.code() + " " + response.message(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<BipRs> call, Throwable t) {
                    Toast.makeText(mApplication, "Falló la conexion: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
