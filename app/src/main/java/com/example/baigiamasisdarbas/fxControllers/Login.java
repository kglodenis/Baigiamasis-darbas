package com.example.baigiamasisdarbas.fxControllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baigiamasisdarbas.R;
import com.example.baigiamasisdarbas.dbControllers.OnGetDataListener;
import com.example.baigiamasisdarbas.dbControllers.UserController;
import com.example.baigiamasisdarbas.ds.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Login extends AppCompatActivity {
    EditText emailT, passwordT;
    Button signInButton;
    TextView registrationWindow, forgotPassword;
    FirebaseAuth fAuth;
    ImageView showPasswordButton;
    Boolean show = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailT = findViewById(R.id.editEmail);
        passwordT = findViewById(R.id.editPassword);
        signInButton = findViewById(R.id.buttonRegistration);
        registrationWindow = findViewById(R.id.textViewLogIn);
        showPasswordButton = findViewById(R.id.showPasswordButton);
        forgotPassword = findViewById(R.id.textViewRememberPassword);
        fAuth = FirebaseAuth.getInstance();
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, ForgotPassword.class));
            }
        });

        registrationWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Registration.class));
                finish();
            }
        });
        showPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (show.equals(false)) {
                    passwordT.setTransformationMethod(null);
                    showPasswordButton.setImageResource(R.drawable.eye);
                    show = true;
                } else {
                    passwordT.setTransformationMethod(new PasswordTransformationMethod());
                    showPasswordButton.setImageResource(R.drawable.baseline_visibility_off_24);
                    show = false;

                }

            }
        });
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailT.getText().toString();
                String password = passwordT.getText().toString();
                if (validation(email, password)) {
                    fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                UserController userController = new UserController();
                                userController.getCurrentUser(new OnGetDataListener<User>() {
                                    @Override
                                    public void onSuccess(User data) {
                                        String userType = data.getUserType();
                                        Intent i = new Intent(Login.this, MainPage.class);
                                        i.putExtra("userType", userType);
                                        startActivity(i);
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(Exception e) {

                                    }
                                });
                            } else {
                                Toast.makeText(Login.this, "Patikrinkite prisijungimo duomenis", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });
                }

            }
        });
    }

    private boolean validation(String email, String password) {

        if (TextUtils.isEmpty(email)) {
            emailT.setError("Įveskite el. paštą");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            passwordT.setError("Įveskite slaptažodį");
            return false;
        }

        return true;
    }


}