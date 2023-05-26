package com.example.baigiamasisdarbas.fxControllers.adminUi.adminAds;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.baigiamasisdarbas.R;
import com.example.baigiamasisdarbas.dbControllers.AdController;
import com.example.baigiamasisdarbas.dbControllers.OnGetDataListener;
import com.example.baigiamasisdarbas.ds.Ad;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class AdminAdInformationFragment extends Fragment {

    private EditText date, apartment, company, title, description;
    private Button delete, update;
    AdController adController = new AdController();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_ad_information, container, false);
        date = view.findViewById(R.id.adminAdDate);
        apartment = view.findViewById(R.id.adminAdAddress);
        company = view.findViewById(R.id.adminAdCompany);
        title = view.findViewById(R.id.adTitleEditText);
        description = view.findViewById(R.id.adDescriptionEditText);
        delete = view.findViewById(R.id.deleteAdButton);
        update = view.findViewById(R.id.updateAdButton);
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String rightNow = currentDate + " " + currentTime;
        Bundle bundle = getArguments();
        date.setText(bundle.getString("date"));
        apartment.setText(bundle.getString("apartment"));
        company.setText(bundle.getString("companyName"));
        title.setText(bundle.getString("title"));
        description.setText(bundle.getString("description"));

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adController.deleteAd(bundle.getString("id"), new OnGetDataListener<String>() {
                    @Override
                    public void onSuccess(String data) {
                        showMessage("Naujiena sėkmingai ištrinta");
                        Navigation.findNavController(v).navigate(R.id.action_nav_admin_ad_information_to_nav_admin_ad);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        showMessage("Nepavyko ištrinti naujienos \n Klaida: " + e);
                    }
                });


            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ad ad = new Ad(bundle.getString("id"),company.getText().toString(),title.getText().toString(), description.getText().toString(), date.getText().toString(), apartment.getText().toString(),   rightNow);
                if (validation(title.getText().toString(), description.getText().toString())) {
                    adController.updateAd(ad, new OnGetDataListener<String>() {
                        @Override
                        public void onSuccess(String data) {
                            showMessage("Naujiena sėkmingai atnaujinta");
                            Navigation.findNavController(v).navigate(R.id.action_nav_admin_ad_information_to_nav_admin_ad);
                        }

                        @Override
                        public void onFailure(Exception e) {
                            showMessage("Nepavyko atnaujinti naujienos informacijos \n Klaida: " + e);
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

    private boolean validation(String titleTxt, String descriptionTxt) {

        if (TextUtils.isEmpty(titleTxt)) {
            title.setError("Įveskite naujienos pavadinimą");
            return false;
        }
        if (TextUtils.isEmpty(descriptionTxt)) {
            description.setError("Įveskite aprašymą");
            return false;
        }


        return true;
    }

}