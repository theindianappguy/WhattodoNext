package com.thendianappguy.whattodonext.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thendianappguy.whattodonext.CustomClass.TasksClass;
import com.thendianappguy.whattodonext.R;

import java.util.ArrayList;
import java.util.Random;

public class TasksAdapter extends BaseAdapter {

    private Context context;
    ArrayList<TasksClass> mTasksList;

    public TasksAdapter(Context x,ArrayList<TasksClass> list){
        context = x;
        this.mTasksList = list;
    }

    @Override
    public int getCount() {
        return mTasksList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View list;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        list = inflater.inflate(R.layout.task_card_new,null);
        TextView task = list.findViewById(R.id.task);
        LinearLayout dateCard = list.findViewById(R.id.dateCard);

        //Initialize the random object
        Random random = new Random();
        int zeroToFiveBothExclusive = random.nextInt(5) + 1;

        if(zeroToFiveBothExclusive == 0){
            dateCard.setBackground(context.getResources().getDrawable(R.drawable.left_circular_gradient_background1));
        }else if(zeroToFiveBothExclusive == 1){
            dateCard.setBackground(context.getResources().getDrawable(R.drawable.left_circular_gradient_background2));
        }else if(zeroToFiveBothExclusive == 2){
            dateCard.setBackground(context.getResources().getDrawable(R.drawable.left_circular_gradient_background3));
        }else if(zeroToFiveBothExclusive == 3){
            dateCard.setBackground(context.getResources().getDrawable(R.drawable.left_circular_gradient_background4));
        }else{
            dateCard.setBackground(context.getResources().getDrawable(R.drawable.left_circular_gradient_background5));
        }

        task.setText(mTasksList.get(position).getTasks()+" Total is "+mTasksList.get(position).getTotal());
        return list;
    }
}
