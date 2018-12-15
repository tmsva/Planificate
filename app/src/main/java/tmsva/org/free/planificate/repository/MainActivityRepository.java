package tmsva.org.free.planificate.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tmsva.org.free.planificate.dao.BipDao;
import tmsva.org.free.planificate.model.ArrivalsRs;
import tmsva.org.free.planificate.model.Bip;
import tmsva.org.free.planificate.model.BipRs;
import tmsva.org.free.planificate.model.RetrofitFactory;
import tmsva.org.free.planificate.persistence.PlanificateRoomDb;
import tmsva.org.free.planificate.webservice.TransantiagoService;

public class MainActivityRepository {

    private BipDao mBipDao;
    private TransantiagoService transantiagoService;
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
    }

    public void insertBip(Bip bip) {
        new insertAsyncTask(mBipDao).execute(bip);
    }
    private static class insertAsyncTask extends AsyncTask<Bip, Void, Void> {

        private BipDao mAsyncTaskDao;

        insertAsyncTask(BipDao mBipDao) {
            mAsyncTaskDao = mBipDao;
        }

        @Override
        protected Void doInBackground(final Bip... bips) {
            mAsyncTaskDao.insert(bips[0]);
            return null;
        }
    }

    public void getNextArrivalsFor(String stopId) {
        transantiagoService = RetrofitFactory
                .getInstance(RetrofitFactory.TRANSANTIAGO_API_URL)
                .create(TransantiagoService.class);

        transantiagoService.getNextArrivalsFor(stopId).enqueue(new Callback<ArrivalsRs>() {
            @Override
            public void onResponse(Call<ArrivalsRs> call, Response<ArrivalsRs> response) {
                if (response.body().getArrivals() != null && response.isSuccessful() && response.body().getArrivals().size() > 0)
                    mArrivals.setValue(response.body());
                else
                    mArrivals.setValue(null);
            }

            @Override
            public void onFailure(Call<ArrivalsRs> call, Throwable t) {
                retryQueryNextArrivals++;
                if(retryQueryNextArrivals <= 2) getNextArrivalsFor(stopId);
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

    public void getBipBalance() {
        transantiagoService = RetrofitFactory
                .getInstance(RetrofitFactory.BIP_BALANCE_API_URL)
                .create(TransantiagoService.class);
        List<Bip> bipList = mBipList.getValue();
        if(bipList == null) {
            return;
        }
        else {
            int bipId = bipList.get(0).getId();
            transantiagoService.getBipBalance(bipId).enqueue(new Callback<BipRs>() {
                @Override
                public void onResponse(Call<BipRs> call, Response<BipRs> response) {
                    if (response.code() == 400) {
                        //TODO handle different response object from server
                        Toast.makeText(mApplication, response.code() + " " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                    if (response.isSuccessful()) {
                        mDefaultBip.setValue(response.body());
                    }
                    else
                        Toast.makeText(mApplication, response.code() + " " + response.message(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<BipRs> call, Throwable t) {
                    Toast.makeText(mApplication, "Falló la conexion: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
