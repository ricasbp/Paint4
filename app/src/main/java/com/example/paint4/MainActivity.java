package com.example.paint4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "MESSAGE";


    ImageView imgView;

    // Create Option Menu on start
    // Options Menu with Sub Items - Android Studio Tutorial
    // https://www.youtube.com/watch?v=oh4YOj9VkVE&ab_channel=CodinginFlow
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflter = getMenuInflater();
        menuInflter.inflate(R.menu.example_menu, menu);
        return true;
    }
    //When items from the options menu are clicked
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_Settings:
                Intent intent = new Intent(this, About.class);
                //EditText editText = (EditText) findViewById(R.id.editTextTextPersonName);
                //String message = editText.getText().toString();
                intent.putExtra(EXTRA_MESSAGE, "whatever");
                startActivityForResult(intent,1);
            case R.id.menu_About:
                Intent intent2 = new Intent(this, About.class);
                //EditText editText = (EditText) findViewById(R.id.editTextTextPersonName);
                //String message = editText.getText().toString();
                intent2.putExtra(EXTRA_MESSAGE, "whatever");
                startActivityForResult(intent2,1);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Tutorial to do this
        //https://www.youtube.com/watch?v=PiExmkR3aps&ab_channel=ZeeshanAcademy

        // Code for Palette Button to switch to the Palette Screen
        // TODO O que é esta VIEW?
        // TODO ResiZe landScape & Fix Quando está no ecra da palette ele nao consegue ir para o landscape idk why

        Button btnPalette = findViewById(R.id.button2);

        if (btnPalette != null){
            btnPalette.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fragmentManager = getSupportFragmentManager();

                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView, PaletteFragment.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("name") // name can be null
                            .commit();
                }
            });
        }

        // Code for Canva Button to switch to the Canva Screen
        Button btnCanva = findViewById(R.id.button3);

        if (btnCanva != null){
            btnCanva.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fragmentManager = getSupportFragmentManager();

                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView, CanvasFragment.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("name") // name can be null
                            .commit();
                }
            });
        }

        // Meter os aspect ratios corretos para landscape
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            getSupportActionBar().hide();

            //imgView = (ImageView) findViewById(R.id.colorPicker);
            //imgView.setScaleType(ImageView.ScaleType.CENTER);
        } else {
            // In portrait
        }
    }
}