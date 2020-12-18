package com.example.beermanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "BeerManagerLogs";

    BottomNavigationView btm_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(TAG,"On create " + getLocalClassName());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Outils");

        getFragment(new Outils());

        btm_view = findViewById(R.id.bottom_view);

        //lambda
        btm_view.setOnNavigationItemSelectedListener(item -> {
            if(item.getItemId() == R.id.outils){
                getSupportActionBar().setTitle("Outils");// replace fragment cf getFragment implémenté plus bas
                getFragment(new Outils());

                Toast.makeText(MainActivity.this, "Outils selectionné", Toast.LENGTH_SHORT).show(); //PopUp du de l'icone selectionnée
            }else if (item.getItemId() == R.id.brasserie){
                getSupportActionBar().setTitle("Brasserie");// replace fragment cf getFragment implémenté plus bas
                getFragment(new Brasserie());

                Toast.makeText(MainActivity.this, "Brasserie selectionné", Toast.LENGTH_SHORT).show(); //PopUp du de l'icone selectionnée
            }else if (item.getItemId() == R.id.ressources){
                getSupportActionBar().setTitle("Ressources");
                getFragment(new Ressources()); // replace fragment cf getFragment implémenté plus bas

                Toast.makeText(MainActivity.this, "Ressources selectionné", Toast.LENGTH_SHORT).show(); //PopUp du de l'icone selectionnée
            }
            return false;
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"On start " + getLocalClassName());
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG,"On stop " + getLocalClassName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"On destroy " + getLocalClassName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"On resume " + getLocalClassName());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG,"On restart " + getLocalClassName());
    }

    private void getFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment); // replace et pas add pour éviter les crash
        fragmentTransaction.commit();

    }
}