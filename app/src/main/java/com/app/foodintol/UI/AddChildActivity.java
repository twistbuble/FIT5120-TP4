package com.app.foodintol.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.foodintol.MainActivity;
import com.app.foodintol.Models.AddChild.AddChildRequest;
import com.app.foodintol.Models.AddChild.AddChildResponse;
import com.app.foodintol.R;
import com.app.foodintol.Retrofit.FoodInTolApplication;
import com.app.foodintol.Retrofit.Helper.APIUtility;

public class AddChildActivity extends AppCompatActivity {

    private RelativeLayout cancel, save, rlMale, rlFemale;
    private EditText name, age, intoleranceOne, intoleranceTwo, intoleranceThree;
    private int gender = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);

        cancel = findViewById(R.id.rl_cancel);
        save = findViewById(R.id.rl_save);
        rlMale = findViewById(R.id.rl_select_male);
        rlFemale = findViewById(R.id.rl_select_female);

        name = findViewById(R.id.et_add_child_name_input);
        age = findViewById(R.id.et_add_child_age_input);
        intoleranceOne = findViewById(R.id.et_add_child_intolerance_one);
        intoleranceTwo = findViewById(R.id.et_add_child_intolerance_two);
        intoleranceThree = findViewById(R.id.et_add_child_intolerance_three);

        rlMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gender = 0;
                rlMale.setBackground(getResources().getDrawable(R.drawable.orange_square_gender));
                rlFemale.setBackground(getResources().getDrawable(R.drawable.grey_square_gender));

            }
        });

        rlFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gender = 1;
                rlFemale.setBackground(getResources().getDrawable(R.drawable.orange_square_gender));
                rlMale.setBackground(getResources().getDrawable(R.drawable.grey_square_gender));

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(AddChildActivity.this, MainActivity.class));

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validate()){

                    addChild();

                }

            }
        });

    }

    private void addChild() {

        AddChildRequest request = new AddChildRequest();
        request.setName(name.getText().toString());
        request.setAge(age.getText().toString());
        request.setGender(gender);

        String intolerance = "";

        if(intoleranceOne.getText().toString().equals("")){

            intolerance += "";

        }else{

            intolerance += intoleranceOne.getText().toString();

        }
        if(intoleranceTwo.getText().toString().equals("")){

            intolerance += "";

        }else{

            if(!intoleranceOne.getText().toString().equals("")){

                intolerance += ", " + intoleranceTwo.getText().toString();

            }

        }
        if(intoleranceThree.getText().toString().equals("")){

            intolerance += "";

        }else{

            if(!intoleranceOne.getText().toString().equals("") || !intoleranceTwo.getText().toString().equals("")){

                intolerance += ", " + intoleranceThree.getText().toString();

            }

        }

        request.setIntolerance(intolerance);

        FoodInTolApplication.getApiUtility(AddChildActivity.this).AddChild(AddChildActivity.this, true, request, new APIUtility.APIResponseListener<AddChildResponse>() {
            @Override
            public void onReceiveResponse(AddChildResponse response) {

                try{

                    if (response.getStatus() == 200){

                        startActivity(new Intent(AddChildActivity.this, MainActivity.class));
                        finish();

                    }

                }catch (Exception e){

                }

            }
        });

    }

    private boolean validate() {

        if(name.getText().toString().equals("")){

            Toast.makeText(AddChildActivity.this, "Please enter your child's name!", Toast.LENGTH_SHORT).show();
            return false;

        }else if(age.getText().toString().equals("")){

            Toast.makeText(AddChildActivity.this, "Please enter your child's age!", Toast.LENGTH_SHORT).show();
            return false;

        }else if(gender == -1){

            Toast.makeText(AddChildActivity.this, "Please choose your child's gender!", Toast.LENGTH_SHORT).show();
            return false;

        }

        return true;

    }
}