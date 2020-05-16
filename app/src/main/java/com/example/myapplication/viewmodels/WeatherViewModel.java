package com.example.myapplication.viewmodels;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.data.WeatherClient;
import com.example.myapplication.pojo.WeatherModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherViewModel extends ViewModel {

    public MutableLiveData<WeatherModel> ProcessingImagesMutableLiveData = new MutableLiveData<>() ;


    public void getWeatherDataFromApi(){
        WeatherClient.getINSTANCE().getWeather().enqueue(new Callback<WeatherModel>() {
            @Override
            public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {
                ProcessingImagesMutableLiveData.setValue(response.body());
            }
            @Override
            public void onFailure(Call<WeatherModel> call, Throwable t) {
            }
        });

    }
}
