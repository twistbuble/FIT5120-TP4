package com.example.iteration_two;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class FoodIntoleranceInformationActivity extends AppCompatActivity {

    private TextView foodIntoleranceItem, foodIntoleranceText, symptomOne, symptomTwo, symptomThree, symptomFour, symptomFive, symptomSix, foodOne, foodTwo, foodThree, foodFour, foodFive;
    private RelativeLayout foodIntoleranceBack;
//    private Arraylist dairySymptomsList, dairyFoodsList,glutenSymptomsList, glutenFoodsList, soySymptomsList, soyFoodsList, eggsSymptomsList, eggsFoodList, fructoseSymptomsList, fructoseFoodsList, fodmapsSymptomsList, fodmapsFoodsList, salicylatesSymptomsList, salicylatesFoodsList, histamineSymptomsList, histamineFoodsList;
    private String dairyText = "@string/string_dairy_info", glutenText = "@string/string_gluten_info", soyText = "@string/string_soy_info", eggsText = "@string/string_eggs_info", fructoseText = "@string/string_fructose_info", fodmapsText = "@string/string_fodmaps_info", salicylatesText = "@string/string_salicylates_info", histamineText = "@string/string_histamine_info",

    /*symptoms*/
            dairySymptomsList[] = new String[]{"Abdominal Pain", "Bloating", "Diarrhoea", "Gas", "Nausea", "Indigestion"},
            glutenSymptomsList[] = new String[]{"Abdominal Pain", "Bloating", "Diarrhoea", "Headaches", "Nausea", "Eczema"},
            soySymptomsList[] = new String[]{"Abdominal Pain", "Bloating", "Diarrhoea", "Gas", "Nausea", "Fatigue"},
            eggsSymptomsList[] = new String[]{"Abdominal Pain", "Bloating", "Diarrhoea", "Gas", "Nausea", "Indigestion"},
            fructoseSymptomsList[] = new String[]{"Abdominal Pain", "Bloating", "Diarrhoea", "Gas", "Nausea", "Vomiting"},
            fodmapsSymptomsList[] = new String[]{"Abdominal Pain", "Bloating", "Diarrhoea", "Gas", "Fatigue", "Eczema"},
            salicylatesSymptomsList[] = new String[]{"Eczema", "Trouble Breathing", "Diarrhoea", "Abdominal Pain", "Gas", "Hives"},
            histamineSymptomsList[] = new String[]{"Abdominal Pain", "Eczema", "Headaches", "Digestive Issues", "Trouble Breathing", "Irregular Bodily Functions"},
    /*foods*/
            dairyFoodsList[] = new String[]{"Milk", "Ice Cream", "Yoghurts", "Fresh Cheeses", "Creams"},
            glutenFoodsList[] = new String[]{"Bread", "Baked Goods", "Pasta", "Cereals", "Crackers"},
            soyFoodsList[] = new String[]{"Soy Sauce", "Tofu", "Soy-based Products", "Asian-style Sauces", "Vegan-safe Products"},
            eggsFoodsList[] = new String[]{"Eggs", "Custards", "Mayonnaise", "Meringue", "Quiches"},
            fructoseFoodsList[] = new String[]{"All Fruits", "Honey", "Spinach", "Sweet Sauces", "Sugary Cereals"},
            fodmapsFoodsList[] = new String[]{"Bread", "Milk-based Products", "Some Fruits", "Honey", "Cakes"},
            salicylatesFoodsList[] = new String[]{"Spices", "Some Fruits", "Onion", "Honey", "Caffeinated Drinks"},
            histamineFoodsList[] = new String[]{"Cheeses", "Dairy Products", "Chocolate", "Nuts", "Caffeinated Drinks"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_intolerance_information);

        foodIntoleranceItem = findViewById(R.id.tv_food_intolerance_item);
        foodIntoleranceText = findViewById(R.id.tv_food_intolerance_text);
        symptomOne = findViewById(R.id.tv_symptom_one);
        symptomTwo = findViewById(R.id.tv_symptom_two);
        symptomThree = findViewById(R.id.tv_symptom_three);
        symptomFour = findViewById(R.id.tv_symptom_four);
        symptomFive = findViewById(R.id.tv_symptom_five);
        symptomSix = findViewById(R.id.tv_symptom_six);
        foodOne = findViewById(R.id.tv_common_foods_one);
        foodTwo = findViewById(R.id.tv_common_foods_two);
        foodThree = findViewById(R.id.tv_common_foods_three);
        foodFour = findViewById(R.id.tv_common_foods_four);
        foodFive = findViewById(R.id.tv_common_foods_five);
        foodIntoleranceBack = findViewById(R.id.rl_food_intol_info_back);

    }
}