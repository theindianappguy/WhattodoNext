package com.thendianappguy.whattodonext;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.thendianappguy.whattodonext.HelpingClass.SessionManagement;
import com.thendianappguy.whattodonext.login_register.LoginActivity;
import com.thendianappguy.whattodonext.login_register.RegisterActivity;

public class SplashActivity extends AppCompatActivity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    SessionManagement cookie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_splash);

        cookie = new SessionManagement(SplashActivity.this);

        if(cookie.isLoggedIn()){
            Intent mainIntent = new Intent(SplashActivity.this,MainActivity.class);
            startActivity(mainIntent);
            finish();
        }else{
            Intent mainIntent = new Intent(SplashActivity.this, RegisterActivity.class);
            startActivity(mainIntent);
            finish();
        }


        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
       /* new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                *//* Create an Intent that will start the Menu-Activity. *//*


            }
        }, SPLASH_DISPLAY_LENGTH);*/
    }
}
