package com.example.myapplication.viewmodels;

import android.os.Environment;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class HistoryImagesViewModel extends ViewModel {
    public MutableLiveData<List<File>> ProcessingImagesMutableLiveData = new MutableLiveData<>() ;
    public void getAllProcessingImages(){
        String path = Environment.getExternalStorageDirectory().toString()+"/PhotoWeather";

        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        try {
            List<File> files = Arrays.asList(directory.listFiles());
            ProcessingImagesMutableLiveData.setValue(files);
        }catch (Exception e){
            ProcessingImagesMutableLiveData.setValue(null);
        }

    }
}
