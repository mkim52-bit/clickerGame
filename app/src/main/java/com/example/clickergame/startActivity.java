 package com.example.clickergame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

 public class startActivity extends AppCompatActivity {
    private LottieAnimationView bg;
    private TextView title;
    private SharedPreferences sharedpreferences;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        bg = findViewById(R.id.bg);
        title = findViewById(R.id.startTitle);
        sharedpreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        Animation fade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        title.startAnimation(fade);
    }


    public void swap(View v)  {
     if (sharedpreferences.contains("em")){
            Log.d("inside","in");
            Intent i = new Intent(startActivity.this,MainActivity.class);
            email = sharedpreferences.getString("em",encode(""));
            i.putExtra("email",email);
            startActivity(i);
            finish();
        }
        else{
            startActivity(new Intent(startActivity.this,IntroActivity.class));
            //overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

            finish();

        }





    }

    private String encode(String email){
        email = email.replace('.','a').replace('#','b').replace('$','c').replace('[','d').replace(']','e');
        return email;


    }


     public boolean isConnected() throws InterruptedException, IOException {
         String command = "ping -c 1 google.com";
         return Runtime.getRuntime().exec(command).waitFor() == 0;
     }








}