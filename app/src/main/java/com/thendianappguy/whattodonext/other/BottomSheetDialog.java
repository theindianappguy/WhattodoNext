package com.thendianappguy.whattodonext.other;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.thendianappguy.whattodonext.CustomClass.TasksClass;
import com.thendianappguy.whattodonext.HelpingClass.FunctionClass;
import com.thendianappguy.whattodonext.HelpingClass.SessionManagement;
import com.thendianappguy.whattodonext.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    private static final String TAG = "BottomSheetDialog";
    SeekBar impactSeekbar, effortsSeekbar, profitabilitySeekbar, fitwithvisionSeekbar;
    LinearLayout addTaskBtn;
    EditText taskEt;
    TextView impactTv, effortsTv, profitabilityTv, fitwithvisionTv, addTaskTv;
    private BottomSheetListener mListner;
    int impactInt = 0, effortInt = 0, profitabilityInt = 0, fitwithvisionInt = 0;
    boolean impactSelected = false, effortSelected = false, profitabilitySelected = false,
            fitwithvisionSelected = false;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    SessionManagement cookie;
    Context mContext;

    String task, taskIdSt;
    boolean isnew;

    public BottomSheetDialog(Context context,String task, int impact, int effort, int profitability,
                             int fitwithvision,String taskId, boolean isnew){
        this.mContext = context;
        this.task = task;
        this.impactInt = impact;
        this.effortInt = effort;
        this.profitabilityInt = profitability;
        this.fitwithvisionInt = fitwithvision;
        this.isnew = isnew;
        this.taskIdSt = taskId;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_task_bottom_sheet, container, false);

        impactSeekbar = view.findViewById(R.id.impactSeekbar);
        effortsSeekbar = view.findViewById(R.id.effortsSeekbar);
        profitabilitySeekbar = view.findViewById(R.id.profitabilitySeekbar);
        fitwithvisionSeekbar = view.findViewById(R.id.fitwithvisionSeekbar);

        addTaskBtn = view.findViewById(R.id.addTaskBtn);
        taskEt = view.findViewById(R.id.taskEt);

        //Text views
        impactTv = view.findViewById(R.id.impactTv);
        effortsTv = view.findViewById(R.id.effortTv);
        profitabilityTv = view.findViewById(R.id.profitabilityTv);
        fitwithvisionTv = view.findViewById(R.id.fitwithvisionTv);
        addTaskTv = view.findViewById(R.id.addTaskTv);

        setOnSeekBarChangeListner();
        cookie = new SessionManagement(getContext());

        if(isnew){
            addTaskTv.setText("Add task");
        }else{
            addTaskTv.setText("Update task");
        }

        /** Setting up the details */
        taskEt.setText(task);
        impactSeekbar.setProgress(impactInt);
        effortsSeekbar.setProgress(effortInt);
        profitabilitySeekbar.setProgress(profitabilityInt);
        fitwithvisionSeekbar.setProgress(fitwithvisionInt);

        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate(getContext())) {
                    addTaskBtn.setClickable(false);
                    Log.d(TAG, "onClick:addTaskBtn validated ");
                    String task = taskEt.getText().toString();
                    updatatoCloud(getContext(), task, impactInt, effortInt, profitabilityInt,
                            fitwithvisionInt);
                    mListner.onButtonClicked("close");
                } else {

                }
            }
        });

        return view;
    }

    private String getWeekFromInt() {
        Calendar c = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        if (Calendar.MONDAY == dayOfWeek) {
            return  "Mon";
        } else if (Calendar.TUESDAY == dayOfWeek) {
            return "Tue";
        } else if (Calendar.WEDNESDAY == dayOfWeek) {
            return "Wed";
        } else if (Calendar.THURSDAY == dayOfWeek) {
            return "Thu";
        } else if (Calendar.FRIDAY == dayOfWeek) {
            return "Fri";
        } else if (Calendar.SATURDAY == dayOfWeek) {
            return "Sat";
        } else if (Calendar.SUNDAY == dayOfWeek) {
            return "Sun";
        }else {
            return "";
        }
    }

    private void updatatoCloud(final Context context, String task, int impact, int effort,
                               int profitability, int fitwithvision) {


        //progressBar.setVisibility(View.VISIBLE);
        Log.e(TAG, "updatatoCloud: ");
        TasksClass tasksClass = new TasksClass();

        Date c = Calendar.getInstance().getTime();
        Log.e(TAG, "updatatoCloud: "+c );

        SimpleDateFormat df = new SimpleDateFormat("dd");
        String day_of_month = df.format(c);
        Log.e(TAG, "onClick: " + day_of_month);
        tasksClass.setDate(day_of_month);

        SimpleDateFormat monthdf = new SimpleDateFormat("MMM");
        String month = monthdf.format(c);
        Log.e(TAG, "onClick: " + month);
        tasksClass.setMonth(month);

        SimpleDateFormat yeardf = new SimpleDateFormat("yyyy");
        String year = yeardf.format(c);
        Log.e(TAG, "onClick: " + year);


        Calendar calendar = Calendar.getInstance();
        //calendar.setTime(d);
        tasksClass.setWeek(getWeekFromInt());

        DateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        String time = timeFormat.format(c);
        tasksClass.setTime(time);

        tasksClass.setTasks(task);
        tasksClass.setImpact(impact);
        tasksClass.setEffort(effort);
        tasksClass.setProfitability(profitability);
        tasksClass.setFitwithvision(fitwithvision);
        tasksClass.setTotal(getTotal(impact, effort, profitability, fitwithvision));

        if(isnew){
            String taskId = FunctionClass.getSaltString(10);
            tasksClass.setTaskId(taskId);
            db.collection("Main").document("UserTasks").collection(cookie.getUserUid())
                    .document(taskId).set(tasksClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.e(TAG, "onSuccess: of upload");
                    //progressBar.setVisibility(View.GONE);
                    Toast.makeText(context, "Task Added Successfully ", Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //progressBar.setVisibility(View.GONE);
                            Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
                            addTaskBtn.setClickable(true);
                            Log.e(TAG, "onFailure: " + e);
                        }
                    });

        }else{
            tasksClass.setTaskId(taskIdSt);
            Log.e(TAG, "updatatoCloud: This is the taskId : "+taskIdSt );
            db.collection("Main").document("UserTasks").collection(cookie.getUserUid())
                    .document(taskIdSt).set(tasksClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.e(TAG, "onSuccess: of upload");
                    //progressBar.setVisibility(View.GONE);
                    Toast.makeText(context, "Task Updated Successfully ", Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //progressBar.setVisibility(View.GONE);
                            Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
                            addTaskBtn.setClickable(true);
                            Log.e(TAG, "onFailure: " + e);
                        }
                    });
        }

    }

    private int getTotal(int impact, int effort, int profitability, int fitwithvision) {
        return impact + effort + profitability + fitwithvision;
    }


    private boolean validate(Context context) {
        if(isnew){
            Log.e(TAG, "validate: we are in validate");
            if (!taskEt.getText().toString().equals("") && impactSelected && effortSelected && profitabilitySelected && fitwithvisionSelected) {
                return true;
            } else {
                Toast.makeText(context, "Make Sure All The Details Are Filled", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else{
            return true;
        }
    }

    private void setOnSeekBarChangeListner() {
        /** Impact */
        impactSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                impactInt = progress;
                String string = "Impact Selected " + progress + "/5";
                impactTv.setText(string);
                impactSelected = true;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        /** Effort */
        effortsSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                effortInt = progress;
                String string = "Efforts Selected " + progress + "/5";
                effortsTv.setText(string);
                effortSelected = true;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        /** Profitaability */
        profitabilitySeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                profitabilityInt = progress;
                String string = "Profitaability Selected " + progress + "/5";
                profitabilityTv.setText(string);
                profitabilitySelected = true;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        /** Fit with vision */
        fitwithvisionSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                fitwithvisionInt = progress;
                String string = "Fit with vision Selected " + progress + "/5";
                fitwithvisionTv.setText(string);
                fitwithvisionSelected = true;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public interface BottomSheetListener {
        void onButtonClicked(String text);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            mListner = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListner");
        }

    }
}
