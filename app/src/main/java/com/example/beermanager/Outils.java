package com.example.beermanager;

import android.content.Context;
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


public class Outils extends Fragment implements View.OnClickListener {

    private static final String TAG = "BeerManagerLogs";

    public Outils() {
        // Required empty public constructor
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "on create view " + getClass().getSimpleName());

        View view = inflater.inflate(R.layout.fragment_outils,container, false);

        Button tauxAlcool = (Button) view.findViewById(R.id.tauxalcool);
        tauxAlcool.setOnClickListener(this);

        Button rendement = (Button) view.findViewById(R.id.recipes);
        rendement.setOnClickListener(this);

        Button densite = (Button) view.findViewById(R.id.densite);
        densite.setOnClickListener(this);

        Button volumes = (Button) view.findViewById(R.id.volumes);
        volumes.setOnClickListener(this);

        Button conversions = (Button) view.findViewById(R.id.conversions);
        conversions.setOnClickListener(this);

        Button embouteillage = (Button) view.findViewById(R.id.embouteillage);
        embouteillage.setOnClickListener(this);


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
        switch(id){
            case R.id.tauxalcool:
                getFragment(new TauxAlcool());
                break;
            case R.id.recipes:
                getFragment(new Rendement());
                break;
            case R.id.densite:
                getFragment(new Densite());
                break;
            case R.id.volumes:
                getFragment(new Volumes());
                break;
            case R.id.conversions:
                getFragment(new Conversions());
                break;
            case R.id.embouteillage:
                getFragment(new Embouteillage());
                break;
        }
    }

    private void getFragment(Fragment fragment) {
        FragmentManager manager =  getParentFragmentManager();;
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment); // replace et pas add pour éviter les crashs
        fragmentTransaction.commit();
    }

}