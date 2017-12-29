package com.example.h_mal.shift_tracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by h_mal on 27/06/2017.
 */

public class LoginScreen extends AppCompatActivity{

    private EditText mPasswordEditText;

    String password;

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        SharedPreferences settings = getSharedPreferences("PREFS", 0);
        password = settings.getString("password", "");

        mPasswordEditText = (EditText) findViewById(R.id.loginPassword);

        Button login = (Button) findViewById(R.id.loginButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
                mPasswordEditText.getText().clear();

            }
        });


    }

    private void login() {
        String PasswordString = mPasswordEditText.getText().toString().trim();

        if (
                TextUtils.isEmpty(PasswordString)  ) {
            Toast.makeText(LoginScreen.this, "please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        if(PasswordString.equals(password)){
            Intent i = new Intent(LoginScreen.this, MainActivity.class);
            startActivity(i);
        }else{
            addCount();
            Toast.makeText(LoginScreen.this, "password incorrect " + (4 - count) + " tries left", Toast.LENGTH_SHORT).show();

        }
    }

    private void addCount(){
        count = count + 1;
        if (count == 4){
            finish();
            System.exit(0);
        }
    }


}
