package com.thendianappguy.whattodonext.login_register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.thendianappguy.whattodonext.CustomClass.UserClass;
import com.thendianappguy.whattodonext.HelpingClass.SessionManagement;
import com.thendianappguy.whattodonext.MainActivity;
import com.thendianappguy.whattodonext.R;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private FirebaseAuth mAuth;
    EditText usernameEt, emailEt, passwordEt;
    private RelativeLayout registerUserBtn;
    private ProgressBar progressBar;

    TextView loginBtn;

    SessionManagement cookie;
    String nameSt;

    /** This is to upload UserInfo on Cloud */
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        defineViews();
        otherDefinings();

        settingOnClickListner();

    }

    private void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });
    }

    private void settingOnClickListner() {

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
               finish();
            }
        });

        registerUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    String emailString = emailEt.getText().toString();
                    String passwordString = passwordEt.getText().toString();
                    nameSt = usernameEt.getText().toString();
                    createUser(emailString, passwordString);
                } else {
                    Toast.makeText(RegisterActivity.this, "Make sure all feild are filled", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void otherDefinings() {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        cookie = new SessionManagement(this);
    }

    private void defineViews() {
        usernameEt = findViewById(R.id.userNameEt);
        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        registerUserBtn = findViewById(R.id.registerUserBtn);

        loginBtn = findViewById(R.id.loginBtn);
    }

    private boolean validate() {

        boolean valid = true;

        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();

        if (usernameEt.equals("")) {
            valid = false;
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEt.setError("enter a valid email address");
            valid = false;
        } else {
            emailEt.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordEt.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordEt.setError(null);
        }

        return valid;
    }

    /** This is to make sure if the user is logged in show main */
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            uploadUserDataToCloud();
            cookie.setUserName(nameSt);
            cookie.createLoginSession(currentUser.getEmail(),currentUser.getUid());
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

    }

    private void uploadUserDataToCloud() {

        String name = usernameEt.getText().toString();
        String emailSt = emailEt.getText().toString();

        UserClass userClass = new UserClass();
        userClass.setName(name);
        userClass.setEmail(emailSt);

        db.collection("UserInfo").document(Objects.requireNonNull(mAuth.getUid()))
                .set(userClass).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(RegisterActivity.this, "Saved Succesfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + e.toString());
            }
        });


    }
}
