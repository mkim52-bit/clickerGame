package com.example.clickergame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.MultiSelectListPreference;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private EditText email,password,username;
    private TextView alreadyUser;
    private Button reg;
    private LottieAnimationView checker;
    private FirebaseDatabase db;
    private DatabaseReference myRef;
    private FirebaseUser user;//prob dont need
    private FirebaseAuth auth;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email = findViewById(R.id.email);
        password = findViewById(R.id.pass);
        username = findViewById(R.id.username);
        alreadyUser = findViewById(R.id.alreadyUser);
        reg = findViewById(R.id.register);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        myRef = db.getReference();
        sharedpreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        checker = findViewById(R.id.checker);


    }

    public void onClickRegister(View v){
        String etxt = email.getText().toString();
        String ptxt = password.getText().toString();
        String userID = username.getText().toString();


                if(etxt.isEmpty()|| ptxt.length() < 6){
                    Log.d("Inside onClick","empty");
                    Toast.makeText(getBaseContext(),"Too Short!", Toast.LENGTH_SHORT).show();

                }
                else if(!etxt.contains("@") || !etxt.contains(".com")){
                    Log.d("Inside onClick","no @");
                    Toast.makeText(getBaseContext(),"Proper Email!", Toast.LENGTH_SHORT).show();

                }
                else if(user != null){
                    Toast.makeText(getBaseContext(),"Already Exists!", Toast.LENGTH_SHORT).show();
                }
                else{
                registerUser(etxt,ptxt,userID);
                    Log.d("Inside onClick","done with register");
                }
                closeKeyboard();





    }

    private void registerUser(String email, String password, String userID){
    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {

        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()){
                reg.setVisibility(View.INVISIBLE);
                editor.putString("em",encode(email));
                editor.commit();
                myRef.child("users").child(encode(email));
                myRef.child("users").child(encode(email)).child("username").setValue(userID);
                myRef.child("users").child(encode(email)).child("count").setValue(0);
                myRef.child("users").child(encode(email)).child("item1").setValue(0);
                myRef.child("users").child(encode(email)).child("item2").setValue(0);
                myRef.child("users").child(encode(email)).child("item3").setValue(0);



                user = auth.getCurrentUser();
                Intent i = new Intent(RegisterActivity.this,MainActivity.class);
                i.putExtra("email",encode(email));
                checker.setVisibility(View.VISIBLE);
                checker.playAnimation();
                checker.addAnimatorListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        startActivity(i);
                        finish();
                        Toast.makeText(getBaseContext(),"Register Complete!", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.d("Inside registerUser","Success");
            }
            else{
                Toast.makeText(getBaseContext(),"Failed", Toast.LENGTH_SHORT).show();
                Log.d("Inside registerUser","Failed");
            }
        }
    });
    }

    public void jumpToLogin(View v){

                startActivity(new Intent(v.getContext(),LoginActivity.class));
                finish();



    }

    private String encode(String email){
         email = email.replace('.','a').replace('#','b').replace('$','c').replace('[','d').replace(']','e');
         return email;


    }

    public void closeKeyboard() {
        // this will give us the view
        // which is currently focus
        // in this layout
        View view = this.getCurrentFocus();

        // if nothing is currently
        // focus then this will protect
        // the app from crash
        if (view != null) {

            // now assign the system
            // service to InputMethodManager
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}