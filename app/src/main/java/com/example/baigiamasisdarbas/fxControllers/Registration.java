package com.example.baigiamasisdarbas.fxControllers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baigiamasisdarbas.R;
import com.example.baigiamasisdarbas.dbControllers.ApartmentsController;
import com.example.baigiamasisdarbas.dbControllers.OnGetDataListener;
import com.example.baigiamasisdarbas.dbControllers.UserController;
import com.example.baigiamasisdarbas.ds.ApartmentBuilding;
import com.example.baigiamasisdarbas.ds.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration extends AppCompatActivity {
    EditText nameT, surnameT, emailT, numberT, passwordT, confirmPassword, registrationCode;
    Button regButton;
    TextView loginWindow;
    ImageView showPassword, showPassword2;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    Boolean isAllFieldsCorrect = false, show = false;
    Spinner spinner;

    private ArrayList<User> mNotes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        nameT = findViewById(R.id.editName);
        surnameT = findViewById(R.id.editSurname);
        emailT = findViewById(R.id.editEmail);
        numberT = findViewById(R.id.editPhone);
        passwordT = findViewById(R.id.editPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        registrationCode = findViewById(R.id.registrationCode);
        regButton = findViewById(R.id.buttonRegistration);
        loginWindow = findViewById(R.id.textViewLogIn);
        spinner = findViewById(R.id.spinner);
        showPassword = findViewById(R.id.showPasswordButton2);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (show.equals(false)) {
                    passwordT.setTransformationMethod(null);
                    confirmPassword.setTransformationMethod(null);
                    showPassword.setImageResource(R.drawable.eye);
                    show = true;
                }
                else {
                    passwordT.setTransformationMethod(new PasswordTransformationMethod());
                    confirmPassword.setTransformationMethod(new PasswordTransformationMethod());
                    showPassword.setImageResource(R.drawable.baseline_visibility_off_24);
                    show = false;

                }

            }
        });

        loginWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Registration.this, Login.class));
                finish();
            }
        });
        fillSpinnerWithData();


        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailT.getText().toString();
                String password = passwordT.getText().toString();
                String confPassword = confirmPassword.getText().toString();
                String name = nameT.getText().toString();
                String surname = surnameT.getText().toString();
                String phoneNumber = numberT.getText().toString();
                String address = spinner.getSelectedItem().toString();
                String regCode = registrationCode.getText().toString();
                isAllFieldsCorrect = validation(email, password, confPassword, name, surname, phoneNumber, address, regCode);
                ApartmentsController apartmentsController = new ApartmentsController();
                apartmentsController.getApartmentByAddress(address, new OnGetDataListener<ApartmentBuilding>() {
                    @Override
                    public void onSuccess(ApartmentBuilding data) {
                        if (Objects.equals(data.getRegistrationCode(), regCode)) {
                            if (isAllFieldsCorrect) {
                                UserController userController = new UserController();
                                userController.checkIfEmailExist(email, new OnGetDataListener<String>() {
                                    @Override
                                    public void onSuccess(String data) {
                                        if (data.equals("false")) {
                                            userController.registerUser(email, password, name, surname, phoneNumber, address);

                                            startActivity(new Intent(Registration.this, Login.class));
                                            finish();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Vartotojas su tokiu el. paštu jau egzistuoja", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Exception e) {

                                    }
                                });

                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Registracijos kodas neteisingas", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });

            }
        });
    }

    private void fillSpinnerWithData() {


        ApartmentsController apartmentsController = new ApartmentsController();
        apartmentsController.getAllApartmentsAddresses(new OnGetDataListener<ArrayList<String>>() {
            @Override
            public void onSuccess(ArrayList<String> data) {
                spinner = findViewById(R.id.spinner);
                ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(), R.layout.registration_spinner_item, data);
                adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adp);
            }

            @Override
            public void onFailure(Exception e) {

            }
        });


    }

    private boolean validation(String email, String password, String confPassword, String name, String surname, String phoneNumber, String address, String regCode) {

        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=\\S+$).{8,20}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);
        if (TextUtils.isEmpty(confPassword)) {
            confirmPassword.setError("Pakartotinai įveskite slaptažodį");
            return false;
        }

        if (TextUtils.isEmpty(regCode)) {
            registrationCode.setError("Įveskite registracijos kodą");
            return false;
        }
        if (!password.equals(confPassword))
            if (TextUtils.isEmpty(email)) {
                confirmPassword.setError("Slaptažodžiai nesutampa");
                return false;
            }
        if (TextUtils.isEmpty(password)) {
            passwordT.setError("Įveskite slaptažodį");
            return false;
        }
        if (password.length() < 8) {
            passwordT.setError("Slaptažodį turi sudaryti ne mažiau nei 8 simboliai");
            return false;
        }
        if (!m.matches()) {
            passwordT.setError("Password must contain 1 digit, 1 lower and upper letter");
            return false;
        }
        if (TextUtils.isEmpty(name)) {
            nameT.setError("Įveskite vardą");
            return false;
        }
        if (TextUtils.isEmpty(surname)) {
            surnameT.setError("Įveskite pavardę");
            return false;
        }
        if (TextUtils.isEmpty(phoneNumber)) {
            numberT.setError("Įveskite tel. nr.");
            return false;
        }
        return true;
    }


}