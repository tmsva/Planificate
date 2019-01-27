package tmsva.org.free.planificate.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import tmsva.org.free.planificate.R;
import tmsva.org.free.planificate.data.network.Arrival;
import tmsva.org.free.planificate.data.network.ArrivalsRs;
import tmsva.org.free.planificate.viewmodels.MainActivityViewModel;

public class QueryServicesFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_LOCATION = 1289;
    private EditText mEdtStop;
    private EditText mEdtService;
    private Button btnQueryServices;
    private Button btnServicesByLocation;
    private GridView gvQueryHeaders;
    private GridView gvQueryItems;
    private LinearLayout llProgressBar;

    private static Location mCurrentLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private MainActivityViewModel mViewModel;
    private ArrayAdapter<String> mItemsAdapter;

    private static final String[] QUERY_HEADERS = new String[] {"Servicio", "Patente", "Tiempo estimado de llegada", "Distancia en mts."};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createComponents();
    }

    private void createComponents() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        initViewModel();
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        mViewModel.getArrivals().observe(this, this::setDisplayableArrivalList);
    }

    private void setDisplayableArrivalList(ArrivalsRs arrivalsRs) {
        enableQueryViews(true);
        ((MainActivity) getActivity()).hideSoftKeyboard();
        mItemsAdapter.clear();
        if(arrivalsRs != null)
            for(Arrival arrival : arrivalsRs.getArrivals()) {
                mItemsAdapter.add(arrival.getRouteId());
                mItemsAdapter.add(arrival.getBusPlateNumber());
                mItemsAdapter.add(arrival.getArrivalEstimation());
                mItemsAdapter.add(arrival.getBusDistance());
            }
        else Toast.makeText(getActivity(), "No hay respuesta de los servidores!", Toast.LENGTH_SHORT).show();

        mItemsAdapter.notifyDataSetChanged();
        llProgressBar.setVisibility(View.GONE);
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
            btnQueryServices = v.findViewById(R.id.btn_query_services);
            btnServicesByLocation = v.findViewById(R.id.btn_services_by_location);
            mEdtStop = v.findViewById(R.id.edt_stop);
            mEdtService = v.findViewById(R.id.edt_service);
            gvQueryHeaders = v.findViewById(R.id.gv_query_headers);
            gvQueryItems = v.findViewById(R.id.gv_query_items);
            llProgressBar = v.findViewById(R.id.ll_progressbar);

            btnQueryServices.setOnClickListener(this);
            btnServicesByLocation.setOnClickListener(this);
        }
    }

    private void initComponents() {
        gvQueryHeaders.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.txt_grid_item, Arrays.asList(QUERY_HEADERS)));
        mItemsAdapter = new ArrayAdapter<>(getActivity(), R.layout.txt_grid_item, new ArrayList<>());
        gvQueryItems.setAdapter(mItemsAdapter);
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
                    ((MainActivity) getActivity()).hideSoftKeyboard();
                    llProgressBar.setVisibility(View.VISIBLE);
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
