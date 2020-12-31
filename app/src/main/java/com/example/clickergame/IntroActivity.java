package com.example.clickergame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class IntroActivity extends AppCompatActivity {
    private Button login;
    private Button register;
    private String in;
    private SharedPreferences sharedpreferences;
    private String email;



    @Override
    protected void onStart() {
        super.onStart();
        sharedpreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);





        if(sharedpreferences.contains("em")){
            Log.d("inside","in");
            Intent i = new Intent(IntroActivity.this,MainActivity.class);
            email = sharedpreferences.getString("em",encode(""));
            i.putExtra("email",email);
            startActivity(i);
            finish();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        login = (Button) findViewById(R.id.toLogin);
        register = findViewById(R.id.toRegister);





    }

    public void toLog(View v) {
        startActivity(new Intent(v.getContext(),LoginActivity.class));
        finish();

    }
    public void toReg(View v) {
        startActivity(new Intent(v.getContext(),RegisterActivity.class));
        finish();

    }

    private String encode(String email){
        email = email.replace('.','a').replace('#','b').replace('$','c').replace('[','d').replace(']','e');
        return email;


    }
}