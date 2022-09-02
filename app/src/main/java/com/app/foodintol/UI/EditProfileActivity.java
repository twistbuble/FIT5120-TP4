package com.app.foodintol.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.foodintol.MainActivity;
import com.app.foodintol.Models.DeleteChild.DeleteChildRequest;
import com.app.foodintol.Models.DeleteChild.DeleteChildResponse;
import com.app.foodintol.Models.HomeScreenData.HomeScreenDataResponse;
import com.app.foodintol.Models.HomeScreenData.HomeScreenDataResponseChildren;
import com.app.foodintol.Preference.PrefEntities;
import com.app.foodintol.Preference.Preferences;
import com.app.foodintol.R;
import com.app.foodintol.Retrofit.FoodInTolApplication;
import com.app.foodintol.Retrofit.Helper.APIUtility;

import java.util.ArrayList;

public class EditProfileActivity extends AppCompatActivity implements DeleteChildAdapter.ChildAdapterCallback{

    private LinearLayout save;
    private TextView username;
    private RecyclerView rvDeleteChild;
    private ArrayList<HomeScreenDataResponseChildren> childrenList;
    private DeleteChildAdapter deleteChildAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        username = findViewById(R.id.tv_username);

        save = findViewById(R.id.ll_save_profile);

        rvDeleteChild = findViewById(R.id.rv_delete_children);

        childrenList = new ArrayList<>();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(EditProfileActivity.this, MainActivity.class));
                finish();

            }
        });

        try{

            username.setText(Preferences.getPreference(EditProfileActivity.this, PrefEntities.USERNAME));

        }catch (Exception e){

        }

        loadData();

    }

    private void loadData() {

        FoodInTolApplication.getApiUtility(EditProfileActivity.this).HomeScreenData(EditProfileActivity.this, true, new APIUtility.APIResponseListener<HomeScreenDataResponse>() {
            @Override
            public void onReceiveResponse(HomeScreenDataResponse response) {

                try{

                    ArrayList<HomeScreenDataResponseChildren> finalList = new ArrayList<>();

                    if(response.getChildren().size() > 0){

                        finalList = response.getChildren();

                        deleteChildAdapter = new DeleteChildAdapter(finalList, EditProfileActivity.this);
                        rvDeleteChild.setHasFixedSize(true);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(EditProfileActivity.this);
                        rvDeleteChild.setLayoutManager(layoutManager);
                        rvDeleteChild.setAdapter(deleteChildAdapter);

                    }

                }catch (Exception e){

//                    Toast.makeText(MainActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    @Override
    public void onDeleteClick(HomeScreenDataResponseChildren data, int pos) {

        Log.e("MainActivity", "childId:" + data.getId());
        deleteChild(data.getId());

    }

    private void deleteChild(String id) {

        try {

            Log.e("MainActivity", "childId:" + id);

            DeleteChildRequest request = new DeleteChildRequest();
            request.setChildId(id);

            FoodInTolApplication.getApiUtility(EditProfileActivity.this).DeleteChild(EditProfileActivity.this, true, request, new APIUtility.APIResponseListener<DeleteChildResponse>() {
                @Override
                public void onReceiveResponse(DeleteChildResponse response) {

                    Toast.makeText(EditProfileActivity.this, "Child deleted!", Toast.LENGTH_SHORT).show();

                    loadData();

                }
            });

        }catch (Exception e){

            Toast.makeText(EditProfileActivity.this, "Service unavailable temporarily.", Toast.LENGTH_SHORT).show();

        }

    }
}