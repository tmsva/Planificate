package tmsva.org.free.planificate.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import tmsva.org.free.planificate.R;
import tmsva.org.free.planificate.model.Arrival;
import tmsva.org.free.planificate.model.ArrivalsRs;
import tmsva.org.free.planificate.model.Bip;
import tmsva.org.free.planificate.model.BipRs;
import tmsva.org.free.planificate.util.Parser;
import tmsva.org.free.planificate.viewmodel.MainActivityViewModel;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel mViewModel;

    private Button btnRefreshBipBalance;
    private EditText mEdtStop;
    private Button btnQueryServices;
    private Button btnAddBip;
    private com.github.chrisbanes.photoview.PhotoView mPvMetroPlane;
    private LinearLayout llQueryGrid;
    private GridView gvQueryHeaders;
    private GridView gvQueryItems;
    private LinearLayout mLlProgressbar;
    private AlertDialog addBipDialog;

    private InputMethodManager inputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewsById();
        initComponents();
    }

    private void findViewsById() {
        btnRefreshBipBalance = findViewById(R.id.btn_refresh_bip_balance);
        mEdtStop = findViewById(R.id.edt_stop);
        mPvMetroPlane = findViewById(R.id.pv_metro_plane);
        mLlProgressbar = findViewById(R.id.ll_progressbar);
        btnQueryServices = findViewById(R.id.btn_query_services);
        btnAddBip = findViewById(R.id.btn_add_bip);

        llQueryGrid = findViewById(R.id.ll_query_grid);
        gvQueryHeaders = findViewById(R.id.gv_query_headers);
        gvQueryItems = findViewById(R.id.gv_query_items);
    }

    private String getEdtStopText() {
        return mEdtStop.getText().toString().toUpperCase();
    }

    @TargetApi(21)
    private void initComponents() {
        inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        initViewModel();

        String[] query_headers = new String[] {"Servicio", "Patente", "Tiempo estimado de llegada", "Distancia en mts."};

        gvQueryHeaders.setAdapter(new ArrayAdapter<>(this, R.layout.txt_grid_item, Arrays.asList(query_headers)));

        btnAddBip.setOnClickListener(v -> {
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
                        if(bipId != null) {
                            mViewModel.insertBip(new Bip(bipId));
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Número de tarjeta inválido. Intente nuevamente", Toast.LENGTH_LONG).show();
                        }
                    })
                    .create();

            edtBipId.requestFocus();
            addBipDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            addBipDialog.show();
        });

        btnRefreshBipBalance.setOnClickListener(v -> {
            mViewModel.getBipBalance();
        });

        btnQueryServices.setOnClickListener(v -> {
            hideSoftKeyboard();
            btnQueryServices.setEnabled(false);
            mLlProgressbar.setVisibility(View.VISIBLE);
            mViewModel.getNextArrivalsFor(getEdtStopText());
        });
    }

    private void initViewModel() {
        this.mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        this.mViewModel.getArrivals().observe(this, this::setDisplayableArrivalList);
        this.mViewModel.getBipList().observe(this, (bipList) -> {
            if (addBipDialog != null) {
                addBipDialog.dismiss();
                Toast.makeText(this, "Inserción exitosa!", Toast.LENGTH_SHORT).show();
            }
        });
        mViewModel.getBip().observe(this, this::refreshBipBalanceState);
    }

    private void refreshBipBalanceState(BipRs bipRs) {
        final TextView txtBipBalance = findViewById(R.id.txt_bip_balance);
        final TextView txtBalanceDate = findViewById(R.id.txt_balance_date);
        txtBipBalance.setText(bipRs.getSaldoTarjeta());
        txtBalanceDate.setText(bipRs.getFechaSaldo());
    }

    private void setDisplayableArrivalList(ArrivalsRs arrivalsRs) {
        btnQueryServices.setEnabled(true);
        showMetroPlane(false);
        hideSoftKeyboard();

        if(arrivalsRs != null) {
            List<String> displayableArrivals = new ArrayList<>();

            for (Arrival arrival : arrivalsRs.getArrivals()) {
                displayableArrivals.add(arrival.getRouteId());
                displayableArrivals.add(arrival.getBusPlateNumber());
                displayableArrivals.add(arrival.getArrivalEstimation());
                displayableArrivals.add(arrival.getBusDistance());
            }

            gvQueryItems.setAdapter(new ArrayAdapter<>(this, R.layout.txt_grid_item, displayableArrivals));
        } else {
            Toast.makeText(this, "No hay respuesta de los servidores!", Toast.LENGTH_SHORT).show();
            showMetroPlane(true);
        }
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
        inputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }
}
