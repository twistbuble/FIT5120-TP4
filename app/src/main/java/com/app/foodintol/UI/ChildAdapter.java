package com.app.foodintol.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.foodintol.Models.HomeScreenData.HomeScreenDataResponseChildren;
import com.app.foodintol.R;

import java.util.ArrayList;


public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.MyViewHolder> {

    private ArrayList<HomeScreenDataResponseChildren> list;
    private Context context;
    private ChildAdapterCallback callback;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView childName;
        private LinearLayout child;
        private ImageView gender;

        public MyViewHolder(View itemView) {
            super(itemView);

            childName = itemView.findViewById(R.id.tv_child_name);

            child = itemView.findViewById(R.id.ll_child);

            gender = itemView.findViewById(R.id.iv_gender);

        }
    }

    public ChildAdapter(ArrayList<HomeScreenDataResponseChildren> list, Context mContext){
        this.list = list;
        this.context = mContext;
        callback = (ChildAdapterCallback) mContext;
    }

    @Override
    public ChildAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child,parent,false);
        return new ChildAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChildAdapter.MyViewHolder holder, final int position) {

        HomeScreenDataResponseChildren model = list.get(position);

        holder.childName.setText(model.getName());

        if(model.getGender() == 0){

            holder.gender.setImageDrawable(context.getResources().getDrawable(R.drawable.boy_icon));

        }else if(model.getGender() == 1){

            holder.gender.setImageDrawable(context.getResources().getDrawable(R.drawable.girl_icon));

        }

        holder.child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callback.onChildClick(list.get(position), position);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface ChildAdapterCallback {
        void onChildClick(HomeScreenDataResponseChildren data, int pos);
    }

}

