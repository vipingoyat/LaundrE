package com.azhar.laundry.view.cucibasah;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.azhar.laundry.R;
import com.azhar.laundry.view.history.HistoryActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QrActivity extends AppCompatActivity {

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_qr );

//        mTextview = (TextView)findViewById(R.id.etText);

        TextView mTextview = findViewById(R.id.etText);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView mTextview2 = findViewById(R.id.etText2);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView mTextview3 = findViewById(R.id.etText3);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView mTextview4 = findViewById(R.id.etText4);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView mTextview5 = findViewById(R.id.etText5);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView mTextview6 = findViewById(R.id.etText6);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView mTextview7 = findViewById(R.id.etText7);

        mTextview.setText(getIntent().getStringExtra("mytext1"));
        String text1 = mTextview.getText().toString();
        mTextview2.setText(getIntent().getStringExtra("mytext2"));
        String text2 = mTextview2.getText().toString();
        mTextview3.setText(getIntent().getStringExtra("mytext3"));
        String text3 = mTextview3.getText().toString();
        mTextview4.setText(getIntent().getStringExtra("mytext4"));
        String text4 = mTextview4.getText().toString();
        mTextview5.setText(getIntent().getStringExtra("mytext5"));
        String text5 = mTextview5.getText().toString();
        mTextview6.setText(getIntent().getStringExtra("mytext6"));
        String text6 = mTextview6.getText().toString();
        mTextview7.setText(getIntent().getStringExtra("mytext7"));
        String text7 = mTextview7.getText().toString();


        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView tv11 = findViewById(R.id.tvText1);
        String tv1 = tv11.getText().toString().trim();
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView tv12 = findViewById(R.id.tvText2);
        String tv2 = tv12.getText().toString().trim();
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView tv13 = findViewById(R.id.tvText3);
        String tv3 = tv13.getText().toString().trim();
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView tv14 = findViewById(R.id.tvText4);
        String tv4 = tv14.getText().toString().trim();
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView tv15 = findViewById(R.id.tvText5);
        String tv5 = tv15.getText().toString().trim();
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView tv16 = findViewById(R.id.tvText6);
        String tv6 = tv16.getText().toString().trim();
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView tv17 = findViewById(R.id.tvText7);
        String tv7 = tv17.getText().toString().trim();

        String text = tv1+text1+"\n"+tv2+text2+"\n"+tv3+text3+"\n"+tv4+text4+"\n"+tv5+text5+"\n"+tv6+text6+"\n"+tv7+text7;








//        String mytext = getIntent().getStringExtra( "mytext" );
        //Button for generating QR code
        Button btnGenerate = findViewById(R.id.btnGenerate);
        //Text will be entered here to generate QR code
//        EditText etText = findViewById(R.id.etText);
//        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText etText2 = findViewById(R.id.etText2);
//        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText etText3 = findViewById(R.id.etText3);
        //ImageView for generated QR code
        ImageView imageCode = findViewById(R.id.imageCode);
        btnGenerate.setOnClickListener(v -> {
            //getting text from input text field.
//            String myText = etText.getText().toString().trim();
//            String myText2 = etText2.getText().toString().trim();
//            String myText3 = etText3.getText().toString().trim();
//            String text = "Bag no.: "+myText+"\n"+"No. of Clothes: "+myText2+"\n"+"Types of clothes: "+myText3+"\n";

            //initializing MultiFormatWriter for QR code
            MultiFormatWriter mWriter = new MultiFormatWriter();

            try {
                //BitMatrix class to encode entered text and set Width & Height
                BitMatrix mMatrix = mWriter.encode(text, BarcodeFormat.QR_CODE, 400,400);
                BarcodeEncoder mEncoder = new BarcodeEncoder();
                Bitmap mBitmap = mEncoder.createBitmap(mMatrix);//creating bitmap of code
                imageCode.setImageBitmap(mBitmap);//Setting generated QR code to imageView
                // to hide the keyboard

                InputMethodManager manager = (InputMethodManager) getSystemService( Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(imageCode.getApplicationWindowToken(), 0);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        });







    }
}



