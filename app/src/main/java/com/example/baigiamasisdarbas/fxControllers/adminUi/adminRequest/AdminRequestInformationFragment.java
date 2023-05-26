package com.example.baigiamasisdarbas.fxControllers.adminUi.adminRequest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baigiamasisdarbas.R;
import com.example.baigiamasisdarbas.dbControllers.OnGetDataListener;
import com.example.baigiamasisdarbas.dbControllers.RequestController;
import com.example.baigiamasisdarbas.ds.RequestStatus;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class AdminRequestInformationFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private TextView dateField, senderField, phoneNumberField, addressField, descriptionField, file;
    private Spinner statusSpinner;
    private Button updateRequestBtn;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private ImageView imageView;
    private Button closePopUp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_request_information, container, false);


        dateField = view.findViewById(R.id.requestDate);
        senderField = view.findViewById(R.id.senderTextView);
        addressField = view.findViewById(R.id.addressTextView);
        descriptionField = view.findViewById(R.id.descriptionTextView);
        statusSpinner = view.findViewById(R.id.statusSpinner);
        phoneNumberField = view.findViewById(R.id.phoneNumberTxt);
        updateRequestBtn = view.findViewById(R.id.updateRequestButton);
        file = view.findViewById(R.id.attachmentField);
        Bundle bundle = getArguments();
        dateField.setText(bundle.getString("requestDate"));
        senderField.setText(bundle.getString("fullName"));
        addressField.setText(bundle.getString("address"));
        descriptionField.setText(bundle.getString("requestDescription"));
        phoneNumberField.setText(bundle.getString("phoneNumber"));
        String requestId = bundle.getString("requestId");
        String requestStatus = bundle.getString("requestStatus");
        String attachmentUrl = bundle.getString("Url");
        if (attachmentUrl == null) {
            file.setText("Priedų nėra");
        } else {
            file.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openPhoto(attachmentUrl);
                }
            });
        }

        RequestStatus[] statuses = RequestStatus.values();
        ArrayAdapter<RequestStatus> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, statuses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);
        statusSpinner.setOnItemSelectedListener(this);
        if (requestStatus != null) {
            int spinnerPosition = adapter.getPosition(RequestStatus.valueOf(requestStatus));
            statusSpinner.setSelection(spinnerPosition);
        }
        updateRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestController requestController = new RequestController();
                String status = statusSpinner.getSelectedItem().toString();
                String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                String rightNow = currentDate + " " + currentTime;
                requestController.updateRequestStatusById(requestId, status, new OnGetDataListener<String>() {
                    @Override
                    public void onSuccess(String data) {
                        showMessage("Užklausa sėkmingai atnaujinta");
                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(requestId, rightNow);
                        editor.apply();
                        Navigation.findNavController(v).navigate(R.id.action_nav_admin_request_information_to_nav_admin_request);
                    }
                    @Override
                    public void onFailure(Exception e) {
                        showMessage("Įvyko klaida \n Klaida: " + e);
                    }
                });
            }
        });


        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void openPhoto(String url) {
        dialogBuilder = new AlertDialog.Builder(getContext());
        final View popUpView = getLayoutInflater().inflate(R.layout.request_image_popup, null);
        imageView = popUpView.findViewById(R.id.popUpImageView);
        closePopUp = popUpView.findViewById(R.id.closePopUpButton);
        Picasso.get().load(url).into(imageView);
        dialogBuilder.setView(popUpView);
        dialog = dialogBuilder.create();
        dialog.show();
        closePopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void showMessage(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }


}