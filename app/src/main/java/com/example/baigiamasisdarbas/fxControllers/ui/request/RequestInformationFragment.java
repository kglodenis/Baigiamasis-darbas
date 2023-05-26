package com.example.baigiamasisdarbas.fxControllers.ui.request;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.baigiamasisdarbas.R;


public class RequestInformationFragment extends Fragment {
    private TextView dateField, senderField, addressField, descriptionField, statusField, phoneField;
    Button back;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request_information, container, false);
        dateField = view.findViewById(R.id.requestDate);
        senderField = view.findViewById(R.id.senderTextView);
        addressField = view.findViewById(R.id.addressTextView);
        phoneField = view.findViewById(R.id.phoneTextView);
        descriptionField = view.findViewById(R.id.descriptionTextView);
        statusField = view.findViewById(R.id.statusTextView);
        back = view.findViewById(R.id.requestBackButton);
        Bundle bundle = getArguments();
        dateField.setText(bundle.getString("requestDate"));
        senderField.setText(bundle.getString("name"));
        addressField.setText(bundle.getString("apartment"));
        phoneField.setText(bundle.getString("phoneNumber"));
        descriptionField.setText(bundle.getString("requestDescription"));
        statusField.setText(bundle.getString("requestStatus"));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_requests_information_to_nav_requests);
            }
        });


        return view;
    }
}