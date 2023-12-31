package com.azhar.laundry.view.cucibasah;

import static io.grpc.InternalStatus.MESSAGE_KEY;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.azhar.laundry.R;
import com.azhar.laundry.utils.FunctionHelper;
import com.azhar.laundry.viewmodel.AddDataViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import im.delight.android.location.SimpleLocation;

public class CuciBasahActivity extends AppCompatActivity {

    
    public static final String DATA_TITLE = "TITLE";
    int hargaKaos = 1, hargaCelana = 1, hargaJaket = 1, hargaSprei = 1, hargaKarpet = 1;
    int itemCount1 = 0, itemCount2 = 0, itemCount3 = 0, itemCount4 = 0, itemCount5 = 0;
    int countKaos, countCelana, countJaket, countSprei, countKarpet, totalItems, totalPrice;
    String strTitle, strCurrentLatLong;
    double strCurrentLatitude;
    double strCurrentLongitude;
    SimpleLocation simpleLocation;
    AddDataViewModel addDataViewModel;
    Button btnCheckout;
    ImageView imageAdd1, imageAdd2, imageAdd3, imageAdd4, imageAdd5,
            imageMinus1, imageMinus2, imageMinus3, imageMinus4, imageMinus5;
    TextView tvTitle, tvInfo, tvJumlahBarang, tvTotalPrice, tvKaos, tvCelana, tvJaket, tvSprei, tvKarpet,
            tvPriceKaos, tvPriceCelana, tvPriceJaket, tvPriceSprei, tvPriceKarpet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laundry);



//        setLocation();
        setStatusbar();
        setInitLayout();
        setDataKaos();
        setDataCelana();
        setDataJaket();
        setDataSprei();
        setDataKarpet();
        setInputData();
//        getCurrentLocation();


    }

//    private void setLocation() {
//        simpleLocation = new SimpleLocation(this);
//
//        if (!simpleLocation.hasLocationEnabled()) {
//            SimpleLocation.openSettings(this);
//        }
//
//        //get location
//        strCurrentLatitude = simpleLocation.getLatitude();
//        strCurrentLongitude = simpleLocation.getLongitude();
//
//        //set location lat long
//        strCurrentLatLong = strCurrentLatitude + "," + strCurrentLongitude;
//    }

    private void setStatusbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void setInitLayout() {
        tvTitle = findViewById(R.id.tvTitle);
        tvInfo = findViewById(R.id.tvInfo);

        tvJumlahBarang = findViewById(R.id.tvJumlahBarang);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);

        tvKaos = findViewById(R.id.tvKaos);
        tvCelana = findViewById(R.id.tvCelana);
        tvJaket = findViewById(R.id.tvJaket);
        tvSprei = findViewById(R.id.tvSprei);
        tvKarpet = findViewById(R.id.tvKarpet);
        tvPriceKaos = findViewById(R.id.tvPriceKaos);
        tvPriceCelana = findViewById(R.id.tvPriceCelana);
        tvPriceJaket = findViewById(R.id.tvPriceJaket);
        tvPriceSprei = findViewById(R.id.tvPriceSprei);
        tvPriceKarpet = findViewById(R.id.tvPriceKarpet);

        imageAdd1 = findViewById(R.id.imageAdd1);
        imageAdd2 = findViewById(R.id.imageAdd2);
        imageAdd3 = findViewById(R.id.imageAdd3);
        imageAdd4 = findViewById(R.id.imageAdd4);
        imageAdd5 = findViewById(R.id.imageAdd5);
        imageMinus1 = findViewById(R.id.imageMinus1);
        imageMinus2 = findViewById(R.id.imageMinus2);
        imageMinus3 = findViewById(R.id.imageMinus3);
        imageMinus4 = findViewById(R.id.imageMinus4);
        imageMinus5 = findViewById(R.id.imageMinus5);

        btnCheckout = findViewById(R.id.btnCheckout);

        strTitle = getIntent().getExtras().getString(DATA_TITLE);
        if (strTitle != null) {
            tvTitle.setText(strTitle);
        }

