package tmsva.org.free.planificate.ui;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import tmsva.org.free.planificate.R;
import tmsva.org.free.planificate.data.database.Bip;
import tmsva.org.free.planificate.utilities.Parser;
import tmsva.org.free.planificate.viewmodels.MainActivityViewModel;

public class BipStatusFragment extends Fragment implements View.OnClickListener {
    private AlertDialog addBipDialog;
    private TextView txtBalanceDate;
    private TextView txtBipBalance;
    private Button btnRefresh;

    private MainActivityViewModel mViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initialize fields and data
        createComponents();
    }

    private void createComponents() {
        addBipDialog = getAddBipDialog();
        initViewModel();
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mViewModel.getBip().observe(this, bipRs -> {
            refreshBipBalanceState(bipRs.getSaldoTarjeta(), bipRs.getFechaSaldo());
        });
        mViewModel.getBipList().observe(this, (bipList) -> {
            if (addBipDialog.isShowing()) {
                addBipDialog.dismiss();
                Toast.makeText(getActivity(), "Inserción exitosa!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bip_status, container, false);
        //Load layout for this fragment
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        //find views inside layout and init those components
        txtBalanceDate = v.findViewById(R.id.txt_balance_date);
        txtBipBalance = v.findViewById(R.id.txt_bip_balance);
        btnRefresh = v.findViewById(R.id.btn_refresh_bip_balance);

        v.findViewById(R.id.btn_add_bip).setOnClickListener(this);
        btnRefresh.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mViewModel.getBipBalance();
    }

    private void refreshBipBalanceState(String balance, String date) {
        txtBipBalance.setText(balance);
        txtBalanceDate.setText(date);
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.btn_add_bip:
                addBipDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                addBipDialog.show();
                break;
            case R.id.btn_refresh_bip_balance:
                mViewModel.getBipBalance();
                break;
        }
    }

    private AlertDialog getAddBipDialog () {
        if (addBipDialog == null) {
            EditText edtBipId = new EditText(getActivity());
            edtBipId.setInputType(InputType.TYPE_CLASS_NUMBER);
            addBipDialog = new AlertDialog.Builder(getActivity())
                    .setCancelable(false)
                    .setView(edtBipId)
                    .setTitle("Ingrese número de la tarjeta bip:")
                    .setNegativeButton("Cancelar", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .setPositiveButton("Guardar", (dialog, which) -> {
                        Integer bipId = Parser.tryIntParse(edtBipId.getText().toString());
                        if (bipId != null) mViewModel.insertBip(new Bip(bipId));
                        else Toast.makeText(getContext(),
                                "Número de tarjeta inválido. Intente nuevamente",
                                Toast.LENGTH_LONG).show();
                    })
                    .create();
        }
        return addBipDialog;
    }

}
