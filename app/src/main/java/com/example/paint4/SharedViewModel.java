package com.example.paint4;

import android.graphics.Color;
import android.util.Log;

import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private PaintCanvas canvas;

    public SharedViewModel(){
        canvas = null;
    }

    public void setPaintCanvas(PaintCanvas canvas) {
        this.canvas = canvas;
    }

    public PaintCanvas getPaintCanvas() {
        return this.canvas;
    }
}
