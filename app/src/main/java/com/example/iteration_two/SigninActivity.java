package com.example.iteration_two;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


public class SigninActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private EditText email, pass;
    private TextView forgotPass, signUp;
    private RelativeLayout signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        email = findViewById(R.id.et_sign_in_email);
        pass = findViewById(R.id.et_sign_in_pass);
        forgotPass = findViewById(R.id.tv_forgot_password);
        signIn = findViewById(R.id.rl_sign_in);
        signUp = findViewById(R.id.tv_sign_up);
    }


}