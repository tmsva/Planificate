package tmsva.org.free.planificate.viewmodel;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import tmsva.org.free.planificate.model.ArrivalsRs;
import tmsva.org.free.planificate.model.Bip;
import tmsva.org.free.planificate.model.BipRs;
import tmsva.org.free.planificate.repository.MainActivityRepository;

public class MainActivityViewModel extends AndroidViewModel {

    private MainActivityRepository mRepo;
    private LiveData<BipRs> mBip;
    private LiveData<ArrivalsRs> mArrivals;
    private LiveData<List<Bip>> mBipList;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        mRepo = new MainActivityRepository(application);
        mBip = mRepo.getDefaultBip();
        mBipList = mRepo.getBipList();
        mArrivals = mRepo.getArrivals();
    }

    public LiveData<BipRs> getBip() {
        return mBip;
    }

    public LiveData<List<Bip>> getBipList() {
        return mBipList;
    }

    public LiveData<ArrivalsRs> getArrivals() {
        return mArrivals;
    }

    public void getNextArrivalsFor(String stopId) {
        mRepo.getNextArrivalsFor(stopId);
    }

    public void getBipBalance() {
        mRepo.getBipBalance();
    }

    public void insertBip(Bip bip) {
        mRepo.insertBip(bip);
    }

    public void getMapForLocation(double longitude, double latitude) {
        mRepo.getMapForLocation(longitude, latitude);
    }
}