//        addDataViewModel = new ViewModelProvider(this, ViewModelProvider
//                .AndroidViewModelFactory
//                .getInstance(this.getApplication()))
//                .get(AddDataViewModel.class);

        tvJumlahBarang.setText("0 items");
        tvTotalPrice.setText("Rs 0");
        tvInfo.setText("Washing wet is the process of washing ordinary clothing using water and detergent.");
    }

    private void setDataKaos() {
        tvKaos.setText(FunctionHelper.rupiahFormat(hargaKaos));
        imageAdd1.setOnClickListener(v -> {
            itemCount1 = itemCount1 + 1;
            String str1 = Integer.toString(itemCount1);
            tvPriceKaos.setText(str1);
            countKaos = hargaKaos * itemCount1;
            setTotalPrice();
        });

        imageMinus1.setOnClickListener(v -> {
            if (itemCount1 > 0) {
                itemCount1 = itemCount1 - 1;
                String str1 = Integer.toString(itemCount1);
                tvPriceKaos.setText(str1);
            }
            countKaos = hargaKaos * itemCount1;
            setTotalPrice();
        });
    }

    private void setDataCelana() {
        tvCelana.setText(FunctionHelper.rupiahFormat(hargaCelana));
        imageAdd2.setOnClickListener(v -> {
            itemCount2 = itemCount2 + 1;
            String str2 = Integer.toString(itemCount2);
            tvPriceCelana.setText(str2);
            countCelana = hargaCelana * itemCount2;
            setTotalPrice();
        });

        imageMinus2.setOnClickListener(v -> {
            if (itemCount2 > 0) {
                itemCount2 = itemCount2 - 1;
                String str2 = Integer.toString(itemCount2);
                tvPriceCelana.setText(str2);
            }
            countCelana = hargaCelana * itemCount2;
            setTotalPrice();
        });
    }

    private void setDataJaket() {
        tvJaket.setText(FunctionHelper.rupiahFormat(hargaJaket));
        imageAdd3.setOnClickListener(v -> {
            itemCount3 = itemCount3 + 1;
            String str3 = Integer.toString(itemCount3);
            tvPriceJaket.setText(str3);
            countJaket = hargaJaket * itemCount3;
            setTotalPrice();
        });

        imageMinus3.setOnClickListener(v -> {
            if (itemCount3 > 0) {
                itemCount3 = itemCount3 - 1;
                String str3 = Integer.toString(itemCount3);
                tvPriceJaket.setText(str3);
            }
            countJaket = hargaJaket * itemCount3;
            setTotalPrice();
        });
    }

    private void setDataSprei() {
        tvSprei.setText(FunctionHelper.rupiahFormat(hargaSprei));
        imageAdd4.setOnClickListener(v -> {
            itemCount4 = itemCount4 + 1;
            String str4 = Integer.toString(itemCount4);
            tvPriceSprei.setText(str4);
            countSprei = hargaSprei * itemCount4;
            setTotalPrice();
        });

        imageMinus4.setOnClickListener(v -> {
            if (itemCount4 > 0) {
                itemCount4 = itemCount4 - 1;
                String str4 = Integer.toString(itemCount4);
                tvPriceSprei.setText(str4);
            }
            countSprei = hargaSprei * itemCount4;
            setTotalPrice();
        });
    }

    private void setDataKarpet() {
        tvKarpet.setText(FunctionHelper.rupiahFormat(hargaKarpet));
        imageAdd5.setOnClickListener(v -> {
            itemCount5 = itemCount5 + 1;
            String str5 = Integer.toString(itemCount5);
            tvPriceKarpet.setText(str5);
            countKarpet = hargaKarpet * itemCount5;
            setTotalPrice();
        });

        imageMinus5.setOnClickListener(v -> {
            if (itemCount5 > 0) {
                itemCount5 = itemCount5 - 1;
                String str5 = Integer.toString(itemCount5);
                tvPriceKarpet.setText(str5);
            }
            countKarpet = hargaKarpet * itemCount5;
            setTotalPrice();
        });
    }

    private void setTotalPrice() {
        totalItems = itemCount1 + itemCount2 + itemCount3 + itemCount4 + itemCount5;
        totalPrice = countKaos + countCelana + countJaket + countSprei + countKarpet;

        tvJumlahBarang.setText(totalItems + " items");
        tvTotalPrice.setText(FunctionHelper.rupiahFormat(totalPrice));
    }

//    private void getCurrentLocation() {
//        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//        try {
//            List<Address> addressList = geocoder.getFromLocation(strCurrentLatitude, strCurrentLongitude, 1);
//            if (addressList != null && addressList.size() > 0) {
//                strCurrentLocation = addressList.get(0).getLocality();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private void setInputData() {
        btnCheckout.setOnClickListener(v -> {
            if (totalItems == 0 || totalPrice == 0) {
                Toast.makeText(CuciBasahActivity.this, "Please choose the type of goods!", Toast.LENGTH_SHORT).show();
            } else {

                String text = "Shirt: "+itemCount1+"\n"+"Jeans: "+itemCount2+"\n"+"Jacket: "+itemCount3+"\n"+"BedSheet: "+itemCount4+"\n"+"Carpet: "+itemCount5+"\n"+"Total items: "+totalItems+"\n"+"Price: "+totalPrice;

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference( currentUser.getDisplayName() + "( "+currentUser.getUid()+" )"  );

                myRef.setValue(text);




//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                DatabaseReference myRef = database.getReference("message1");
//
//                myRef.setValue(text);




                String a = String.valueOf( itemCount1 );
                String b = String.valueOf( itemCount2 );
                String c = String.valueOf( itemCount3 );
                String d = String.valueOf( itemCount4 );
                String e = String.valueOf( itemCount5 );
                String f = String.valueOf( totalItems );
                String g = String.valueOf( totalPrice );



                Intent intent= new Intent(this ,QrActivity.class);

                intent.putExtra("mytext1",a);
                intent.putExtra("mytext2",b);
                intent.putExtra("mytext3",c);
                intent.putExtra("mytext4",d);
                intent.putExtra("mytext5",e);
                intent.putExtra("mytext6",f);
                intent.putExtra("mytext7",g);


                startActivity(intent);

//                addDataViewModel.addDataLaundry(strTitle, totalItems, totalPrice);
//                Toast.makeText(CuciBasahActivity.this, "Your order is being processed, check in the history menu", Toast.LENGTH_SHORT).show();
//                finish();
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

}