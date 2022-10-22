package com.example.paint4;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class PaletteFragment extends Fragment {

    // TUTORIAL STUFF
    ImageView imgView;
    TextView mColorValues;
    View mColorViews;

    Bitmap bitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_palette, container, false);
    }

    // Create Color Picker App In Android Studio With Source Code
    // https://www.youtube.com/watch?v=aR7JKHw2aqg&ab_channel=Edubaba
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // findViewById in Fragment:
        // https://stackoverflow.com/questions/6495898/findviewbyid-in-fragment
        // DIDNT WORK

        // Solved✔: findViewById in Fragment and onViewCreated(view) not Android Activity
        // https://www.youtube.com/watch?v=XPdUkpkmuFM&ab_channel=CodeDocuDeveloperC%23AspNetAngular

        imgView = (ImageView) getView().findViewById(R.id.colorPicker);
        mColorValues = (TextView) getView().findViewById(R.id.displayValues);
        mColorViews = getView().findViewById(R.id.displayColors);

        imgView.setDrawingCacheEnabled(true);
        imgView.buildDrawingCache(true);
        String hex = new String();

        imgView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN || motionEvent.getAction() == MotionEvent.ACTION_MOVE){
                    bitmap = imgView.getDrawingCache();
                    int pixels = bitmap.getPixel((int) motionEvent.getX(), (int) motionEvent.getY());
                    int r = Color.red(pixels);
                    int g = Color.green(pixels);
                    int b = Color.blue(pixels);



                    String hex = "#" + Integer.toHexString(pixels);
                    mColorViews.setBackgroundColor(Color.rgb(r,g,b));
                    mColorValues.setText("RGB: " + r + ", " + g + ", " + b + "\nHEX:" + hex);


                    SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);


                    PaintCanvas ps = sharedViewModel.getPaintCanvas();
                    ps.setColor(pixels);
                    sharedViewModel.setPaintCanvas(ps);
                }
                return true;
            }
        });
    }
}