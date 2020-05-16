package com.example.myapplication.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myapplication.ui.ImageViwerActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class WriteOnImage {
    private Uri imageSource;
    private File processingImage;
    private Context context;
    private Activity activity;
    private ImageView imageResult;
    private  ImageViwerActivity.WeatherData  weatherData;

    public File getProcessingImage() {
        return processingImage;
    }

    public WriteOnImage(Context context , Activity activity , ImageView imageResult , File processingImage , Uri imageSource , ImageViwerActivity.WeatherData weatherData) {
        this.context = context;
        this.activity = activity;
        this.imageResult = imageResult;
        this.processingImage = processingImage;
        this.imageSource = imageSource;
        this.weatherData = weatherData;
        checkAndSaveImage();
    }


    //Check permission then save the image
    private void checkAndSaveImage(){
        int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            if(imageSource != null){
                Bitmap processedBitmap = ProcessingBitmap();
                if(processedBitmap != null){
                    processingImage = saveImage(processedBitmap);
                }else{
                    Toast.makeText(context, "Something wrong in processing!", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(context, "take Image", Toast.LENGTH_LONG).show();
            }
        }
    }

    //Configure Image Sizes and draw text on it
    private Bitmap ProcessingBitmap(){
        Bitmap decodedBitmap = null;
        Bitmap rotatedBitmap = null;
        try {
            decodedBitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(imageSource));
            Matrix matrix = new Matrix();
            matrix.postRotate(90f);
            rotatedBitmap = Bitmap.createBitmap(decodedBitmap, 0, 0, decodedBitmap.getWidth(), decodedBitmap.getHeight(), matrix, false);
            Canvas newCanvas = new Canvas(rotatedBitmap);
            newCanvas.drawBitmap(rotatedBitmap, 0, 0,  null);
            String cityName = weatherData.getCityName();
            String tempreture = weatherData.getTempreture();
            String weatherCondition = weatherData.getWeatherCondition();
            if(cityName != null){
                Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
                paintText.setColor(Color.WHITE);
                paintText.setTextSize(70);
                paintText.setStyle(Paint.Style.FILL);
                paintText.setShadowLayer(7f, 0f, 0f, Color.BLACK);
                Rect rectText = new Rect();
                paintText.getTextBounds(cityName, 0, cityName.length(), rectText);
                newCanvas.drawText(cityName, 40, rectText.height()+50, paintText);
                newCanvas.drawText(tempreture, 40, rectText.height()+150, paintText);
                newCanvas.drawText(weatherCondition, 40, rectText.height()+250, paintText);
            }else{
                Toast.makeText(context, "caption empty!", Toast.LENGTH_LONG).show();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return rotatedBitmap;
    }




    private File saveImage(Bitmap finalBitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        File FolderDirection = new File(root + "/PhotoWeather");
        FolderDirection.mkdirs();
        String fileName = "Image-"+ Calendar.getInstance().getTime() +".jpg";
        File file = new File (FolderDirection, fileName);
        if (file.exists ()) file.delete ();
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;

    }
}
