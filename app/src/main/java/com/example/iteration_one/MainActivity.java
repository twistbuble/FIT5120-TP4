package com.example.iteration_one;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private EditText signInEmail, signInPassword, createAccName, createAccEmail, createAccPass, createAccConfirmPass;
    private RelativeLayout anonLogIn, gmailLogin, facebookLogin;
    private LinearLayout signIn, kidOne, kidTwo, confirmCreateAcc, mealOne, mealTwo, mealThree;
    private TextView forgotPassword, signUp;
    private ImageView addPic, addKid, addFood;
    private Spinner dateSpinner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // EditText
        createAccName = findViewById(R.id.et_create_acc_name_input);
        createAccEmail = findViewById(R.id.et_create_acc_email_input);
        createAccPass = findViewById(R.id.et_create_acc_password_input);
        createAccConfirmPass = findViewById(R.id.et_create_acc_confirm_pass);
        signInEmail = findViewById(R.id.et_sign_in_email_input);
        signInPassword = findViewById(R.id.et_sign_in_password_input);

        // RelativeLayout
        anonLogIn = findViewById(R.id.rl_anonymous_login);
        gmailLogin = findViewById(R.id.rl_gmail_login);
        facebookLogin = findViewById(R.id.rl_facebook_login);

        // LinearLayout
        kidOne = findViewById(R.id.ll_kid_one);
        kidTwo = findViewById(R.id.ll_kid_two);
        signIn = findViewById(R.id.ll_sign_in);
        confirmCreateAcc = findViewById(R.id.ll_confirm_create_acc);
        mealOne = findViewById(R.id.ll_meal_one);
        mealTwo = findViewById(R.id.ll_meal_two);
        mealThree = findViewById(R.id.ll_meal_three);

        // TextView
        forgotPassword = findViewById(R.id.tv_forgot_password);
        signUp = findViewById(R.id.tv_sign_up);

        // ImageView
        addPic = findViewById(R.id.iv_camera);
        addKid = findViewById(R.id.iv_add_kid);

        // Spinner
        dateSpinner = findViewById(R.id.spin_food_diary_date);
    }
}