package com.example.baigiamasisdarbas.fxControllers.adminUi.adminSurvey;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.baigiamasisdarbas.R;
import com.example.baigiamasisdarbas.dbControllers.ApartmentsController;
import com.example.baigiamasisdarbas.dbControllers.OnGetDataListener;
import com.example.baigiamasisdarbas.dbControllers.SurveyController;
import com.example.baigiamasisdarbas.ds.Survey;

import java.util.ArrayList;

public class AdminSurveyInformationFragment extends Fragment {

    EditText title, description, url;
    ArrayList<String> apartments;
    Spinner apartmentsSpinner;
    Button save, delete;
    SurveyController surveyController = new SurveyController();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_survey_information, container, false);
        title = view.findViewById(R.id.adminSurveyTitle);
        description = view.findViewById(R.id.adminSurveyDescription);
        url = view.findViewById(R.id.adminSurveyUrl);
        save = view.findViewById(R.id.adminSurveyUpdateButton);
        delete = view.findViewById(R.id.deleteSurveyButton);
        apartmentsSpinner = view.findViewById(R.id.apartmentsSpinner);
        Bundle bundle = getArguments();
        url.setText(bundle.getString("url"));
        title.setText(bundle.getString("title"));
        description.setText(bundle.getString("description"));
        ApartmentsController apartmentsController = new ApartmentsController();
        apartmentsController.getAllApartmentsAddresses(new OnGetDataListener<ArrayList<String>>() {
            @Override
            public void onSuccess(ArrayList<String> data) {
                apartments = data;
                ArrayAdapter<String> adp = new ArrayAdapter<String>(getContext(), R.layout.registration_spinner_item, data);
                adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                apartmentsSpinner.setAdapter(adp);
                System.out.println("OPAOPAOPASKDKAPSDOPASD" + bundle.getString("apartment"));
                int spinnerPosition = adp.getPosition(bundle.getString("apartment"));
                apartmentsSpinner.setSelection(spinnerPosition);
            }
            @Override
            public void onFailure(Exception e) {
                Log.d("Klaida", "Klaidos pranešimas: " + e);
            }
        });

        String id = bundle.getString("id");
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation(title.getText().toString(), description.getText().toString(), url.getText().toString())) {
                    Survey survey = new Survey(id, title.getText().toString(), description.getText().toString(), url.getText().toString(), apartmentsSpinner.getSelectedItem().toString());
                    surveyController.updateSurvey(survey, new OnGetDataListener<String>() {
                        @Override
                        public void onSuccess(String data) {
                            showMessage("Apklausa sėkmingai atnaujinta");
                            Navigation.findNavController(v).navigate(R.id.action_nav_admin_survey_information_to_nav_admin_survey);
                        }
                        @Override
                        public void onFailure(Exception e) {
                            showMessage("Įvyko klaida atnaujinant apklausą \n Klaida: " + e);
                        }
                    });
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                surveyController.deleteSurvey(id, new OnGetDataListener<String>() {
                    @Override
                    public void onSuccess(String data) {
                        showMessage("Apklausa sėkmingai ištrinta");
                        Navigation.findNavController(v).navigate(R.id.action_nav_admin_survey_information_to_nav_admin_survey);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        showMessage("Įvyko klaida ištrinant apklausą \n Klaida: " + e);
                    }
                });
            }
        });


        return view;
    }

    private void showMessage(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    private boolean validation(String titleTxt, String descriptionTxt, String urlTxt) {

        if (TextUtils.isEmpty(titleTxt)) {
            title.setError("Įveskite apklausos pavadinimą");
            return false;
        }
        if (TextUtils.isEmpty(descriptionTxt)) {
            description.setError("Įveskite aprašymą");
            return false;
        }
        if (TextUtils.isEmpty(urlTxt)) {
            url.setError("Įveskite nuorodą");
            return false;
        }
        return true;
    }
}