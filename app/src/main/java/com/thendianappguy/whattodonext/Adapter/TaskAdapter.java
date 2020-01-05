package com.thendianappguy.whattodonext.Adapter;

import android.content.Context;
import android.os.Handler;
import android.text.Layout;
import android.util.Log;
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
import com.thendianappguy.whattodonext.HelpingClass.SessionManagement;
import com.thendianappguy.whattodonext.MainActivity;
import com.thendianappguy.whattodonext.R;

import java.util.Random;

public class TaskAdapter extends FirestoreRecyclerAdapter<TasksClass, TaskAdapter.TaskHolder> {

    private static final String TAG = "TaskAdapter";
    private final int SPLASH_DISPLAY_LENGTH = 5000;
    Context mContext;

    public TaskAdapter(Context context,@NonNull FirestoreRecyclerOptions<TasksClass> options) {
        super(options);
        mContext = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull TaskHolder holder, int position, @NonNull final TasksClass model) {
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

        String task = model.getTasks();
        holder.task.setText(task);
        holder.monthTv.setText(model.getMonth());
        holder.dayTv.setText(model.getDate());
        String weekTimeSt = model.getWeek()+", "+model.getTime();
        holder.weekTimeTv.setText(weekTimeSt);

        holder.cardLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** Task, impact, effort, profitability, fitwithvision */
                ((MainActivity) mContext).showBottomSheetWithPrefilledDetails(model.getTasks(),
                        model.getImpact(),model.getEffort(),model.getProfitability(),model.getFitwithvision(),
                        model.getTaskId());
            }
        });

    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_card_new,
               parent,false);
        return new TaskHolder(view);
    }

    public void onDataChanged() {
        SessionManagement cookie = new SessionManagement(mContext);
        cookie.setItemCount(getItemCount());
        Log.e(TAG, "onDataChanged:1 "+getItemCount() );
        if (mContext instanceof MainActivity) {
            ((MainActivity) mContext).checkAndSetEmptyView();
        }

    }


    public void deleteItem(final int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class TaskHolder extends RecyclerView.ViewHolder{

        TextView task, monthTv, dayTv, weekTimeTv;
        LinearLayout dateCard, cardLl;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);

            task = itemView.findViewById(R.id.task);
            monthTv = itemView.findViewById(R.id.monthTv);
            dayTv = itemView.findViewById(R.id.dayTv);
            weekTimeTv = itemView.findViewById(R.id.weekTimeTv);
            dateCard = itemView.findViewById(R.id.dateCard);

            cardLl = itemView.findViewById(R.id.cardLl);



        }
    }
}
