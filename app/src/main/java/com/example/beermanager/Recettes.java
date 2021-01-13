package com.example.beermanager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class Recettes extends Fragment implements View.OnClickListener {

    private static final String TAG = "BeerManagerLogs";

    private EditText urlRecetteEditText;

    public Recettes() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "on create view " + getClass().getSimpleName());

        View view = inflater.inflate(R.layout.fragment_recettes,container, false);

        urlRecetteEditText = (EditText) view.findViewById(R.id.urlRecette);

        Button brewdogUrl = (Button) view.findViewById(R.id.brewdogUrl);
        brewdogUrl.setOnClickListener(this);

        Button recipes = (Button) view.findViewById(R.id.recipes);
        recipes.setOnClickListener(this);
            // Inflate the layout for this fragment

        return view;
        }

        @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);
            Log.i(TAG, "on attach " + getClass().getSimpleName());
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            Log.i(TAG, "on activity created " + getClass().getSimpleName());
        }

        @Override
        public void onStart() {
            super.onStart();
            Log.i(TAG, "on start " + getClass().getSimpleName());
        }

        @Override
        public void onResume() {
            super.onResume();
            Log.i(TAG, "on resume " + getClass().getSimpleName());
        }

        @Override
        public void onPause() {
            super.onPause();
            Log.i(TAG, "on pause " + getClass().getSimpleName());
        }

        @Override
        public void onStop() {
            super.onStop();
            Log.i(TAG, "on stop " + getClass().getSimpleName());
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            Log.i(TAG, "destroy view " + getClass().getSimpleName());
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            Log.i(TAG, "on destroy " + getClass().getSimpleName());
        }

        @Override
        public void onDetach() {
            super.onDetach();
            Log.i(TAG, "on detach " + getClass().getSimpleName());
        }


    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick " + getClass().getSimpleName());
        int id = v.getId();
        Intent intent;
        switch(id){
            case R.id.brewdogUrl:
                Log.i(TAG, "on Click open Brewdog - " + getClass().getSimpleName());
                // Le site est HS depuis le 12/01/2021 mais ça marchait avant :'(
                Uri uri = Uri.parse("https://brewdogrecipes.com");
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;

            case R.id.recipes:
                Log.i(TAG, "on Click Recipe - " + getClass().getSimpleName());

                String urlRecette   = urlRecetteEditText.getText().toString();

                Log.i(TAG, "on Click Recipe - getUrl : " + urlRecette);

                Fragment AffichageRecette = new AffichageRecette();
                Bundle args = new Bundle();
                if (urlRecette != "") {
                    args.putString("urlRecette", urlRecette);
                }else {
                    args.putString("urlRecette","fichierDemo");
                }
                AffichageRecette.setArguments(args);

                FragmentManager manager =  getParentFragmentManager();;
                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                fragmentTransaction.replace(R.id.container,AffichageRecette);
                fragmentTransaction.commit();
//
//                    intent = new Intent(this, RecipeActivity.class);
//                    Intent intent = new Intent(view.getContext(), FragmentGreen.class);
//                    intent.putExtra("urlRecette", urlRecette);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                    startActivity(intent);
             break;
        }
 }





    }
