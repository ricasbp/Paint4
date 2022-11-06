package com.example.paint4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


public class MapActivity extends AppCompatActivity {

    // How to Get Current Location On Google Map in Android Studio | CurrentLocationOnMap | Android Coding
    // https://www.youtube.com/watch?v=p0PoKEPI65o&ab_channel=AndroidCoding
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;

    // How to Draw Polyline On Google Map in Android Studio | DrawPolyline | Android Coding
    //https://www.youtube.com/watch?v=rEhYTd4T__c&ab_channel=AndroidCoding

    Polyline polyline = null;
    boolean startGettingLocation = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //Assign Variables
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);

        // Initialize fused location
        client = LocationServices.getFusedLocationProviderClient(this);

        //Check Permission. Permission_Granted == 0
        if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //When permission granted
            //Call method:

            //Initialize task location
            Task<Location> task = client.getLastLocation();
            Log.d("LogDaTuga", "task = " + task);

            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    //When success
                    if (location != null) {
                        //Sync map
                        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady( GoogleMap googleMap) {
                                //Initialize latlng
                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                                // Create marker options
                                MarkerOptions options = new MarkerOptions().position(latLng)
                                        .title("I am there");
                                //Zoom map
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

                                Log.d("LogDaTuga", "task = " + options);

                                //Add marker on map
                                googleMap.addMarker(options);
                            }
                        });
                    }
                }
            });
        } else { //Permission Denied
            //Request permission
            ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }


        Button btn_StartStopDrawing = findViewById(R.id.button_StartStopDrawing);

        if (btn_StartStopDrawing != null){
            btn_StartStopDrawing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("LogDaTuga", "clickedButton ! ");

                    // Corre um método que está sempre a correr enquanto tivermos o map aberto
                    // e vai desenhando a polyline num mapa.

                    // Learn how to track Location by adding a LocationListener DIDNT WORK
                    // https://www.youtube.com/watch?v=5fjwDx8fOMk&ab_channel=Padakoo

                    startDrawing();
                }
            });
        }
    }

    public void startDrawing(){
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //TextView locationInfo = findViewById(R.id.locationInfo);
                //locationInfo.setText(location.toString());
                //Log.d("LogDaTuga", "locationInfo = " + locationInfo + "/n   " +
                //        "location.toString() = " + location);
                Log.d("LogDaTuga", "latitude = "+ location.getLatitude());

            }
        };

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Permission is not there
        }
        // Provider, Time 1000 = 1 milisecond, 1 distance between last location and current location , and listener
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 1F, (android.location.LocationListener) locationListener);
    }


    // I run this code when i dont have permission (?)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //When permission granted
                //Call method

                //Check Permission. Permission_Granted == 0
                if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //When permission granted
                    //Call method:

                    //Initialize task location
                    Task<Location> task = client.getLastLocation();
                    task.addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            //When success
                            if (location != null) {
                                //Sync map
                                supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                                    @Override
                                    public void onMapReady(GoogleMap googleMap) {
                                        //Initialize latlng
                                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                        // Create marker options
                                        MarkerOptions options = new MarkerOptions().position(latLng)
                                                .title("I am there");
                                        //Zoom map
                                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                                        //Add marker on map
                                        googleMap.addMarker(options);
                                    }
                                });
                            }
                        }
                    });
                } else { //Permission Denied
                    //Request permission
                    ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        }
    }
}