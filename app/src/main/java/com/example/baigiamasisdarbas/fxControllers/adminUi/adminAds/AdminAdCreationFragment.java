package com.example.baigiamasisdarbas.fxControllers.adminUi.adminAds;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.baigiamasisdarbas.R;
import com.example.baigiamasisdarbas.databinding.FragmentAdminAdCreationBinding;
import com.example.baigiamasisdarbas.dbControllers.AdController;
import com.example.baigiamasisdarbas.dbControllers.ApartmentsController;
import com.example.baigiamasisdarbas.dbControllers.OnGetDataListener;
import com.example.baigiamasisdarbas.ds.Ad;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class AdminAdCreationFragment extends Fragment {

    Spinner apartmentsSpinner;
    FragmentAdminAdCreationBinding binding;
    FirebaseFirestore fStore;
    FirebaseStorage fStorage;
    FirebaseAuth fAuth;
    FirebaseUser currentUser;
    ApartmentsController apartmentsController = new ApartmentsController();

    ArrayList<String> apartments;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdminAdCreationBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        apartmentsSpinner = root.findViewById(R.id.apartmentsSpinner);
        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();
        fAuth = FirebaseAuth.getInstance();
        currentUser = fAuth.getCurrentUser();

        apartmentsController.getAllApartmentsAddresses(new OnGetDataListener<ArrayList<String>>() {
            @Override
            public void onSuccess(ArrayList<String> data) {
                apartments = data;
                ArrayAdapter<String> adp = new ArrayAdapter<String>(getContext(), R.layout.registration_spinner_item, data);
                adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                apartmentsSpinner.setAdapter(adp);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d("Klaida", "Klaidos pranešimas: " + e);

            }
        });

        binding.createNewAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ad ad = new Ad(binding.adCompanyNameTextView.getText().toString(), binding.adTitleTextView.getText().toString(), binding.adDescriptionTextView.getText().toString(), currentDate, apartmentsSpinner.getSelectedItem().toString());
                AdController adController = new AdController();
                if (validation(binding.adCompanyNameTextView.getText().toString(), binding.adTitleTextView.getText().toString(), binding.adDescriptionTextView.getText().toString())) {
                    adController.createAd(ad, new OnGetDataListener<String>() {
                        @Override
                        public void onSuccess(String data) {
                            showMessage("Naujiena sėkmingai sukurta");
                            Navigation.findNavController(v).navigate(R.id.action_nav_admin_ad_create_to_nav_admin_ad);
                        }

                        @Override
                        public void onFailure(Exception e) {
                            showMessage("Nepavyko sukurti naujo pranešimo \n Klaida: " + e);
                        }
                    });
                }
            }
        });

        return root;
    }

    private void showMessage(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    private boolean validation(String companyTxt, String titleTxt, String descriptionTxt) {
        if (TextUtils.isEmpty(companyTxt)) {
            binding.adCompanyNameTextView.setError("Įveskite įmonės pavadinimą");
            return false;
        }
        if (TextUtils.isEmpty(titleTxt)) {
            binding.adTitleTextView.setError("Įveskite naujienos pavadinimą");
            return false;
        }
        if (TextUtils.isEmpty(descriptionTxt)) {
            binding.adDescriptionTextView.setError("Įveskite aprašymą");
            return false;
        }


        return true;
    }


}