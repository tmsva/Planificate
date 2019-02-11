package tmsva.org.free.planificate.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import tmsva.org.free.planificate.R;
import tmsva.org.free.planificate.adapters.QueryServicesAdapter;
import tmsva.org.free.planificate.data.network.ArrivalsRs;
import tmsva.org.free.planificate.utilities.Toaster;
import tmsva.org.free.planificate.viewmodels.MainActivityViewModel;

public class QueryServicesFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_LOCATION = 1289;
    private EditText mEdtStop;
    private EditText mEdtService;
    private Button btnQueryServices;
    private Button btnServicesByLocation;
    private ProgressBar progressBar;
    private RecyclerView rvArrivals;
    private QueryServicesAdapter mAdapter;

    private static Location mCurrentLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private MainActivityViewModel mViewModel;

    private static final String[] QUERY_HEADERS = new String[] {"Servicio", "Patente", "Tiempo estimado de llegada", "Distancia en mts."};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createComponents();
    }

    private void createComponents() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mAdapter = new QueryServicesAdapter(getContext(), null);
        subscribeUi();
    }

    private void subscribeUi() {
        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        mViewModel.getArrivals().observe(this, this::setAdapter);
    }

    private void setAdapter(ArrivalsRs arrivalsRs) {
        enableQueryViews(true);
        ((MainActivity) getActivity()).hideSoftKeyboard();
        if(arrivalsRs != null) mAdapter.updateArrivals(arrivalsRs.getArrivals());
        else Toaster.show(getContext(), R.string.services_null_response);
        progressBar.setVisibility(View.GONE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_query_services, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        findViewsById(v);
        initComponents();
    }

    private void findViewsById(View v) {
        if(v != null) {
            rvArrivals = v.findViewById(R.id.rv_arrivals);

            btnQueryServices = v.findViewById(R.id.btn_query_services);
            btnServicesByLocation = v.findViewById(R.id.btn_services_by_location);
            mEdtStop = v.findViewById(R.id.edt_stop);
            progressBar = v.findViewById(R.id.progressbar);
            mEdtService = v.findViewById(R.id.edt_service);
            btnQueryServices.setOnClickListener(this);
            btnServicesByLocation.setOnClickListener(this);
        }
    }

    private void initComponents() {
        rvArrivals.setHasFixedSize(true);
        rvArrivals.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_services_by_location:
                if(ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), location -> {
                        mCurrentLocation = location;
                        getClosestStops();
                    });
                } else {
                    if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION))
                        Toast.makeText(getActivity(), "Location permission is needed to use this functionality", Toast.LENGTH_SHORT).show();
                    requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                }
                break;
            case R.id.btn_query_services:
                String stopId = getEdtStopText();
                if(validateQuery(stopId)) {
                    enableQueryViews(false);
                    mAdapter.clear();
                    ((MainActivity) getActivity()).hideSoftKeyboard();
                    progressBar.setVisibility(View.VISIBLE);
                    mViewModel.getNextArrivalsBy(stopId);
                } else btnQueryServices.setEnabled(true);
                break;
        }
    }

    private void enableQueryViews(boolean enable) {
        mEdtStop.setEnabled(enable);
        btnQueryServices.setEnabled(enable);
        mEdtService.setEnabled(enable);
        btnServicesByLocation.setEnabled(enable);
    }

    //TODO activate Fused Location Provider client (page - Receive location updates | ADevs)
    private void getClosestStops() {
        if (mCurrentLocation != null) {
            double longitude = mCurrentLocation.getLongitude();
            double latitude = mCurrentLocation.getLatitude();
            mViewModel.getMapForLocation(longitude, latitude);
        } else Toast.makeText(getActivity(), "Can't find your location", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_LOCATION) {
            if (grantResults[0] == PermissionChecker.PERMISSION_GRANTED) getClosestStops();
            else Toast.makeText(getActivity(), "Permission not granted", Toast.LENGTH_SHORT).show();
        } else super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private String getEdtStopText() { return mEdtStop.getText().toString().trim().toUpperCase(); }

    private boolean validateQuery(String stopId) {
        if(!TextUtils.isEmpty(stopId)) return true;
        Toast.makeText(getActivity(), "Enter a valid stop", Toast.LENGTH_SHORT).show();
        return false;
    }
}
