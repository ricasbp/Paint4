package com.example.paint4;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class CanvasFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //View view = inflater.inflate(R.layout.fragment_canvas, container, false);
        GestureListener mGestureListener = new GestureListener();
        GestureDetector mGestureDetector = new GestureDetector(getActivity(), mGestureListener);
        mGestureDetector.setIsLongpressEnabled(true);
        mGestureDetector.setOnDoubleTapListener(mGestureListener);


        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        PaintCanvas paintCanvas;
        if(sharedViewModel.getPaintCanvas() != null){
            paintCanvas = sharedViewModel.getPaintCanvas();
        }else{
            paintCanvas = new PaintCanvas(getActivity(), null, mGestureDetector);
            sharedViewModel.setPaintCanvas(paintCanvas);
        }

        mGestureListener.setCanvas(paintCanvas);

        //view.setContentView(paintCanvas);// adds the created view to the screen
        // Inflate the layout for this fragment
        return paintCanvas;
    }


}