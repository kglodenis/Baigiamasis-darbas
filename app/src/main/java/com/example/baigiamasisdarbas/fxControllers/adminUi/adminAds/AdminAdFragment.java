package com.example.baigiamasisdarbas.fxControllers.adminUi.adminAds;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.baigiamasisdarbas.R;
import com.example.baigiamasisdarbas.dbControllers.AdController;
import com.example.baigiamasisdarbas.dbControllers.OnGetDataListener;
import com.example.baigiamasisdarbas.ds.Ad;
import com.example.baigiamasisdarbas.fxControllers.adminUi.adminApartments.AdminApartmentInformationFragment;

import java.util.ArrayList;


public class AdminAdFragment extends Fragment {
    private ArrayList<Ad> adList;
    private RecyclerView recyclerView;

    private AdminAdRecyclerAdapter.RecyclerViewClickListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_ad, container, false);
        recyclerView = view.findViewById(R.id.adminAdRecyclerView);
        adList = new ArrayList<>();
        setAdapter();
        Button btn = view.findViewById(R.id.createAd);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_admin_ad_to_nav_admin_ad_create);
            }
        });

        return view;
    }

    private void setAdapter() {
        AdController adController = new AdController();
        adController.getAllAds(new OnGetDataListener<ArrayList<Ad>>() {
            @Override
            public void onSuccess(ArrayList<Ad> data) {
                setOnClickListener(data);
                AdminAdRecyclerAdapter adapter = new AdminAdRecyclerAdapter(listener, data);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d("Klaida", "Klaidos pranešimas: " + e);
            }
        });
    }

    private void setOnClickListener(ArrayList<Ad> data) {
        listener = new AdminAdRecyclerAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                AdminApartmentInformationFragment adminApartmentInformationFragment = new AdminApartmentInformationFragment();
                Bundle bundle = new Bundle();
                bundle.putString("id", data.get(position).getId());
                bundle.putString("apartment", data.get(position).getApartment());
                bundle.putString("companyName", data.get(position).getCompanyName());
                bundle.putString("date", data.get(position).getDate());
                bundle.putString("title", data.get(position).getTitle());
                bundle.putString("description", data.get(position).getDescription());
                adminApartmentInformationFragment.setArguments(bundle);
                Navigation.findNavController(v).navigate(R.id.action_nav_admin_ad_to_nav_admin_ad_information, bundle);

            }
        };
    }
}