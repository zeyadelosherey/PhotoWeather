package com.example.myapplication.data;

import com.example.myapplication.pojo.WeatherModel;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherClient {
    private static final String BASE_URL = "http://dataservice.accuweather.com/";
    private final String APPID= "92f57e85108d5a7e53459b8b96a290c2";
    private WeatherInterface weatherInterface;
    private static WeatherClient INSTANCE;

    public WeatherClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        weatherInterface = retrofit.create(WeatherInterface.class);
    }

    public static WeatherClient getINSTANCE() {
        if (null == INSTANCE){
            INSTANCE = new WeatherClient();
        }
        return INSTANCE;
    }

    public Call<WeatherModel> getWeather(){
        return weatherInterface.getWeatherData("360630", APPID);
    }
}



