package com.example.baigiamasisdarbas.fxControllers.ui.request;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baigiamasisdarbas.R;
import com.example.baigiamasisdarbas.databinding.FragmentRequestRegistrationBinding;
import com.example.baigiamasisdarbas.dbControllers.OnGetDataListener;
import com.example.baigiamasisdarbas.dbControllers.RequestController;
import com.example.baigiamasisdarbas.dbControllers.UserController;
import com.example.baigiamasisdarbas.ds.Request;
import com.example.baigiamasisdarbas.ds.RequestStatus;
import com.example.baigiamasisdarbas.ds.RequestType;
import com.example.baigiamasisdarbas.ds.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RequestRegistrationFragment extends Fragment {
    Uri uri;
    FirebaseFirestore fStore;
    FirebaseStorage fStorage;
    FirebaseAuth fAuth;
    FirebaseUser currentUser;
    String userId, address, name;
    private ProgressDialog progressDialog;
    Spinner typeSpinner;
    RequestController requestController;
    TextView nameField, addressField, numberField, descriptionField, uploadedPhotoLink;
    Button submit, upload;
    Boolean isPhotoUploaded = false;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private ImageView imageView;
    private Button closePopUp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_registration, container, false);
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String rightNow = currentDate + " " + currentTime;
        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();
        fAuth = FirebaseAuth.getInstance();
        currentUser = fAuth.getCurrentUser();
        userId = currentUser.getUid();
        requestController = new RequestController();
        nameField = view.findViewById(R.id.fullNameField);
        nameField.setKeyListener(null);
        addressField = view.findViewById(R.id.requestAddressField);
        addressField.setKeyListener(null);
        numberField = view.findViewById(R.id.contactPhoneField);
        descriptionField = view.findViewById(R.id.requestDescriptionField);
        uploadedPhotoLink = view.findViewById(R.id.uploadedPhotoLink);
        submit = view.findViewById(R.id.submit);
        upload = view.findViewById(R.id.uploadFile);
        createProgressDialog();
        fillSpinnerWithData(view);
        UserController userController = new UserController();
        userController.getCurrentUser(new OnGetDataListener<User>() {
            @Override
            public void onSuccess(User data) {
                address = data.getApartmentBuilding();
                name = data.getName() + " " + data.getSurname();
                nameField.setText(name);
                addressField.setText(address);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d("Klaida", "Klaidos pranešimas: " + e);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadImage();
                isPhotoUploaded = true;
                uploadedPhotoLink.setText("Atidaryti prisegtą nuotrauką");

            }
        });
        uploadedPhotoLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewDialog(String.valueOf(uri));
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Request request = new Request(descriptionField.getText().toString(), userId, numberField.getText().toString(), rightNow, rightNow, RequestStatus.Užregistruotas, typeSpinner.getSelectedItem().toString());
                if (validation(numberField.getText().toString(), descriptionField.getText().toString())) {
                    if (isPhotoUploaded.equals(false)) {
                        requestController.createRequest(request, new OnGetDataListener<String>() {
                            @Override
                            public void onSuccess(String data) {
                                Navigation.findNavController(v).navigate(R.id.action_request_registration_to_nav_requests);
                                SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(data, request.getModificationDate());
                                editor.apply();
                                showMessage("Užklausa sėkmingai užregistruota");

                            }

                            @Override
                            public void onFailure(Exception e) {
                                showMessage("Nepavyko užregistruoti užklausos \n Klaida: " + e);
                            }
                        });
                    } else {
                        requestController.createRequest(uri, request, new OnGetDataListener<String>() {
                            @Override
                            public void onSuccess(String data) {
                                SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(data, request.getModificationDate());
                                editor.apply();
                                Intent intent = new Intent();
                                requireActivity().setResult(RESULT_OK, intent);
                                requireActivity().setResult(Activity.RESULT_OK, intent);
                                showMessage("Užklausa sėkmingai užregistruota");
                                Navigation.findNavController(v).navigate(R.id.action_request_registration_to_nav_requests);
                            }

                            @Override
                            public void onFailure(Exception e) {
                                showMessage("Nepavyko užregistruoti užklausos \n Klaida: " + e);
                            }
                        });
                    }
                }
            }
        });


        return view;
    }

    public void createNewDialog(String url) {
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

    private void createProgressDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Uploading, please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Uploading");
    }

    private void fillSpinnerWithData(View view) {
        typeSpinner = view.findViewById(R.id.typeSpinner);
        RequestType[] types = RequestType.values();
        ArrayAdapter<RequestType> adp = new ArrayAdapter<>(getContext(), R.layout.registration_spinner_item, types);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adp);
    }

    private void UploadImage() {
        Dexter.withContext(getContext())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, 101);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            uri = data.getData();
        }
    }

    private void showMessage(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    private boolean validation(String phoneNumber, String description) {

        if (TextUtils.isEmpty(phoneNumber)) {
            numberField.setError("Įveskite kontaktinį tel. nr.");
            return false;
        }
        if (TextUtils.isEmpty(description)) {
            descriptionField.setError("Įveskite užklausos aprašymą");
            return false;
        }

        return true;
    }
}