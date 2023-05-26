package com.example.baigiamasisdarbas.fxControllers.ui.ads;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.baigiamasisdarbas.R;
import com.example.baigiamasisdarbas.dbControllers.AdController;
import com.example.baigiamasisdarbas.dbControllers.OnGetDataListener;
import com.example.baigiamasisdarbas.ds.Ad;

import java.util.ArrayList;

public class AdFragment extends Fragment {

    private RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ad, container, false);
        recyclerView = view.findViewById(R.id.adRecyclerView);
        ArrayList<Ad> adList = new ArrayList<>();
        setAdapter();
        return view;
    }

    private void setAdapter() {
        AdController adController = new AdController();
        adController.getAllAds(new OnGetDataListener<ArrayList<Ad>>() {
            @Override
            public void onSuccess(ArrayList<Ad> data) {
                AdRecyclerAdapter adapter = new AdRecyclerAdapter(data);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onFailure(Exception e) {
                showMessage("Įvyko klaida \n Klaida: " + e);
            }
        });
    }

    private void showMessage(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }
}