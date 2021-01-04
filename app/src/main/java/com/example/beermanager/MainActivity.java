package com.example.beermanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "BeerManager";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG, "onCreate - " + getLocalClassName());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart - " + getLocalClassName());
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop - " + getLocalClassName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy - " + getLocalClassName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause - " + getLocalClassName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume - " + getLocalClassName());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart - " + getLocalClassName());
    }

    public void onClickRecipeActivity(View v) {
        Log.i(TAG, "on Click Recipe - " + getLocalClassName());

        Intent intent = new Intent(this, RecipeActivity.class);
//        intent.putExtra("postedValue", valueToPost);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

}