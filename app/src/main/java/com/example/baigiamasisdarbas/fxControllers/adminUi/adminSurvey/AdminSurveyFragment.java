package com.example.baigiamasisdarbas.fxControllers.adminUi.adminSurvey;

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
import com.example.baigiamasisdarbas.dbControllers.SurveyController;
import com.example.baigiamasisdarbas.ds.Survey;

import java.util.ArrayList;


public class AdminSurveyFragment extends Fragment {

    private ArrayList<Survey> surveysList;
    private RecyclerView recyclerView;
    private Button createButton;
    private AdminSurveyRecyclerAdapter.RecyclerViewClickListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_survey, container, false);
        recyclerView = view.findViewById(R.id.adminSurveyRecyclerView);
        createButton = view.findViewById(R.id.createSurvey);
        surveysList = new ArrayList<>();
        setAdapter();
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_admin_survey_to_nav_admin_survey_creation);
            }
        });

        return view;
    }

    private void setAdapter() {
        SurveyController surveyController = new SurveyController();
        surveyController.getAllSurveys(new OnGetDataListener<ArrayList<Survey>>() {
            @Override
            public void onSuccess(ArrayList<Survey> data) {
                setOnClickListener(data);
                AdminSurveyRecyclerAdapter adapter = new AdminSurveyRecyclerAdapter(listener, data);
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

    private void setOnClickListener(ArrayList<Survey> data) {
        listener = new AdminSurveyRecyclerAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                AdminSurveyInformationFragment adminSurveyInformationFragment = new AdminSurveyInformationFragment();
                Bundle bundle = new Bundle();
                bundle.putString("id", data.get(position).getId());
                bundle.putString("title", data.get(position).getTitle());
                bundle.putString("description", data.get(position).getDescription());
                bundle.putString("url", data.get(position).getUrl());
                bundle.putString("apartment", data.get(position).getApartment());
                adminSurveyInformationFragment.setArguments(bundle);
                Navigation.findNavController(v).navigate(R.id.action_nav_admin_survey_to_nav_admin_survey_information, bundle);
            }
        };
    }
}