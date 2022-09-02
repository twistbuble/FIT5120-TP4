package com.app.foodintol.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.foodintol.Models.LoadMeal.LoadMealResponseList;
import com.app.foodintol.R;

import java.util.ArrayList;
import java.util.Calendar;


public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MyViewHolder> {

    private ArrayList<LoadMealResponseList> list;
    private Context context;
    private ChildAdapterCallback callback;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView details;
        private LinearLayout meal;

        public MyViewHolder(View itemView) {
            super(itemView);

            details = itemView.findViewById(R.id.tv_meal_details);

            meal = itemView.findViewById(R.id.ll_meal);

        }
    }

    public MealAdapter(ArrayList<LoadMealResponseList> list, Context mContext){
        this.list = list;
        this.context = mContext;
        callback = (ChildAdapterCallback) mContext;
    }

    @Override
    public MealAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal,parent,false);
        return new MealAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MealAdapter.MyViewHolder holder, final int position) {

        LoadMealResponseList model = list.get(position);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(model.getTime()));

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        holder.details.setText(model.getTitle() + " " + hour + ":" + minute);

        holder.meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callback.onMealClick(list.get(position), position);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface ChildAdapterCallback {
        void onMealClick(LoadMealResponseList data, int pos);
    }

}

