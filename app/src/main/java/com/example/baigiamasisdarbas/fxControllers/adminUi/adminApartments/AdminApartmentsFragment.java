package com.example.baigiamasisdarbas.fxControllers.adminUi.adminApartments;

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
import com.example.baigiamasisdarbas.dbControllers.ApartmentsController;
import com.example.baigiamasisdarbas.dbControllers.OnGetDataListener;
import com.example.baigiamasisdarbas.ds.ApartmentBuilding;

import java.util.ArrayList;


public class AdminApartmentsFragment extends Fragment {
    private ArrayList<ApartmentBuilding> apartmentsList;

    private RecyclerView recyclerView;
    private AdminApartmentRecyclerAdapter.AdminApartmentRecyclerViewClickListener listener;
    private Button newApartment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_admin_apartments, container, false);
            recyclerView = view.findViewById(R.id.adminApartmentRecyclerView);
            apartmentsList = new ArrayList<>();
            newApartment = view.findViewById(R.id.addNewApartmentButton);
            newApartment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(v).navigate(R.id.action_nav_admin_apartments_to_nav_add_apartment);
                }
            });
            setAdapter();
        return view;
    }

    private void setAdapter() {
        ApartmentsController apartmentsController = new ApartmentsController();
        apartmentsController.getAllApartments(new OnGetDataListener<ArrayList<ApartmentBuilding>>() {
            @Override
            public void onSuccess(ArrayList<ApartmentBuilding> data) {
                setOnClickListener(data);
                AdminApartmentRecyclerAdapter adapter = new AdminApartmentRecyclerAdapter(listener,data);
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

    private void setOnClickListener(ArrayList<ApartmentBuilding> data)
    {
        listener = new AdminApartmentRecyclerAdapter.AdminApartmentRecyclerViewClickListener()
        {
            @Override
            public void onClick(View v, int position)
            {
                AdminApartmentInformationFragment apartmentInformationFragment = new AdminApartmentInformationFragment();
                Bundle bundle = new Bundle();
                bundle.putString("address", data.get(position).getAddress());
                bundle.putString("nameAndSurname", data.get(position).getResponsiblePersonNameAndSurname());
                bundle.putString("number", data.get(position).getResponsiblePersonNumber());
                bundle.putInt("floors", data.get(position).getFloors());
                if(data.get(position).getAllRequests()!=null)
                {
                    bundle.putInt("requests", data.get(position).getAllRequests().size());

                }else{
                    bundle.putInt("requests", 0);
                }
                if(data.get(position).getAllResidents()!=null)
                {
                    bundle.putInt("residents", data.get(position).getAllResidents().size());

                }
                else {
                    bundle.putInt("residents", 0);


                }
                bundle.putStringArrayList("schedule", data.get(position).getCleaningSchedule());
                bundle.putString("registrationCode", data.get(position).getRegistrationCode());
                apartmentInformationFragment.setArguments(bundle);
                Navigation.findNavController(v).navigate(R.id.action_nav_admin_apartments_to_nav_admin_apartments_information, bundle);
            }
        };
    }
}