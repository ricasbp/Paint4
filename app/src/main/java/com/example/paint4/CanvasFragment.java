package com.example.paint4;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class CanvasFragment extends Fragment {

    // Android Studio Phone Accelerometer Shake Sensor and Line Graph:
    // https://www.youtube.com/watch?v=zUzZ67grYt8&ab_channel=Programmingw%2FProfessorSluiter
    private SensorManager sensorManager;
    private Sensor mAccelerometer;
    private double accelerationCurrentValue;
    private double accelerationPreviousValue;

    // Sensor for the ambient light TODO
    private Sensor sensor2;

    // Sensor for the inclination
    private Sensor sensor3;



    private SensorEventListener sensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];


            //Log.d("LogDaTuga", "changeInAcceleration = " + sensorEvent.sensor.getType());
            //TYPE_GYROSCOPE_UNCALIBRATED checks for shakes and deletes what has been previously drawn
            if(sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE_UNCALIBRATED){

                accelerationCurrentValue = Math.sqrt((x * x + y * y + z * z));
                double changeInAcceleration = Math.abs(accelerationCurrentValue - accelerationPreviousValue);
                //Log.d("LogDaTuga", "Number = " + changeInAcceleration);
                accelerationPreviousValue = accelerationCurrentValue;


                // If you shake agressivelly it deletes the drawing
                if(changeInAcceleration > 3){
                    SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);


                    //Log.d("LogDaTuga", "SHAKEEEEEEEEEEEEEEEEEEEEEEEEE = ");
                    PaintCanvas ps = sharedViewModel.getPaintCanvas();
                    ps.deleteDrawing();
                    sharedViewModel.setPaintCanvas(ps);

                }
            }

            //TYPE_MAGNETIC_FIELD checks inclination and changes the backgroun color
            if(sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD ) {
                float x2 = sensorEvent.values[0];
                float y2 = sensorEvent.values[1];
                float z2 = sensorEvent.values[2];

                //Log.d("LogDaTuga", "x = " + x2);
                //Log.d("LogDaTuga", "y = " + y2);
                //Log.d("LogDaTuga", "z = " + z2);

                if(Math.abs(z2) >= 25){
                    //Log.d("LogDaTuga", "Estou deitado na vertical= " + Math.abs(z2));
                    SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

                    //Log.d("LogDaTuga", "SHAKEEEEEEEEEEEEEEEEEEEEEEEEE ");
                    PaintCanvas ps = sharedViewModel.getPaintCanvas();
                    ps.changeBackgroundBlue();
                    sharedViewModel.setPaintCanvas(ps);
                }


                //Log.d("LogDaTuga", "----------------");


            }


        }


        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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


        //Sensor Stuff:
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE_UNCALIBRATED);

        sensor3 = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);


        //view.setContentView(paintCanvas);// adds the created view to the screen
        // Inflate the layout for this fragment
        return paintCanvas;
    }



    public void onResume(){
        super.onResume();
        sensorManager.registerListener(sensorEventListener , mAccelerometer, sensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorEventListener , sensor3, sensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onPause(){
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
        sensorManager.unregisterListener(sensorEventListener);

    }


}