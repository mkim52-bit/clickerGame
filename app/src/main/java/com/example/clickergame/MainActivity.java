package com.example.clickergame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity  {
    private TextView score,item1,item2,item3;
    private boolean stopHandler = false;
    private boolean skip= false;
    private int in;
    private Handler handler;
    private Runnable runnable;
    private String username,ID;

     private FirebaseAuth auth;
     private FirebaseUser user;
    private FirebaseDatabase db;
    private DatabaseReference myRef;

    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;

    int sec = 0;
    int count = 0;
    int multiplier1 = 0;
    int multiplier2 = 0;
    int multiplier3 = 0;
    String scoreCount = "";

    @Override
    protected void onRestart() {
        super.onRestart();

            stopHandler = false;
            handler.post(runnable);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        score = findViewById(R.id.Score);
        item1 = findViewById(R.id.item1);
        item2 = findViewById(R.id.item2);
        item3 = findViewById(R.id.item3);
        sharedpreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        username = getIntent().getStringExtra("email");
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseDatabase.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("users").child(username);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count = Integer.parseInt(snapshot.child("count").getValue().toString());
                scoreCount = String.valueOf(count);
                score.setText("Score: " + scoreCount);
                multiplier1 = Integer.parseInt(snapshot.child("item1").getValue().toString());
                multiplier2 = Integer.parseInt(snapshot.child("item2").getValue().toString());
                multiplier3 = Integer.parseInt(snapshot.child("item3").getValue().toString());

                ID = snapshot.child("username").getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getBaseContext(),"Data Failed to Retrieve", Toast.LENGTH_SHORT).show();
            }
        });

        Toast.makeText(getBaseContext(),"Welcome " + ID, Toast.LENGTH_SHORT).show();



//        item1.setVisibility(View.INVISIBLE);
//        item2.setVisibility(View.INVISIBLE);
//        item3.setVisibility(View.INVISIBLE);







        handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    count = count + (1 * multiplier1) + (5*multiplier2) + (3*multiplier3);//fine for 0 because count will never be 0 when clicked
                    scoreCount = String.valueOf(count);
                    score.setText("Score: " + scoreCount);
                    sec++;
                    if(sec%5 == 0){
                        FirebaseDatabase.getInstance().getReference().child("users").child(username).child("count").setValue(count);
                        FirebaseDatabase.getInstance().getReference().child("users").child(username).child("item1").setValue(multiplier1);
                        FirebaseDatabase.getInstance().getReference().child("users").child(username).child("item2").setValue(multiplier2);
                        FirebaseDatabase.getInstance().getReference().child("users").child(username).child("item3").setValue(multiplier3);

                    }


                    if (!stopHandler) {
                        handler.postDelayed(this, 1000);
                    }
                }
            };

        if (skip == false) {

            handler.post(runnable);
            skip = true;
        }
   }

    @Override
    protected void onStop() {
        super.onStop();
        stopHandler = true;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        editor.putBoolean("in",true);
    }

    public void signOut(View v){
        editor.remove("em");
        editor.commit();
        stopHandler = true;
        auth.signOut();
        Intent intent = new Intent(MainActivity.this,IntroActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();



    }


    public void onClick(View v){


        count++;
        scoreCount = String.valueOf(count);
        score.setText("Score: " + scoreCount);



    }

//items must subtract cookies and price becomes exponential
//items must continue after restarting
    public void itemOne(View V) {//auto increment by 1
            count = count - 5;//experimental
        scoreCount = String.valueOf(count);
        score.setText("Score: " + scoreCount);
        //toast message of cost
        if(count < 0){
            count = 0;
        }
            multiplier1++;//goes to 1
            item1.setText("Item1: " + multiplier1);


    }

    public void itemTwo(View V) {//auto increment by 5
        count = count - 10;//experimental
        scoreCount = String.valueOf(count);
        score.setText("Score: " + scoreCount);
        //toast message of cost
        if(count < 0){
            count = 0;
        }
        multiplier2+= 5;//goes to 1
        item2.setText("Item1: " + multiplier2);


    }
    public void itemThree(View V) {
        count = count - 50;//experimental
        scoreCount = String.valueOf(count);
        score.setText("Score: " + scoreCount);
        //toast message of cost
        if(count < 0){
            count = 0;
        }
        multiplier3+=10;//goes to 1
        item3.setText("Item3: " + multiplier3);


    }







}



