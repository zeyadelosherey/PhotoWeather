package com.example.myapplication.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.example.myapplication.R;
import com.example.myapplication.adapters.HistoryAdapter;
import com.example.myapplication.viewmodels.HistoryImagesViewModel;

import java.io.File;
import java.util.List;

public class ImageHistoryActivity extends AppCompatActivity {
    HistoryImagesViewModel historyImagesViewModel ;
    Toolbar toolbar ;
    RecyclerView recyclerView;
    HistoryAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_processing);

        // Initialize
        init();

        //load all images from
        historyImagesViewModel.getAllProcessingImages();

        //observe on live data
        historyImagesViewModel.ProcessingImagesMutableLiveData.observe(this, new Observer<List<File>>() {
            @Override
            public void onChanged(List<File> files) {
                if (files == null) {
                    Toast.makeText(getApplicationContext(), "There is no images", Toast.LENGTH_LONG).show();
                } else {
                    adapter.setList(files);
                }
            }
        });

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    void init(){
        recyclerView = findViewById(R.id.historyRecyclerView);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        historyImagesViewModel = new ViewModelProvider(this).get(HistoryImagesViewModel.class);
        adapter = new HistoryAdapter();
        recyclerView.setLayoutManager(new GridLayoutManager(this , 3));
        recyclerView.setAdapter(adapter);
    }
}
