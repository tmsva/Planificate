package tmsva.org.free.planificate.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import tmsva.org.free.planificate.R;
import tmsva.org.free.planificate.viewmodels.MainActivityViewModel;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private com.github.chrisbanes.photoview.PhotoView mPvMetroPlane;
    private AlertDialog addBipDialog;

    private FrameLayout frameLayout;
    private QueryServicesFragment queryServicesFragment;
    private Fragment activeFragment;

    private final FragmentManager fragmentManager = getSupportFragmentManager();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = (menuItem) -> {
        switch (menuItem.getItemId()) {
            case R.id.nav_metro_plane:
                showMetroPlane();
                return true;
            case R.id.nav_main:
                setMainNavFragment(queryServicesFragment.getClass().getSimpleName());
                return true;
        }
        return false;
    };

    private void setMainNavFragment(String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment curFragment = fragmentManager.findFragmentByTag(tag);
        if(curFragment != null) {
            hideMetroPlane();
            transaction.hide(activeFragment).show(curFragment);
            activeFragment = curFragment;
        }
        transaction.commit();
    }

    private void showMetroPlane() {
        fragmentManager.beginTransaction().hide(activeFragment).commit();
        hideSoftKeyboard();
        frameLayout.findViewById(R.id.pv_metro_plane).setVisibility(View.VISIBLE);
    }

    private void hideMetroPlane() {
        frameLayout.findViewById(R.id.pv_metro_plane).setVisibility(View.GONE);
    }

    private MainActivityViewModel mViewModel;
    private InputMethodManager imeManager;

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
        frameLayout = findViewById(R.id.fl_main_container);

        //mPvMetroPlane = findViewById(R.id.pv_metro_plane);
        ((BottomNavigationView) findViewById(R.id.bnv_main)).setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void initComponents() {
        queryServicesFragment = new QueryServicesFragment();

        imeManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        addFragments(new Fragment[] {queryServicesFragment});

        initViewModel();
    }

    private void addFragments(Fragment[] fragments) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        activeFragment = fragments[0];
        for(Fragment frag: fragments) {
            String tag = frag.getClass().getSimpleName();
            transaction.add(R.id.fl_main_container, frag, tag).addToBackStack(tag).hide(frag);
        }
        transaction.show(activeFragment).commit();
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Toast.makeText(this, "ONRESTOREINSTANCE", Toast.LENGTH_SHORT).show();
        super.onRestoreInstanceState(savedInstanceState);
        /*refreshBipBalanceState(savedInstanceState.getString(EXTRA_BIP_BALANCE),
                savedInstanceState.getString(EXTRA_BALANCE_DATE));*/
    }


    public void hideSoftKeyboard() {
        imeManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        /*String balanceDateText = txtBalanceDate.getText().toString().trim();
        if(!TextUtils.isEmpty(balanceDateText) && !balanceDateText.equalsIgnoreCase("Ultima fecha actualizacion")) {
            outState.putString(EXTRA_BIP_BALANCE, txtBipBalance.getText().toString());
            outState.putString(EXTRA_BALANCE_DATE, balanceDateText);
            Toast.makeText(this, "SAVED", Toast.LENGTH_SHORT).show();
        }*/
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }


}
