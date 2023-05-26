package com.example.baigiamasisdarbas.fxControllers.adminUi.adminApartments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baigiamasisdarbas.R;
import com.example.baigiamasisdarbas.dbControllers.ApartmentsController;
import com.example.baigiamasisdarbas.dbControllers.OnGetDataListener;
import com.example.baigiamasisdarbas.ds.ApartmentBuilding;

import java.util.ArrayList;
import java.util.List;

public class AdminApartmentInformationFragment extends Fragment {
    private TextView requestsField, residentsField;
    private EditText addressField, responsibleField, numberField, registrationCode, floorField, mondayField, tuesdayField, wednesdayField, thursdayField, fridayField, saturdayField, sundayField;

    private List<String> requests;
    private List<String> schedule;
    private Button updateBtn, deleteApartment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_apartment_information, container, false);
        addressField = view.findViewById(R.id.addressTextView);
        responsibleField = view.findViewById(R.id.responsablePersonTextView);
        numberField = view.findViewById(R.id.numberTextView);
        floorField = view.findViewById(R.id.floorTextView);
        registrationCode = view.findViewById(R.id.registrationCodeText);
        requestsField = view.findViewById(R.id.requestsTextView);
        residentsField = view.findViewById(R.id.residentsTextView);
        mondayField = view.findViewById(R.id.mondayEditField);
        tuesdayField = view.findViewById(R.id.tuesdayEditField);
        wednesdayField = view.findViewById(R.id.wednesdayEditField);
        thursdayField = view.findViewById(R.id.thursdayEditField);
        fridayField = view.findViewById(R.id.fridayEditField);
        saturdayField = view.findViewById(R.id.saturdayEditField);
        sundayField = view.findViewById(R.id.sundayEditField);
        updateBtn = view.findViewById(R.id.updateApartment);
        deleteApartment = view.findViewById(R.id.deleteApartment);


        Bundle bundle = getArguments();
        addressField.setText(bundle.getString("address"));
        responsibleField.setText(bundle.getString("nameAndSurname"));
        numberField.setText(bundle.getString("number"));
        floorField.setText(Integer.toString(bundle.getInt("floors")));
        requestsField.setText(Integer.toString(bundle.getInt("requests")));
        residentsField.setText(Integer.toString(bundle.getInt("residents")));
        schedule = bundle.getStringArrayList("schedule");
        registrationCode.setText(bundle.getString("registrationCode"));

        if (!schedule.isEmpty()) {

            mondayField.setText(schedule.get(0));
            tuesdayField.setText(schedule.get(1));
            wednesdayField.setText(schedule.get(2));
            thursdayField.setText(schedule.get(3));
            fridayField.setText(schedule.get(4));
            saturdayField.setText(schedule.get(5));
            sundayField.setText(schedule.get(6));
        }
        deleteApartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApartmentsController apartmentsController = new ApartmentsController();
                if (bundle.getInt("residents") == 0) {
                    apartmentsController.deleteApartment(bundle.getString("address"), new OnGetDataListener<String>() {
                        @Override
                        public void onSuccess(String data) {
                            showMessage("Daugiabutis sėkmingai ištrintas");
                            Navigation.findNavController(v).navigate(R.id.action_nav_admin_apartments_information_to_nav_admin_apartments);
                        }

                        @Override
                        public void onFailure(Exception e) {

                        }
                    });
                } else {
                    showMessage("Daugiabutis neištrintas, nes yra prisiregistravusių gyventojų");
                }
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> scheduleUpdate = new ArrayList<>();
                scheduleUpdate.add(mondayField.getText().toString());
                scheduleUpdate.add(tuesdayField.getText().toString());
                scheduleUpdate.add(wednesdayField.getText().toString());
                scheduleUpdate.add(thursdayField.getText().toString());
                scheduleUpdate.add(fridayField.getText().toString());
                scheduleUpdate.add(saturdayField.getText().toString());
                scheduleUpdate.add(sundayField.getText().toString());
                ApartmentBuilding apartmentBuilding = new ApartmentBuilding(addressField.getText().toString(), Integer.parseInt(floorField.getText().toString()), responsibleField.getText().toString(), numberField.getText().toString(), registrationCode.getText().toString(), scheduleUpdate);
                ApartmentsController apartmentsController = new ApartmentsController();
                if (validation(addressField.getText().toString(), Integer.parseInt(floorField.getText().toString()), responsibleField.getText().toString(), numberField.getText().toString(), registrationCode.getText().toString())) {
                    apartmentsController.updateApartment(apartmentBuilding, bundle.getString("address"), new OnGetDataListener<String>() {
                        @Override
                        public void onSuccess(String data) {
                            showMessage("Daugiabutis sėkmingai atnaujintas");
                            Navigation.findNavController(v).navigate(R.id.action_nav_admin_apartments_information_to_nav_admin_apartments);
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