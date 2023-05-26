package com.example.baigiamasisdarbas.fxControllers.ui.userEditor;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.baigiamasisdarbas.databinding.FragmentUserEditBinding;
import com.example.baigiamasisdarbas.dbControllers.OnGetDataListener;
import com.example.baigiamasisdarbas.dbControllers.UserController;
import com.example.baigiamasisdarbas.ds.User;
import com.example.baigiamasisdarbas.fxControllers.Login;
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


public class UserEdit extends Fragment {

    FragmentUserEditBinding binding;
    Uri uri;
    FirebaseFirestore fStore;
    FirebaseStorage fStorage;
    FirebaseAuth fAuth;
    FirebaseUser currentUser;
    UserController userController;
    String uriOld;
    private ProgressDialog progressDialog;
    Boolean isPhotoUploaded = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserEditBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();
        fAuth = FirebaseAuth.getInstance();
        currentUser = fAuth.getCurrentUser();
        userController = new UserController();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Uploading, please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Uploading");

        binding.signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), Login.class));

            }
        });
        binding.deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userController.deleteUser();
                startActivity(new Intent(getContext(), Login.class));
            }
        });
        userController.getCurrentUser(new OnGetDataListener<User>() {
            @Override
            public void onSuccess(User data) {
                binding.userNameField.setText(data.getName());
                binding.userEmailField.setText(data.getEmail());
                binding.userSurnameField.setText(data.getSurname());
                binding.userNumberField.setText(data.getPhoneNumber());
                uriOld = data.getPictureUri();
                Picasso.get().load(data.getPictureUri()).into(binding.profilePictureImageView);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d("Klaida", "Klaidos pranešimas: " + e);

            }
        });

        binding.uploadProfilePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadImage();
                isPhotoUploaded = true;
            }
        });

        binding.updateUserInformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user;
                if (validation(binding.userEmailField.getText().toString(), binding.userNameField.getText().toString(), binding.userSurnameField.getText().toString(), binding.userNumberField.getText().toString())) {
                    if (isPhotoUploaded.equals(false)) {
                        user = new User(binding.userEmailField.getText().toString(), binding.userNameField.getText().toString(), binding.userSurnameField.getText().toString(), binding.userNumberField.getText().toString(), uriOld);
                        userController.updateUser(user, new OnGetDataListener<String>() {
                            @Override
                            public void onSuccess(String data) {
                                showMessage("Vartotojo informacija sėkmingai atnaujinta");
                            }

                            @Override
                            public void onFailure(Exception e) {
                                showMessage("Nepavyko atnaujinti vartotojo informacijos \n Klaida: " + e);
                            }
                        });
                    } else {
                        user = new User(binding.userEmailField.getText().toString(), binding.userNameField.getText().toString(), binding.userSurnameField.getText().toString(), binding.userNumberField.getText().toString(), uri.toString());
                        userController.updateUser(uri, user, new OnGetDataListener<String>() {
                            @Override
                            public void onSuccess(String data) {
                                Intent intent = new Intent();
                                requireActivity().setResult(RESULT_OK, intent);
                                progressDialog.dismiss();
                                showMessage("Vartotojo informacija sėkmingai atnaujinta");
                            }

                            @Override
                            public void onFailure(Exception e) {
                                showMessage("Nepavyko atnaujinti vartotojo informacijos \n Klaida: " + e);
                            }
                        });

                    }
                }


            }
        });
        return root;
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
            binding.profilePictureImageView.setImageURI(uri);
        }
    }

    private void showMessage(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    private boolean validation(String email, String name, String surname, String number) {

        if (TextUtils.isEmpty(email)) {
            binding.userEmailField.setError("Įveskite el. paštą");
            return false;
        }
        if (TextUtils.isEmpty(name)) {
            binding.userNameField.setError("Įveskite vardą");
            return false;
        }
        if (TextUtils.isEmpty(surname)) {
            binding.userSurnameField.setError("Įveskite pavardę");
            return false;
        }
        if (TextUtils.isEmpty(number)) {
            binding.userNumberField.setError("Įveskite tel. nr.");
            return false;
        }

        return true;
    }
}


