package com.example.paint4;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PaletteFragment extends Fragment {

    // TUTORIAL STUFF
    ImageView imgView;
    TextView mColorValues;
    View mColorViews;

    Bitmap bitmap;


    //GSON Tutorial Part 1 - SIMPLE (DE)SERIALIZATION - Android Studio Tutorial
    //https://www.youtube.com/watch?v=f-kcvxYZrB4&ab_channel=CodinginFlow
    FirebaseFirestore db;
    Gson gson = new Gson();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_palette, container, false);
    }

    // Create Color Picker App In Android Studio With Source Code
    // https://www.youtube.com/watch?v=aR7JKHw2aqg&ab_channel=Edubaba
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
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




        // Code for Save State Button to save the paintings already done
        Button saveButton = (Button) getView().findViewById(R.id.buttonSave);

        if (saveButton != null){
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Get the paintCanvas view model ready
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


                    db = FirebaseFirestore.getInstance();
                    Map<String, Object> hashInfo = new HashMap<>();
                    hashInfo.put("AllPaints", paintCanvas.getAllPaints());



                    Log.d("LogDaTuga", "paintCanvas.getAllPaints().size()= " + paintCanvas.getAllPaints().size());
                    Log.d("LogDaTuga", "paintCanvas.getAllPaths().size()= " + paintCanvas.getAllPaths().size());

                    int i = 0;
                    for (Integer whatever :  paintCanvas.getAllPaints()){ //Size of all the paths

                        //CustomPath cp = paintCanvas.getAllPaths().get(i);
                        //Log.d("LogDaTuga", "cp.getClass()= " + cp.getClass());
                        //for (int j = 0; i <= cp.getActions().size(); j++){
                        //    Log.d("LogDaTuga", "cp.getActions().get(j)= " + cp.getActions().get(j));
                        //}
                        //
                        //Action Move is always the first value

                        String json = gson.toJson(paintCanvas.getAllPaths().get(i));
                        Log.d("LogDaTuga", "json written= " + json);

                        hashInfo.put(Integer.toString(i), json);
                        i++;
                    }
                    i = 0;

                    //Add a new document with a generated ID
                    db.collection("Paint").document("PaintPaths")
                            .set(hashInfo)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("LogDaTuga", "Written!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("LogDaTuga", "Error adding document", e);
                                }
                            });
                    }
            });
        }



        // Code for Load State Button to load the paintings in the dataBase
        Button loadButton = (Button) getView().findViewById(R.id.buttonLoad);

        if (loadButton != null){
            loadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    db = FirebaseFirestore.getInstance();
                    db.collection("Paint")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        ArrayList<Integer> allPaints = new ArrayList<>();
                                        ArrayList<CustomPath> listaDosPaths = new ArrayList<>();

                                        for (QueryDocumentSnapshot document : task.getResult()) { // Get the Data inside the document "PaintPaths"
                                            Log.d("LogDaTuga", document.getId() + " => " + document.getData());
                                            Map<String, Object> hashInfo = document.getData();
                                            for (Object value : hashInfo.values()) { // Add the paths and paints to the right spot


                                                if (value.getClass().equals(ArrayList.class)) {
                                                    try {
                                                        JSONArray colorList = new JSONArray(String.valueOf(value));
                                                        for(int i = 0; i <= colorList.length(); i++){
                                                            allPaints.add((Integer) colorList.get(i));
                                                        }

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                } else {
                                                    Log.d("LogDaTuga", "value.toString().getClass();: " + value.getClass());
                                                    JSONObject json = null;
                                                    try {
                                                        json = new JSONObject(String.valueOf(value));
                                                        Log.d("LogDaTuga", "json2: " +
                                                                json.get("actions"));

                                                        JSONArray actions = (JSONArray) json.get("actions");

                                                        HashMap<Float, Float> mapaCoordenadas = new HashMap<>();
                                                        for (int i = 0; i <= actions.length() - 1; i++) { //Get all the coordinations of an action/path
                                                            Float x = (float) actions.getJSONObject(i).getDouble("x");
                                                            Float y = (float) actions.getJSONObject(i).getDouble("y");
                                                            mapaCoordenadas.put(x, y);
                                                        }
                                                        CustomPath cp = new CustomPath();
                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                            Optional<Float> firstKey = mapaCoordenadas.keySet().stream().findFirst();

                                                            if (firstKey.isPresent()) { // Get the first value of the array. It is the ActionMove
                                                                cp.moveTo(firstKey.get(), (Float) mapaCoordenadas.values().toArray()[0]);
                                                            } else {
                                                                Log.w("LogDaTuga", "Error getting first Value.", task.getException());
                                                            }
                                                        }

                                                        for (float x : mapaCoordenadas.keySet()) {
                                                            float y = mapaCoordenadas.get(x);
                                                            cp.lineTo(x, y);
                                                        }

                                                        listaDosPaths.add(cp);


                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        }
                                        // Get the paintCanvas view model ready
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
                                        paintCanvas.setAllPaths(listaDosPaths);
                                        paintCanvas.setAllPaints(allPaints);

                                    } else {
                                        Log.w("LogDaTuga", "Error getting documents.", task.getException());
                                    }
                                }
                            });
                }
            });
        }


    }
    // Replaces all the " in the json with \" soo we can serialize it correctly
    private String formatJsonToString(Object value) {
        StringBuilder sb = new StringBuilder();
        String json = value.toString();

        int j = 0;
        for(int i = 0; i < json.length(); i++){
            sb.append(json.charAt(i));
            if(json.charAt(i) == '\"'){
                sb.insert(i + j, '\\'); //This inserts a "\"
                j++;
            }
        }
        return sb.toString();
    }
}