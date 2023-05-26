package com.example.baigiamasisdarbas.fxControllers.ui.homePage;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baigiamasisdarbas.R;
import com.example.baigiamasisdarbas.dbControllers.ApartmentsController;
import com.example.baigiamasisdarbas.dbControllers.OnGetDataListener;
import com.example.baigiamasisdarbas.dbControllers.RequestController;
import com.example.baigiamasisdarbas.ds.ApartmentBuilding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class HomePageFragment extends Fragment {

    ImageView notifications, request, ad, settings, forum;
    TextView monday, tuesday, wednesday, thursday, friday, saturday, sunday, responsiblePerson;

    ApartmentsController apartmentsController = new ApartmentsController();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        notifications = view.findViewById(R.id.notificationsImage);
        monday = view.findViewById(R.id.mondayViewField);
        tuesday = view.findViewById(R.id.tuesdayViewField);
        wednesday = view.findViewById(R.id.wednesdayViewField);
        thursday = view.findViewById(R.id.thursdayViewField);
        friday = view.findViewById(R.id.fridayViewField);
        saturday = view.findViewById(R.id.saturdayViewField);
        sunday = view.findViewById(R.id.sundayViewField);
        responsiblePerson = view.findViewById(R.id.responsiblePersonTextView);
        request = view.findViewById(R.id.requestImage);
        ad = view.findViewById(R.id.adImage);
        settings = view.findViewById(R.id.userSettingsImage);
        forum = view.findViewById(R.id.forumImage);

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_home_page_to_nav_requests);
            }
        });

        ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_home_page_to_nav_ad);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_home_page_to_nav_user_profile);
            }
        });

        forum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_home_page_to_nav_forum);
            }
        });
        apartmentsController.getUserApartment(new OnGetDataListener<ApartmentBuilding>() {
            @Override
            public void onSuccess(ApartmentBuilding data) {
                data.getCleaningSchedule().get(0);
                monday.setText(data.getCleaningSchedule().get(0));
                tuesday.setText(data.getCleaningSchedule().get(1));
                wednesday.setText(data.getCleaningSchedule().get(2));
                thursday.setText(data.getCleaningSchedule().get(3));
                friday.setText(data.getCleaningSchedule().get(4));
                saturday.setText(data.getCleaningSchedule().get(5));
                sunday.setText(data.getCleaningSchedule().get(6));
                responsiblePerson.append(data.getResponsiblePersonNumber() + " (" + data.getResponsiblePersonNameAndSurname() + ")");
            }
            @Override
            public void onFailure(Exception e) {
                Log.d("Klaida", "Klaidos pranešimas: " + e);

            }
        });
        SharedPreferences sharedPreferences;
        sharedPreferences = getContext().getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        if (sharedPreferences.getAll().isEmpty()) {
            notifications.setImageResource(R.mipmap.notification_empty_icon_foreground);
        } else {
            notifications.setImageResource(R.mipmap.notification_not_empty_icon_foreground);
        }
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_home_page_to_nav_notification);
            }
        });
        BarChart barChart = view.findViewById(R.id.requestsBarChart);
        RequestController requestController = new RequestController();
        requestController.getRequestsCountByType(new OnGetDataListener<ArrayList<BarEntry>>() {
            @Override
            public void onSuccess(ArrayList<BarEntry> data) {
                BarDataSet barDataSet = new BarDataSet(data, "");
                barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                barDataSet.setValueTextColor(Color.BLACK);
                barDataSet.setValueTextSize(16f);
                barDataSet.setBarBorderWidth(0.5f);

                BarData barData = new BarData(barDataSet);
                barChart.setFitBars(true);
                barChart.setData(barData);
                barChart.getDescription().setText("");
                barChart.animateY(2000);
                barChart.setExtraOffsets(10f, 10f, 10f, 10f);
                XAxis xAxis = barChart.getXAxis();
                xAxis.setDrawLabels(true);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{"", "Vanduo", "Elektra", "Švara", "Šildymas", "Triukšmas", "Kita"}));

                YAxis yAxis = barChart.getAxisLeft();
                yAxis.setAxisMinimum(0f);
                yAxis.setGranularity(1f);
                yAxis.setDrawGridLines(true);

            }

            @Override
            public void onFailure(Exception e) {
                Log.d("Klaida", "Klaidos pranešimas: " + e);
            }
        });

        return view;

    }

    private void showMessage(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }
}