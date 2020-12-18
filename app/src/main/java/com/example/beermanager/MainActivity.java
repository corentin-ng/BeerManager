package com.example.beermanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView btm_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Outils");

        getFragment(new Outils());

        btm_view = findViewById(R.id.bottom_view);
        btm_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.outils){
                    getSupportActionBar().setTitle("Outils");
                    getFragment(new Outils());

                    Toast.makeText(MainActivity.this, "Outils selectionné", Toast.LENGTH_SHORT).show();
                }else if (item.getItemId() == R.id.brasserie){
                    getSupportActionBar().setTitle("Brasserie");
                    getFragment(new Brasserie());

                    Toast.makeText(MainActivity.this, "Brasserie selectionné", Toast.LENGTH_SHORT).show();
                }else if (item.getItemId() == R.id.ressources){
                    getSupportActionBar().setTitle("Ressources");
                    getFragment(new Ressources());

                    Toast.makeText(MainActivity.this, "Ressources selectionné", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    private void getFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment); // replace et pas add pour éviter les crash
        fragmentTransaction.commit();

    }
}