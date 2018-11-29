package com.newaswan.seven;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;

public class DetailActivity extends AppCompatActivity {
    static boolean isInitialized = false;
    private static String TAG = "DetailActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        final ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo currentNetworkInfo = connectivityManager.getActiveNetworkInfo();
        try{
            if(!isInitialized){
                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                isInitialized = true;
            }else {
                Log.d(TAG,"Already Initialized");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        final Typeface alyoum = Typeface.createFromAsset(getAssets(), "fonts/alyoum.ttf");
        final Typeface agency = Typeface.createFromAsset(getAssets(), "fonts/annahar-bold-ar.otf");
        final Typeface boahmed = Typeface.createFromAsset(getAssets(), "fonts/boahmed-alhour-ar.ttf");

        TextView textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        TextView textViewShortDesc = (TextView) findViewById(R.id.textViewShortDesc);
        TextView textViewRating = (TextView)findViewById(R.id.textViewRating);
        TextView textViewPrice = (TextView)findViewById(R.id.textViewPrice);
        TextView textViewLongDesc =(TextView)findViewById(R.id.textViewLongDesc) ;
        TextView textLiq = (TextView)findViewById(R.id.textLiq);
        ImageView imageView =(ImageView)findViewById(R.id.imageView) ;
        textViewTitle.setTypeface(boahmed);
        textViewShortDesc.setTypeface(alyoum);
        textViewLongDesc.setTypeface(alyoum);
        Intent intent = getIntent();
        Book book = intent.getParcelableExtra("Book");
        textViewTitle.setText((CharSequence) book.getTitle());
        textViewShortDesc.setText((CharSequence) book.getShortdesc());
        textViewRating.setText((CharSequence) book.getRating());
        textViewPrice.setText((CharSequence) book.getPrice());
        textViewLongDesc.setText((CharSequence) book.getLongdesc());
        textLiq.setText((CharSequence) book.getLiqo());

        Glide.with(this).load(book.getImg()).into(imageView);
        final Toast toast = Toast.makeText(this, "جاري تحميل المعلومات", Toast.LENGTH_LONG);
        toast.show();
    }
}
