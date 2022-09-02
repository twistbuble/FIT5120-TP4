package com.app.foodintol.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.foodintol.Models.HomeScreenData.HomeScreenDataResponseChildren;
import com.app.foodintol.R;

import java.util.ArrayList;


public class DeleteChildAdapter extends RecyclerView.Adapter<DeleteChildAdapter.MyViewHolder> {

    private ArrayList<HomeScreenDataResponseChildren> list;
    private Context context;
    private ChildAdapterCallback callback;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView childName;
        private RelativeLayout delete;
        private ImageView gender;

        public MyViewHolder(View itemView) {
            super(itemView);

            childName = itemView.findViewById(R.id.tv_child_name);

            delete = itemView.findViewById(R.id.rl_delete);

            gender = itemView.findViewById(R.id.iv_gender);

        }
    }

    public DeleteChildAdapter(ArrayList<HomeScreenDataResponseChildren> list, Context mContext){
        this.list = list;
        this.context = mContext;
        callback = (ChildAdapterCallback) mContext;
    }

    @Override
    public DeleteChildAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_delete_child,parent,false);
        return new DeleteChildAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DeleteChildAdapter.MyViewHolder holder, final int position) {

        HomeScreenDataResponseChildren model = list.get(position);

        holder.childName.setText(model.getName());

        if(model.getGender() == 0){

            holder.gender.setImageDrawable(context.getResources().getDrawable(R.drawable.boy_icon));

        }else if(model.getGender() == 1){

            holder.gender.setImageDrawable(context.getResources().getDrawable(R.drawable.girl_icon));

        }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callback.onDeleteClick(list.get(position), position);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface ChildAdapterCallback {
        void onDeleteClick(HomeScreenDataResponseChildren data, int pos);
    }

}

