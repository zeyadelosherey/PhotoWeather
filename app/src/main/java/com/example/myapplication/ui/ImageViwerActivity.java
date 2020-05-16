package com.example.myapplication.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.pojo.WeatherModel;
import com.example.myapplication.utils.ScaleListener;
import com.example.myapplication.utils.WriteOnImage;
import com.example.myapplication.viewmodels.HistoryImagesViewModel;
import com.example.myapplication.viewmodels.WeatherViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class ImageViwerActivity extends AppCompatActivity {

    ImageView imageResult;
    Button faceBookButton , twitterButton;
    File processingImage;
    Uri imageSource;
    WriteOnImage writeOnImage;
    WeatherViewModel weatherViewModel;
    LinearLayout loadingLayout ;
    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;
    TextView internetConnectionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viwer_page);
        init();


        twitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (processingImage.getAbsolutePath()!=null && processingImage.getAbsoluteFile().getAbsolutePath()!="") {
                    shareOnASpecificAPP("com.twitter.android");
                }
            }
        });

        faceBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (processingImage.getAbsolutePath()!=null && processingImage.getAbsoluteFile().getAbsolutePath()!="") {
                    shareOnASpecificAPP("com.facebook.katana");
                }
            }
        });

      /* check if the path of the image from processing image or not
          -if the image passing to this activity not from "PhotoWeather" path which contains all processing images
           draw Weather Data on image and save it
          -if the image path contains "PhotoWeather" path show it without any processing
        */
        if(!processingImage.getAbsolutePath().contains("PhotoWeather")){
            weatherViewModel.getWeatherDataFromApi();
            weatherViewModel.ProcessingImagesMutableLiveData.observe(this, new Observer<WeatherModel>() {
                @Override
                public void onChanged(WeatherModel weatherModel) {
                    if (weatherModel != null){
                        WeatherData weatherData = new WeatherData(weatherModel.getName(),weatherModel.getWeather().get(0).getMain() , weatherModel.getMain().getTemp().toString());
                        writeOnImage =  new WriteOnImage(ImageViwerActivity.this , ImageViwerActivity.this , imageResult ,processingImage , imageSource , weatherData );
                        processingImage = writeOnImage.getProcessingImage();
                        createView();
                    }else{
                        loadingLayout.setVisibility(View.VISIBLE);
                    }
                }
            });
        }else{
            createView();
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        scaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }



    void init(){
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        imageResult = findViewById(R.id.result);
        faceBookButton = findViewById(R.id.facebook);
        twitterButton = findViewById(R.id.twitter);
        loadingLayout = findViewById(R.id.loadingLayout);
        internetConnectionText = findViewById(R.id.internet_connection);
        Intent intent = getIntent();
        processingImage = (File) intent.getExtras().get("image");
        imageSource = Uri.fromFile(processingImage);
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener(imageResult , mScaleFactor));
        weatherViewModel =  new ViewModelProvider(this).get(WeatherViewModel.class);
        if (!isNetworkConnected()){
            Toast.makeText(ImageViwerActivity.this, "Please check your internet connection", Toast.LENGTH_LONG).show();
            loadingLayout.setVisibility(View.GONE);
            internetConnectionText.setVisibility(View.VISIBLE);
        }



    }

    // checked if the app is installed
    public boolean isAppInstalled(String packageName ) {
        try {
            getApplicationContext().getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    // share through a specific app
    public void shareOnASpecificAPP(String packageName){

        if (isAppInstalled(packageName)) {
            final Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/jpg");
            final File photoFile = new File(processingImage.getAbsolutePath());
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(photoFile));
            shareIntent.setPackage(packageName);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(shareIntent, "Share"));
        } else {
            Toast.makeText(getApplicationContext(), "Please Install the app! ", Toast.LENGTH_LONG).show();
        }

    }

    // change state of the view
    public void createView(){
        loadingLayout.setVisibility(View.GONE);
        twitterButton.setVisibility(View.VISIBLE);
        faceBookButton.setVisibility(View.VISIBLE);
        imageResult.setVisibility(View.VISIBLE);
        Glide.with(ImageViwerActivity.this)
                .load(processingImage)
                .into(imageResult);
    }


    //Check if there is internet connection return a value true or false
    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }



    public class WeatherData{
        private String cityName , tempreture , weatherCondition;
        public WeatherData(String cityName , String weatherCondition,String  tempreture){
            this.cityName = cityName;
            this.tempreture = tempreture;
            this.weatherCondition = weatherCondition;
        }
        public String getWeatherCondition() {
            return weatherCondition;
        }
        public String getCityName() {
            return cityName;
        }
        public String getTempreture() {
            return tempreture;
        }
    }
}
