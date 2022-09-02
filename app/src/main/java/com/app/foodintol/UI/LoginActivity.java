package com.app.foodintol.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.foodintol.MainActivity;
import com.app.foodintol.Models.Login.LoginRequest;
import com.app.foodintol.Models.Login.LoginResponse;
import com.app.foodintol.Preference.PrefEntities;
import com.app.foodintol.Preference.Preferences;
import com.app.foodintol.R;
import com.app.foodintol.Retrofit.FoodInTolApplication;
import com.app.foodintol.Retrofit.Helper.APIUtility;

public class LoginActivity extends AppCompatActivity {

    private LinearLayout signIn;
    private RelativeLayout anonymousLogin, gmailLogin, facebookLogin;
    private EditText email, password;
    private TextView signUp, forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_login);

         signIn = findViewById(R.id.ll_sign_in);

         anonymousLogin = findViewById(R.id.rl_anonymous_login);
         gmailLogin = findViewById(R.id.rl_gmail_login);
         facebookLogin = findViewById(R.id.rl_facebook_login);

         email = findViewById(R.id.et_sign_in_email_input);
         password = findViewById(R.id.et_sign_in_password_input);

         signUp = findViewById(R.id.tv_sign_up);
         forgotPassword = findViewById(R.id.tv_forgot_password);

         signIn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 validate();

             }
         });

        anonymousLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                anonymousLoginAPI();

            }
        });

        gmailLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(LoginActivity.this, "Available from the next iteration!", Toast.LENGTH_SHORT).show();

            }
        });

        facebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(LoginActivity.this, "Available from the next iteration!", Toast.LENGTH_SHORT).show();

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(LoginActivity.this, "Available from the next iteration!", Toast.LENGTH_SHORT).show();

            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(LoginActivity.this, "Available from the next iteration!", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void anonymousLoginAPI() {

        LoginRequest request = new LoginRequest();

        request.setEmail("testuser");
        request.setPassword("testpass");

        FoodInTolApplication.getApiUtility(LoginActivity.this).Login(LoginActivity.this, true, request, new APIUtility.APIResponseListener<LoginResponse>() {
            @Override
            public void onReceiveResponse(LoginResponse response) {

                try {

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();

                    Preferences.setPreference(LoginActivity.this, PrefEntities.USERNAME, response.getUsername());

                }catch (Exception e){

                }

            }
        });

    }

    private void validate() {

        if(email.getText().toString().equals("")){

            Toast.makeText(LoginActivity.this, "Please enter your Email!", Toast.LENGTH_SHORT).show();

        }else if(password.getText().toString().equals("")){

            Toast.makeText(LoginActivity.this, "Please enter your password!", Toast.LENGTH_SHORT).show();

        }else if(email.getText().toString().equals("") && email.getText().toString().equals("")){

            Toast.makeText(LoginActivity.this, "Please enter your credentials!", Toast.LENGTH_SHORT).show();

        }else if(!email.getText().toString().equals("") && !email.getText().toString().equals("")){

            loginAPI();

        }

    }

    private void loginAPI() {

        Toast.makeText(LoginActivity.this, "Available from the next iteration!", Toast.LENGTH_SHORT).show();


        // uncomment after iteration 1

//        LoginRequest request = new LoginRequest();
//
//        request.setEmail(email.getText().toString());
//        request.setPassword(password.getText().toString());
//
//        FoodInTolApplication.getApiUtility(LoginActivity.this).Login(LoginActivity.this, true, request, new APIUtility.APIResponseListener<LoginResponse>() {
//            @Override
//            public void onReceiveResponse(LoginResponse response) {
//
//
//                startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                finish();
//
//            }
//        });


    }
}