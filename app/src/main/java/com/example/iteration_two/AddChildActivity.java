package com.example.iteration_two;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class AddChildActivity extends AppCompatActivity {

    private RelativeLayout cancel, selectMale, selectFemale, confirmAddChild;
    private EditText childName, childAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);

        cancel = findViewById(R.id.rl_cancel);
        selectMale = findViewById(R.id.rl_select_male);
        selectFemale = findViewById(R.id.rl_select_female);
        childName = findViewById(R.id.et_add_child_name);
        childAge = findViewById(R.id.et_add_child_age);
        confirmAddChild = findViewById(R.id.rl_add_child_confirm);

    }
}