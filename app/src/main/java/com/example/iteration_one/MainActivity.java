package com.example.iteration_one;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private EditText email, password;
    private RelativeLayout anonLogIn, gmailLogin, facebookLogin;
    private LinearLayout signIn, kidOne, kidTwo;
    private TextView forgotPass, signUp;
    private ImageView addPic;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.et_email_input);
        password = findViewById(R.id.et_password_input);
        forgotPass = findViewById(R.id.tv_forgot_password);
        signIn = findViewById(R.id.ll_sign_in);
        anonLogIn = findViewById(R.id.rl_anonymous_login);
        gmailLogin = findViewById(R.id.rl_gmail_login);
        facebookLogin = findViewById(R.id.rl_facebook_login);
        signUp = findViewById(R.id.tv_sign_up);
        addPic = findViewById(R.id.iv_camera);
        kidOne = findViewById(R.id.ll_kid_one);
        kidTwo = findViewById(R.id.ll_kid_two);

    }
}