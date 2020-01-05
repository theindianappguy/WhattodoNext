package com.thendianappguy.whattodonext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.thendianappguy.whattodonext.Adapter.TaskAdapter;
import com.thendianappguy.whattodonext.Adapter.TaskRecycleViewAdapter;
import com.thendianappguy.whattodonext.Adapter.TasksAdapter;
import com.thendianappguy.whattodonext.CustomClass.TasksClass;
import com.thendianappguy.whattodonext.HelpingClass.FunctionClass;
import com.thendianappguy.whattodonext.HelpingClass.SessionManagement;
import com.thendianappguy.whattodonext.other.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION;

public class MainActivity extends AppCompatActivity implements BottomSheetDialog.BottomSheetListener {

    private static final String TAG = "MainActivity";

    private TextView addTask, name, greeting;
    private ImageView profile;

    RecyclerView tasksList;
    RecyclerView.LayoutManager layoutManager;

    LinearLayout noListingFound;

    private CollectionReference tasksRef = FirebaseFirestore.getInstance()
            .collection("Main");

    SessionManagement cookie;
    ArrayList<TasksClass> tasksClassArrayList;
    TaskAdapter mAdapter;

    BottomSheetDialog bottomSheetDialog;
    private AdView mAdView;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addTask = findViewById(R.id.addTask);
        profile = findViewById(R.id.profile);
        name = findViewById(R.id.name);
        greeting = findViewById(R.id.greeting);

        setGreeeting();

        cookie = new SessionManagement(this);
        tasksRef = tasksRef.document("UserTasks").collection(cookie.getUserUid());
        Log.e(TAG, "onCreate: " +cookie.getUserUid());

        String nameST = cookie.getUserName();
        name.setText(nameST);

        noListingFound = findViewById(R.id.noListingFound);
        noListingFound.setVisibility(View.GONE);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        setUpRecyclerView();

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(intent);
            }
        });

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTaskBottomSheet();
            }
        });
    }

    private void setGreeeting() {

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 12){
            greeting.setText("Good Morning");
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            greeting.setText("Good Afternoon");
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            greeting.setText("Good Evening");
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            greeting.setText("Good Night");
        }
    }

    private void setUpRecyclerView() {
        Query query = tasksRef.orderBy("total", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<TasksClass> options = new FirestoreRecyclerOptions.Builder<TasksClass>()
                .setQuery(query,TasksClass.class)
                .build();

        mAdapter = new TaskAdapter(MainActivity.this,options);
        tasksList = findViewById(R.id.tasksList);
        layoutManager = new LinearLayoutManager(this);
        tasksList.setLayoutManager(layoutManager);
        tasksList.setAdapter(mAdapter);

        checkAndSetEmptyView();


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder
                    viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mAdapter.deleteItem(viewHolder.getAdapterPosition());
                mAdapter.onDataChanged();
                checkAndSetEmptyView();
            }
        }).attachToRecyclerView(tasksList);

    }

    public void checkAndSetEmptyView() {
        SessionManagement sessionManagement = new SessionManagement(MainActivity.this);
        Log.e(TAG, "checkAndSetEmptyView: "+sessionManagement.getItemCount());
        if(sessionManagement.getItemCount() == 0){
            noListingFound.setVisibility(View.VISIBLE);
        }else{
            noListingFound.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    private void showAddTaskBottomSheet() {
        bottomSheetDialog = new BottomSheetDialog(MainActivity.this, "", 0, 0,
                0,0,"",true);
        bottomSheetDialog.show(getSupportFragmentManager(),"addTaskFragment");

    }

    /*private void getTaksFromCloud() {

        tasksClassArrayList = new ArrayList<>();
        tasksRef.orderBy("total", Query.Direction.DESCENDING)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    TasksClass tasksClass = documentSnapshot.toObject(TasksClass.class);
                    tasksClass.setTaskId(documentSnapshot.getId());
                    tasksClassArrayList.add(tasksClass);

                }

                if(tasksClassArrayList!=null){
                    mAdapter = new TaskRecycleViewAdapter(MainActivity.this,tasksClassArrayList);
                    new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(tasksList);
                    tasksList.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    if(tasksClassArrayList.size()==0){
                        noListingFound.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }*/




    @Override
    public void onButtonClicked(String text) {
        if(text.equals("close")){
            bottomSheetDialog.dismiss();
        }
    }

    public void showBottomSheetWithPrefilledDetails(String task, int impact, int effort,
                                                    int profitability,int fitwithvision,String taskId) {

        bottomSheetDialog = new BottomSheetDialog(MainActivity.this, task, impact, effort,
        profitability,fitwithvision,taskId,false);

        bottomSheetDialog.show(getSupportFragmentManager(),"addTaskFragment");
        Log.e(TAG, "showBottomSheetWithPrefilledDetails: "+taskId );
    }
}
