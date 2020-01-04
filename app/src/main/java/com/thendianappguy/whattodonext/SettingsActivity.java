package com.thendianappguy.whattodonext;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.thendianappguy.whattodonext.HelpingClass.SessionManagement;
import com.thendianappguy.whattodonext.login_register.ResetPass;

public class SettingsActivity extends AppCompatActivity {

    TextView usernametv, gmailtv, logout;
    ImageView backBtn;
    LinearLayout privacypolicy, contactus, resetpass;

    SessionManagement cookie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        cookie = new SessionManagement(this);
        usernametv = findViewById(R.id.usernametv);
        gmailtv = findViewById(R.id.gmailtv);
        resetpass = findViewById(R.id.resetpass);
        backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        logout = findViewById(R.id.logout);
        usernametv.setText(cookie.getUserName());
        gmailtv.setText(cookie.getUserEmail());

        privacypolicy = findViewById(R.id.privacypolicy);
        contactus = findViewById(R.id.contactus);

        resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, ResetPass.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cookie.logoutUser();
                Intent intent = new Intent(SettingsActivity.this,SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        privacypolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(getResources().getString(R.string. privacy_policy)); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "whattodonextofficial@gmail.com" });
                intent.putExtra(Intent.EXTRA_SUBJECT, "Hey I use WhatToDoNext");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(intent, ""));
            }
        });
    }
}
