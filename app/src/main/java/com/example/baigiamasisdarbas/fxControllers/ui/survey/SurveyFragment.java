package com.example.baigiamasisdarbas.fxControllers.ui.survey;

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

import com.example.baigiamasisdarbas.R;
import com.example.baigiamasisdarbas.dbControllers.OnGetDataListener;
import com.example.baigiamasisdarbas.dbControllers.SurveyController;
import com.example.baigiamasisdarbas.ds.Survey;

import java.util.ArrayList;


public class SurveyFragment extends Fragment {

    private ArrayList<Survey> surveysList;
    private RecyclerView recyclerView;
    private SurveyRecyclerAdapter.RecyclerViewClickListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_survey, container, false);
        recyclerView = view.findViewById(R.id.surveyRecyclerView);
        surveysList = new ArrayList<>();
        setAdapter();
        return view;
    }

    private void setAdapter() {
        SurveyController surveyController = new SurveyController();
        surveyController.getAllSurveys(new OnGetDataListener<ArrayList<Survey>>() {
            @Override
            public void onSuccess(ArrayList<Survey> data) {
                setOnClickListener(data);
                SurveyRecyclerAdapter adapter = new SurveyRecyclerAdapter(listener, data);
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
        listener = new SurveyRecyclerAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                SurveyWebFragment surveyWebFragment = new SurveyWebFragment();
                Bundle bundle = new Bundle();
                bundle.putString("url", data.get(position).getUrl());
                surveyWebFragment.setArguments(bundle);
                Navigation.findNavController(v).navigate(R.id.action_nav_survey_to_nav_survey_web, bundle);
            }
        };
    }

}