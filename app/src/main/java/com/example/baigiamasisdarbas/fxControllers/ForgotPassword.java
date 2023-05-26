package com.example.baigiamasisdarbas.fxControllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.baigiamasisdarbas.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private EditText mEmailEditText;
    private Button mSubmitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();
        mEmailEditText = findViewById(R.id.email_edit_text);
        mSubmitButton = findViewById(R.id.submit_button);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }
    private void resetPassword() {
        String email = mEmailEditText.getText().toString().trim();

        if (validation(email)) {
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgotPassword.this,
                                        "Slaptažodžio atkūrimo nuoroda išsiųsta", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ForgotPassword.this,
                                        "Slaptažodžio atkūrimo nuorodos nepavyko išsiųsti", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }


    }

    private boolean validation(String email) {

        if (TextUtils.isEmpty(email)) {
            mEmailEditText.requestFocus();
            mEmailEditText.setError("Įveskite el. paštą");

            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailEditText.requestFocus();
            return false;
        }


        return true;
    }
}