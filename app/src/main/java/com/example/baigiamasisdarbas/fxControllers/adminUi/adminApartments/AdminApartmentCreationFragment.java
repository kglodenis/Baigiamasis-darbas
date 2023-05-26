package com.example.baigiamasisdarbas.fxControllers.adminUi.adminApartments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baigiamasisdarbas.R;
import com.example.baigiamasisdarbas.dbControllers.ApartmentsController;
import com.example.baigiamasisdarbas.dbControllers.OnGetDataListener;
import com.example.baigiamasisdarbas.ds.ApartmentBuilding;

import java.util.ArrayList;

public class AdminApartmentCreationFragment extends Fragment {

    TextView addressField, responsibleField, numberField, floorField, registrationCode, mondayField, tuesdayField, wednesdayField, thursdayField, fridayField, saturdayField, sundayField;
    Button addButton;
    ArrayList<String> schedule = new ArrayList<>();
    ApartmentsController apartmentsController = new ApartmentsController();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_apartment_creation, container, false);
        addressField = view.findViewById(R.id.addressField);
        responsibleField = view.findViewById(R.id.responsibleField);
        numberField = view.findViewById(R.id.numberField);
        floorField = view.findViewById(R.id.floorsField);
        addButton = view.findViewById(R.id.addButton);
        registrationCode = view.findViewById(R.id.regCodeEdit);
        mondayField = view.findViewById(R.id.mondayField);
        tuesdayField = view.findViewById(R.id.tuesdayField);
        wednesdayField = view.findViewById(R.id.wednesdayField);
        thursdayField = view.findViewById(R.id.thursdayField);
        fridayField = view.findViewById(R.id.fridayField);
        saturdayField = view.findViewById(R.id.saturdayField);
        sundayField = view.findViewById(R.id.sundayField);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validation(addressField.getText().toString(), Integer.parseInt(floorField.getText().toString()), responsibleField.getText().toString(), numberField.getText().toString(), registrationCode.getText().toString())) {
                    ApartmentBuilding apartmentBuilding = new ApartmentBuilding();
                    if (mondayField.getText().toString().isEmpty()) {
                        apartmentBuilding = new ApartmentBuilding(addressField.getText().toString(), Integer.parseInt(floorField.getText().toString()), responsibleField.getText().toString(), numberField.getText().toString(), registrationCode.getText().toString());
                    } else {
                        schedule.add(mondayField.getText().toString());
                        schedule.add(tuesdayField.getText().toString());
                        schedule.add(wednesdayField.getText().toString());
                        schedule.add(thursdayField.getText().toString());
                        schedule.add(fridayField.getText().toString());
                        schedule.add(saturdayField.getText().toString());
                        schedule.add(sundayField.getText().toString());
                        apartmentBuilding = new ApartmentBuilding(addressField.getText().toString(), Integer.parseInt(floorField.getText().toString()), responsibleField.getText().toString(), numberField.getText().toString(), registrationCode.getText().toString(), schedule);


                    }
                    apartmentsController.createApartment(apartmentBuilding, new OnGetDataListener<String>() {
                        @Override
                        public void onSuccess(String data) {
                            showMessage("Daugiabutis sėkmingai sukurtas");
                            Navigation.findNavController(v).navigate(R.id.action_nav_add_apartment_to_nav_admin_apartments);
                        }

                        @Override
                        public void onFailure(Exception e) {
                            showMessage("Įvyko klaida \n Klaida: " + e);

                        }
                    });
                }
            }
        });
        return view;
    }

    private void showMessage(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    private boolean validation(String addressTxt, Integer floorTxt, String responsibleTxt, String numberTxt, String regCodeTxt) {

        if (TextUtils.isEmpty(addressTxt)) {
            addressField.setError("Įveskite adresą");
            return false;
        }
        if (TextUtils.isEmpty(responsibleTxt)) {
            responsibleField.setError("Įveskite atsakingą asmenį");
            return false;
        }
        if (TextUtils.isEmpty(numberTxt)) {
            numberField.setError("Įveskite tel. nr.");
            return false;
        }
        if (TextUtils.isEmpty(floorTxt.toString())) {
            floorField.setError("Įveskite aukštų skaičių");
            return false;
        }
        if (TextUtils.isEmpty(regCodeTxt.toString())) {
            registrationCode.setError("Įveskite registracijos kodą");
            return false;
        }


        return true;
    }
}