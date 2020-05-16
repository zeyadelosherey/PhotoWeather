package com.example.myapplication.data;

import com.example.myapplication.pojo.WeatherModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface WeatherInterface {
    @GET("https://api.openweathermap.org/data/2.5/weather")
    public Call<WeatherModel> getWeatherData(@Query("id") String id , @Query("APPID") String APPID);
}

