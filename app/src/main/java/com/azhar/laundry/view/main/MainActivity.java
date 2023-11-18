package com.azhar.laundry.view.main;

import static com.firebase.ui.auth.ui.phone.SubmitConfirmationCodeFragment.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.azhar.laundry.models.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.azhar.laundry.R;
import com.azhar.laundry.view.cucibasah.CuciBasahActivity;
import com.azhar.laundry.view.cucibasah.QrActivity;
import com.azhar.laundry.view.dryclean.DryCleanActivity;
import com.azhar.laundry.view.history.HistoryActivity;
import com.azhar.laundry.view.ironing.IroningActivity;
import com.azhar.laundry.view.premiumwash.PremiumWashActivity;
import com.azhar.laundry.viewmodel.MainViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import im.delight.android.location.SimpleLocation;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {


    int REQ_PERMISSION = 100;
    double strCurrentLatitude;
    double strCurrentLongitude;
    String strCurrentLocation;

    GoogleSignInClient googleSignInClient;
    GoogleMap mapsView;
    SimpleLocation simpleLocation;
    ProgressDialog progressDialog;
    MainViewModel mainViewModel;
    MenuAdapter menuAdapter;

    MainAdapter mainAdapter;
    ModelMenu modelMenu;
    RecyclerView rvMenu, rvRekomendasi;
    LinearLayout layoutHistory;
    List<ModelMenu> modelMenuList = new ArrayList<>();


    @SuppressLint({"SetTextI18n", "RestrictedApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


        TextView mTextview = findViewById(R.id.name_history);
        TextView Name = findViewById(R.id.name);
//        mTextview.setText(getIntent().getStringExtra("mytext"));
        mTextview.setText( currentUser.getDisplayName().toString()+", Checkout your order history!");
        Name.setText( currentUser.getDisplayName() );


        setStatusbar();
        setPermission();
        setLocation();
        setInitLayout();
        setMenu();
//        getLocationViewModel();

    }

    private void setLocation() {
        simpleLocation = new SimpleLocation(this);

        if (!simpleLocation.hasLocationEnabled()) {
            SimpleLocation.openSettings(this);
        }

        //get location
        strCurrentLatitude = simpleLocation.getLatitude();
        strCurrentLongitude = simpleLocation.getLongitude();

        //set location lat long
        strCurrentLocation = strCurrentLatitude + "," + strCurrentLongitude;
    }

    private void setInitLayout() {
        rvMenu = findViewById(R.id.rvMenu);
        rvRekomendasi = findViewById(R.id.rvRekomendasi);
        layoutHistory = findViewById(R.id.layoutHistory);

    Button sign_out = findViewById( R.id.sign_out_btn );

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait…");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("displaying the location");

        rvMenu.setLayoutManager(new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false));
        rvMenu.setHasFixedSize(true);

        mainAdapter = new MainAdapter(this);
        rvRekomendasi.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvRekomendasi.setAdapter(mainAdapter);
        rvRekomendasi.setHasFixedSize(true);

        layoutHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });


//        sign_out.setOnClickListener(v -> {
//            FirebaseAuth.getInstance().signOut();
////            finish();
////            System.exit( 0 );
//
//
//
//        });

    }







    private void setPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.mapsView = googleMap;

        //viewmodel
//        getLocationViewModel();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQ_PERMISSION && resultCode == RESULT_OK) {
//
//            load viewmodel
//            getLocationViewModel();
//        }
    }

    private void setStatusbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void setMenu() {
        modelMenu = new ModelMenu("Wet Wash", R.drawable.ic_cuci_basah);
        modelMenuList.add(modelMenu);
        modelMenu = new ModelMenu("Dry Cleaning", R.drawable.ic_dry_cleaning);
        modelMenuList.add(modelMenu);
        modelMenu = new ModelMenu("Premium Wash", R.drawable.ic_premium_wash);
        modelMenuList.add(modelMenu);
        modelMenu = new ModelMenu("Iron", R.drawable.ic_setrika);
        modelMenuList.add(modelMenu);

        menuAdapter = new MenuAdapter(this, modelMenuList);
        rvMenu.setAdapter(menuAdapter);

        menuAdapter.setOnItemClickListener(modelMenu -> {
            if (modelMenu.getTvTitle().equals("Wet Wash")) {
                Intent intent = new Intent(new Intent(MainActivity.this, CuciBasahActivity.class));
                intent.putExtra(CuciBasahActivity.DATA_TITLE, modelMenu.getTvTitle());
                startActivity(intent);
            } else if (modelMenu.getTvTitle().equals("Dry Cleaning")) {
                Intent intent = new Intent(new Intent(MainActivity.this, DryCleanActivity.class));
                intent.putExtra(DryCleanActivity.DATA_TITLE, modelMenu.getTvTitle());
                startActivity(intent);
            } else if (modelMenu.getTvTitle().equals("Premium Wash")) {
                Intent intent = new Intent(new Intent(MainActivity.this, PremiumWashActivity.class));
                intent.putExtra(PremiumWashActivity.DATA_TITLE, modelMenu.getTvTitle());
                startActivity(intent);
            } else if (modelMenu.getTvTitle().equals("Iron")) {
                Intent intent = new Intent(new Intent(MainActivity.this, IroningActivity.class));
                intent.putExtra(IroningActivity.DATA_TITLE, modelMenu.getTvTitle());
                startActivity(intent);
            }
        });
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        if (on) {
            layoutParams.flags |= bits;
        } else {
            layoutParams.flags &= ~bits;
        }
        window.setAttributes(layoutParams);
    }

//    private void getLocationViewModel() {
//        mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
//        mainViewModel.setMarkerLocation(strCurrentLocation);
//        progressDialog.show();
//        mainViewModel.getMarkerLocation().observe(this, modelResults -> {
//            if (modelResults.size() != 0) {
//                mainAdapter.setLocationAdapter(modelResults);
//                progressDialog.dismiss();
//            } else {
//                progressDialog.show();
//            }
//            progressDialog.dismiss();
//        });
//
//    }

}