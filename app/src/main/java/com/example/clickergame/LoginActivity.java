package com.example.clickergame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private EditText email,password;
    private TextView LtoReg;
    private Button login;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase db;
    private DatabaseReference myRef;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private LottieAnimationView check;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.lEmail);
        password = findViewById(R.id.lPass);
        LtoReg = findViewById(R.id.lToReg);
        login = findViewById(R.id.login);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        sharedpreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        check = findViewById(R.id.check);

    }


    public void onClickLogin(View v){
        String etxt = email.getText().toString();
        String ptxt = password.getText().toString();
        if(etxt.isEmpty() || ptxt.isEmpty() ){
            Toast.makeText(getBaseContext(),"Empty Fields!", Toast.LENGTH_SHORT).show();
        }
//        else if(user == null){
//            Toast.makeText(getBaseContext(),"Invalid!", Toast.LENGTH_SHORT).show();
//        }
        else{

            LogInUser(etxt,ptxt);

        }

        closeKeyboard();


    }

    private  void LogInUser(String email,String password){
    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
           if(task.isSuccessful()){
               login.setVisibility(View.INVISIBLE);
               Log.d("Inside LoginUser","Success");
               editor.putString("em",encode(email));
               editor.commit();


               Intent i = new Intent(LoginActivity.this,MainActivity.class);

               i.putExtra("email",encode(email));
               check.setVisibility(View.VISIBLE);

               check.playAnimation();
               check.addAnimatorListener(new AnimatorListenerAdapter() {
                   @Override
                   public void onAnimationEnd(Animator animation) {
                       super.onAnimationEnd(animation);
                       startActivity(i);
                       finish();
                       Toast.makeText(getBaseContext(),"Successfully Logged In!", Toast.LENGTH_SHORT).show();
                   }
               });

           }
           else if(!task.isSuccessful()){
               Toast.makeText(getBaseContext(),"Failed!", Toast.LENGTH_SHORT).show();
           }
        }
    });

    }
    public void jumpToRegsiter(View v){

        startActivity(new Intent(v.getContext(),RegisterActivity.class));
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