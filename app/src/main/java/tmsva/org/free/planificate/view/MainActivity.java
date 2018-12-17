package tmsva.org.free.planificate.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import tmsva.org.free.planificate.R;
import tmsva.org.free.planificate.model.Arrival;
import tmsva.org.free.planificate.model.ArrivalsRs;
import tmsva.org.free.planificate.model.Bip;
import tmsva.org.free.planificate.util.Parser;
import tmsva.org.free.planificate.viewmodel.MainActivityViewModel;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEdtStop;
    private Button btnQueryServices;
    private com.github.chrisbanes.photoview.PhotoView mPvMetroPlane;
    private LinearLayout llQueryGrid;
    private GridView gvQueryHeaders;
    private GridView gvQueryItems;
    private LinearLayout mLlProgressbar;
    private AlertDialog addBipDialog;
    private TextView txtBalanceDate;
    private TextView txtBipBalance;

    private MainActivityViewModel mViewModel;
    private ArrayAdapter<String> mItemsAdapter;
    private List<String> mDisplayableArrivals;
    private InputMethodManager imeManager;
    private FusedLocationProviderClient mFusedLocationClient;

    private static final String[] QUERY_HEADERS = new String[] {"Servicio", "Patente", "Tiempo estimado de llegada", "Distancia en mts."};

    private static final String EXTRA_BIP_BALANCE = "tmsva.org.free.planificate.EXTRA_BIP_BALANCE";
    private static final String EXTRA_BALANCE_DATE = "tmsva.org.free.planificate.EXTRA_BALANCE_DATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewsById();
        initComponents();
    }

    private void findViewsById() {
        txtBalanceDate = findViewById(R.id.txt_balance_date);
        txtBipBalance = findViewById(R.id.txt_bip_balance);
        mEdtStop = findViewById(R.id.edt_stop);
        mPvMetroPlane = findViewById(R.id.pv_metro_plane);
        mLlProgressbar = findViewById(R.id.ll_progressbar);
        btnQueryServices = findViewById(R.id.btn_query_services);
        llQueryGrid = findViewById(R.id.ll_query_grid);
        gvQueryHeaders = findViewById(R.id.gv_query_headers);
        gvQueryItems = findViewById(R.id.gv_query_items);
    }

    private void initComponents() {
        imeManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        gvQueryHeaders.setAdapter(new ArrayAdapter<>(this, R.layout.txt_grid_item, Arrays.asList(QUERY_HEADERS)));

        initViewModel();
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mViewModel.getArrivals().observe(this, this::setDisplayableArrivalList);
        mViewModel.getBipList().observe(this, (bipList) -> {
            if (addBipDialog != null) {
                addBipDialog.dismiss();
                Toast.makeText(this, "Inserción exitosa!", Toast.LENGTH_SHORT).show();
            }
        });
        mViewModel.getBip().observe(this, bipRs -> {
            refreshBipBalanceState(bipRs.getSaldoTarjeta(), bipRs.getFechaSaldo());
        });
    }

    private void refreshBipBalanceState(String balance, String date) {
        txtBipBalance.setText(balance);
        txtBalanceDate.setText(date);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Toast.makeText(this, "ONRESTOREINSTANCE", Toast.LENGTH_SHORT).show();
        super.onRestoreInstanceState(savedInstanceState);

        refreshBipBalanceState(savedInstanceState.getString(EXTRA_BIP_BALANCE),
                savedInstanceState.getString(EXTRA_BALANCE_DATE));
    }

    private void setDisplayableArrivalList(ArrivalsRs arrivalsRs) {
        btnQueryServices.setEnabled(true);
        mEdtStop.setEnabled(true);
        showMetroPlane(false);
        hideSoftKeyboard();

        if(arrivalsRs != null) {
            mDisplayableArrivals = new ArrayList<>();

            for(Arrival arrival : arrivalsRs.getArrivals()) {
                mDisplayableArrivals.add(arrival.getRouteId());
                mDisplayableArrivals.add(arrival.getBusPlateNumber());
                mDisplayableArrivals.add(arrival.getArrivalEstimation());
                mDisplayableArrivals.add(arrival.getBusDistance());
            }

            if(mItemsAdapter == null) mItemsAdapter = getItemsAdapter();
            else mItemsAdapter.clear();

            mItemsAdapter.addAll(mDisplayableArrivals);

            gvQueryItems.setAdapter(mItemsAdapter);
        } else {
            Toast.makeText(this, "No hay respuesta de los servidores!", Toast.LENGTH_SHORT).show();
            showMetroPlane(true);
        }
    }

    private ArrayAdapter<String> getItemsAdapter() {
        return new ArrayAdapter<>(this, R.layout.txt_grid_item, mDisplayableArrivals);
    }

    private void showMetroPlane(boolean showPlane) {
        mLlProgressbar.setVisibility(View.GONE);
        if(showPlane) {
            mPvMetroPlane.setVisibility(View.VISIBLE);
            llQueryGrid.setVisibility(View.GONE);
        } else {
            mPvMetroPlane.setVisibility(View.GONE);
            llQueryGrid.setVisibility(View.VISIBLE);
        }
    }

    private void hideSoftKeyboard() {
        imeManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        String balanceDateText = txtBalanceDate.getText().toString().trim();
        if(!TextUtils.isEmpty(balanceDateText) && !balanceDateText.equalsIgnoreCase("Ultima fecha actualizacion")) {
            outState.putString(EXTRA_BIP_BALANCE, txtBipBalance.getText().toString());
            outState.putString(EXTRA_BALANCE_DATE, balanceDateText);
            Toast.makeText(this, "SAVED", Toast.LENGTH_SHORT).show();
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_bip:
                addBipDialog = getAddBippDialog();
                addBipDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                addBipDialog.show();
                break;
            case R.id.btn_refresh_bip_balance:
                mViewModel.getBipBalance();
                break;
            case R.id.btn_get_location:
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                        if (location != null) {
                            double longitude = location.getLongitude();
                            double latitude = location.getLatitude();
                            //Toast.makeText(this, "LAT: " + location.getLatitude() + "\nLON: " + location.getLongitude(), Toast.LENGTH_SHORT).show();
                            mViewModel.getMapForLocation(longitude, latitude);
                        }
                        else Toast.makeText(this, "NO HAY HAND", Toast.LENGTH_SHORT).show();
                    });
                }
                break;
            case R.id.btn_query_services:
                String stop = getEdtStopText();
                if(validateQuery(stop)) {
                    mEdtStop.setEnabled(false);
                    hideSoftKeyboard();
                    btnQueryServices.setEnabled(false);
                    mLlProgressbar.setVisibility(View.VISIBLE);
                    mViewModel.getNextArrivalsFor(stop);
                }
            break;
        }
    }

    private AlertDialog getAddBippDialog() {
        if(addBipDialog == null) {
            EditText edtBipId = new EditText(MainActivity.this);
            edtBipId.setInputType(InputType.TYPE_CLASS_NUMBER);

            addBipDialog = new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle("Ingrese número de la tarjeta bip:")
                    .setView(edtBipId)
                    .setNegativeButton("Cancelar", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .setPositiveButton("Guardar", (dialog, which) -> {
                        Integer bipId = Parser.tryIntParse(edtBipId.getText().toString());
                        if (bipId != null) mViewModel.insertBip(new Bip(bipId));
                        else
                            Toast.makeText(MainActivity.this,
                                    "Número de tarjeta inválido. Intente nuevamente",
                                    Toast.LENGTH_LONG).show();
                    })
                    .create();
        }
        return addBipDialog;
    }

    private String getEdtStopText() {
        return mEdtStop.getText().toString().trim().toUpperCase();
    }

    private boolean validateQuery(String stopId) {
        if(!TextUtils.isEmpty(stopId)) return true;
        Toast.makeText(this, "Ingrese un paradero válido", Toast.LENGTH_SHORT).show();
        return false;
    }

}
