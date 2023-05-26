package com.example.baigiamasisdarbas.fxControllers.ui.request;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
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
import com.example.baigiamasisdarbas.dbControllers.OnGetDataListener;
import com.example.baigiamasisdarbas.dbControllers.RequestController;
import com.example.baigiamasisdarbas.ds.Request;

import java.util.ArrayList;

public class RequestFragment extends Fragment {
    private ArrayList<Request> requestsList;
    private RecyclerView recyclerView;
    private RequestRecyclerAdapter.RecyclerViewClickListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        requestsList = new ArrayList<>();
        setAdapter();
        Button btn = view.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_requests_to_request_registration);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            super.onResume();
            setAdapter();
        }
    }

    private void setAdapter() {
        RequestController requestController = new RequestController();
        requestController.getAllRequestsByUser(new OnGetDataListener<ArrayList<Request>>() {
            @Override
            public void onSuccess(ArrayList<Request> data) {
                setOnClickListener(data);
                RequestRecyclerAdapter adapter = new RequestRecyclerAdapter(data, listener);
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

    private void setOnClickListener(ArrayList<Request> data) {
        listener = new RequestRecyclerAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                RequestInformationFragment requestInformationFragment = new RequestInformationFragment();
                Bundle bundle = new Bundle();
                bundle.putString("requestDescription", data.get(position).getRequestDescription());
                bundle.putString("requestStatus", data.get(position).getRequestStatus());
                bundle.putString("requestDate", data.get(position).getRegistrationDate());
                bundle.putString("apartment", data.get(position).getApartmentBuilding());
                bundle.putString("phoneNumber", data.get(position).getPhoneNumber());
                bundle.putString("name", data.get(position).getSenderFullName());
                requestInformationFragment.setArguments(bundle);
                Navigation.findNavController(v).navigate(R.id.action_nav_requests_to_nav_requests_information, bundle);


            }
        };
    }
}