package com.thendianappguy.whattodonext.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thendianappguy.whattodonext.CustomClass.TasksClass;
import com.thendianappguy.whattodonext.R;

import java.util.ArrayList;
import java.util.Random;

public class TaskRecycleViewAdapter extends RecyclerView.Adapter<TaskRecycleViewAdapter.TaskRecyclerViewHolder> {

    private Context mContext;
    ArrayList<TasksClass> mTasksList;

    @NonNull
    @Override
    public TaskRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_card_new,parent,
                false);
        TaskRecyclerViewHolder evh = new TaskRecyclerViewHolder(view);
        return evh;
    }

    public void deleteItem(int position){

    }

    @Override
    public void onBindViewHolder(@NonNull TaskRecyclerViewHolder holder, int position) {
        //Initialize the random object
        Random random = new Random();
        int zeroToFiveBothExclusive = random.nextInt(5) + 1;

        if(zeroToFiveBothExclusive == 0){
            holder.dateCard.setBackground(mContext.getResources().getDrawable(R.drawable.left_circular_gradient_background1));
        }else if(zeroToFiveBothExclusive == 1){
            holder.dateCard.setBackground(mContext.getResources().getDrawable(R.drawable.left_circular_gradient_background2));
        }else if(zeroToFiveBothExclusive == 2){
            holder.dateCard.setBackground(mContext.getResources().getDrawable(R.drawable.left_circular_gradient_background3));
        }else if(zeroToFiveBothExclusive == 3){
            holder.dateCard.setBackground(mContext.getResources().getDrawable(R.drawable.left_circular_gradient_background4));
        }else{
            holder.dateCard.setBackground(mContext.getResources().getDrawable(R.drawable.left_circular_gradient_background5));
        }

        holder.task.setText(mTasksList.get(position).getTasks()+" Total is "+mTasksList.get(position).getTotal());

    }

    @Override
    public int getItemCount() {
        return mTasksList.size();
    }

    public static class TaskRecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView task ;
        LinearLayout dateCard;

        public TaskRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            task = itemView.findViewById(R.id.task);
            dateCard = itemView.findViewById(R.id.dateCard);
        }
    }

    public TaskRecycleViewAdapter(Activity activity, ArrayList<TasksClass> tasksClassArrayList){
        mTasksList = tasksClassArrayList;
        mContext = activity;
    }
}
