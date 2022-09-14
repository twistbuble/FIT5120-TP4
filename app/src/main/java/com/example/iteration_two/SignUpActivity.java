package com.example.iteration_two;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SignUpActivity extends AppCompatActivity {

    private TextView signUpName, signUpEmail, signUpPass, signUpConfirmPass;
    private RelativeLayout signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUpName = findViewById(R.id.et_signup_name);
        signUpEmail = findViewById(R.id.et_signup_email);
        signUpPass = findViewById(R.id.et_signup_pass);
        signUpConfirmPass = findViewById(R.id.et_signup_confirm_pass);
        signUp = findViewById(R.id.rl_confirm_signup);
    }
}