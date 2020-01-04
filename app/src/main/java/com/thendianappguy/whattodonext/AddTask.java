package com.thendianappguy.whattodonext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.thendianappguy.whattodonext.CustomClass.TasksClass;
import com.thendianappguy.whattodonext.HelpingClass.SessionManagement;

public class AddTask extends AppCompatActivity {

    private static final String TAG = "AddTask";

    SeekBar impactSeekbar, effortsSeekbar, profitabilitySeekbar, fitwithvisionSeekbar;

    LinearLayout addTaskBtn;
    EditText taskEt;
    TextView impactTv, effortsTv, profitabilityTv, fitwithvisionTv;

    boolean impactSelected = false, effortSelected = false, profitabilitySelected = false,
            fitwithvisionSelected = false;

    int impactInt = 0, effortInt = 0, profitabilityInt = 0, fitwithvisionInt = 0;

    DatabaseReference databaseReference;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    SessionManagement cookie;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        defineViews();
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        otherDefinings();

        setOnClickListner();

        setOnseekBarChange();

    }

    private void setOnseekBarChange() {

        /** Impact */
        impactSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                impactInt = progress;
                String string = "Impact Selected "+progress+"/5";
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
                String string = "Efforts Selected "+progress+"/5";
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
                String string = "Profitaability Selected "+progress+"/5";
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
                String string = "Fit with vision Selected "+progress+"/5";
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

    private void otherDefinings() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        cookie = new SessionManagement(AddTask.this);
    }

    private void setOnClickListner() {
        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    addTaskBtn.setClickable(false);
                    Log.d(TAG, "onClick:addTaskBtn validated ");
                    String task = taskEt.getText().toString();
                    updatatoCloud(task, impactInt, effortInt, profitabilityInt, fitwithvisionInt);
                } else {

                }
            }
        });
    }

    private void updatatoCloud(String task, int impact, int effort, int profitability, int fitwithvision) {

        progressBar.setVisibility(View.VISIBLE);
        Log.e(TAG, "updatatoCloud: " );
        TasksClass tasksClass = new TasksClass();
        tasksClass.setTasks(task);
        tasksClass.setImpact(impact);
        tasksClass.setEffort(effort);
        tasksClass.setProfitability(profitability);
        tasksClass.setFitwithvision(fitwithvision);
        tasksClass.setTotal(getTotal(impact,effort,profitability,fitwithvision));

        db.collection("Main").document("UserTasks").collection(cookie.getUserUid())
                .document().set(tasksClass).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e(TAG, "onSuccess: of upload" );
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AddTask.this, "Task Added Successfully ", Toast.LENGTH_SHORT).show();
                finish();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AddTask.this, "Error!", Toast.LENGTH_SHORT).show();
                addTaskBtn.setClickable(true);
                Log.e(TAG, "onFailure: "+e);
            }
        });

    }

    private int getTotal(int impact, int effort, int profitability, int fitwithvision) {
        return impact+effort+profitability+fitwithvision;
    }

    private boolean validate() {
        Log.e(TAG, "validate: we are in validate" );
        if (impactSelected && effortSelected && profitabilitySelected && fitwithvisionSelected) {
            return true;
        }else {
            Toast.makeText(this, "Make Sure All The Details Are Filled", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void defineViews() {

        impactSeekbar = findViewById(R.id.impactSeekbar);
        effortsSeekbar = findViewById(R.id.effortsSeekbar);
        profitabilitySeekbar = findViewById(R.id.profitabilitySeekbar);
        fitwithvisionSeekbar = findViewById(R.id.fitwithvisionSeekbar);

        addTaskBtn = findViewById(R.id.addTaskBtn);
        taskEt = findViewById(R.id.taskEt);

        //Text views
        impactTv = findViewById(R.id.impactTv);
        effortsTv = findViewById(R.id.effortTv);
        profitabilityTv = findViewById(R.id.profitabilityTv);
        fitwithvisionTv = findViewById(R.id.fitwithvisionTv);
    }
}
