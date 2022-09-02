package com.app.foodintol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.app.foodintol.Models.HomeScreenData.HomeScreenDataResponse;
import com.app.foodintol.Models.HomeScreenData.HomeScreenDataResponseChildren;
import com.app.foodintol.Preference.PrefEntities;
import com.app.foodintol.Preference.Preferences;
import com.app.foodintol.Retrofit.FoodInTolApplication;
import com.app.foodintol.Retrofit.Helper.APIUtility;
import com.app.foodintol.UI.AddChildActivity;
import com.app.foodintol.UI.ChildAdapter;
import com.app.foodintol.UI.EditProfileActivity;
import com.app.foodintol.UI.FoodDiaryActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ChildAdapter.ChildAdapterCallback {

    private LinearLayout edit;
    private RelativeLayout addChild;
    private RecyclerView rvChildrenList;
    private ChildAdapter childAdapter;
    private ArrayList<HomeScreenDataResponseChildren> childrenList;
    private TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.tv_username);

        edit = findViewById(R.id.ll_edit_profile);

        addChild = findViewById(R.id.rl_add_kid);

        rvChildrenList = findViewById(R.id.rv_children);

        childrenList = new ArrayList<>();

        try{

            username.setText(Preferences.getPreference(MainActivity.this, PrefEntities.USERNAME));

        }catch (Exception e){

        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, EditProfileActivity.class));

            }
        });

        addChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, AddChildActivity.class));

            }
        });

        loadData();

    }

    private void loadData() {

        FoodInTolApplication.getApiUtility(MainActivity.this).HomeScreenData(MainActivity.this, true, new APIUtility.APIResponseListener<HomeScreenDataResponse>() {
            @Override
            public void onReceiveResponse(HomeScreenDataResponse response) {

                try{

                    ArrayList<HomeScreenDataResponseChildren> finalList = new ArrayList<>();

                    if(response.getChildren().size() > 0){

                        edit.setVisibility(View.VISIBLE);

                        finalList = response.getChildren();

                        childAdapter = new ChildAdapter(finalList, MainActivity.this);
                        rvChildrenList.setHasFixedSize(true);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                        rvChildrenList.setLayoutManager(layoutManager);
                        rvChildrenList.setAdapter(childAdapter);

                    }else{

                        edit.setVisibility(View.INVISIBLE);

                    }

                }catch (Exception e){

//                    Toast.makeText(MainActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    @Override
    public void onChildClick(HomeScreenDataResponseChildren data, int pos) {

        Intent intent = new Intent(MainActivity.this, FoodDiaryActivity.class);

        intent.putExtra("ChildId", data.getId());

        startActivity(intent);

    }
}