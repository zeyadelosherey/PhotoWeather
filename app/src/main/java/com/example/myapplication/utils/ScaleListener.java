package com.example.myapplication.utils;

import android.view.ScaleGestureDetector;
import android.widget.ImageView;

public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
    private float mScaleFactor;
    private ImageView imageView;
    public ScaleListener(ImageView imageView , float mScaleFactor){
        this.imageView = imageView;
        this.mScaleFactor = mScaleFactor;
    }
    @Override
    public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
        mScaleFactor *= scaleGestureDetector.getScaleFactor();
        mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
        imageView.setScaleX(mScaleFactor);
        imageView.setScaleY(mScaleFactor);
        return true;
    }
}
