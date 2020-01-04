package com.thendianappguy.whattodonext.Adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.thendianappguy.whattodonext.CustomClass.TasksClass;
import com.thendianappguy.whattodonext.R;

import java.util.Random;

public class TaskAdapter extends FirestoreRecyclerAdapter<TasksClass, TaskAdapter.TaskHolder> {

    Context mContext;

    public TaskAdapter(Context context,@NonNull FirestoreRecyclerOptions<TasksClass> options) {
        super(options);
        mContext = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull TaskHolder holder, int position, @NonNull TasksClass model) {
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

        String task = model.getTasks()+" Total is "+model.getTotal();
        holder.task.setText(task);


    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_card_new,
               parent,false);
        return new TaskHolder(view);
    }

    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class TaskHolder extends RecyclerView.ViewHolder{

        TextView task ;
        LinearLayout dateCard;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);

            task = itemView.findViewById(R.id.task);
            dateCard = itemView.findViewById(R.id.dateCard);
        }
    }
}
