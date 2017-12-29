package com.example.h_mal.shift_tracker;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by h_mal on 27/06/2017.
 */

public class LoginSetup extends Activity {

    private EditText mPasswordInitialEditText;
    private EditText mPasswordCheckEditText;

    public SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_setup);

        mPasswordInitialEditText = (EditText) findViewById(R.id.loginPasswordInitial);
        mPasswordCheckEditText = (EditText) findViewById(R.id.loginPasswordCheck);

        Button login = (Button) findViewById(R.id.loginButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickLogin();

            }
        });


    }

    private void clickLogin() {

        String initialPasswordString = mPasswordInitialEditText.getText().toString().trim();
        String checkPasswordString = mPasswordCheckEditText.getText().toString().trim();

        if (
                TextUtils.isEmpty(initialPasswordString) ||
                        TextUtils.isEmpty(checkPasswordString) ) {
            Toast.makeText(LoginSetup.this, "please set password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!initialPasswordString.equals(checkPasswordString)){
            Toast.makeText(LoginSetup.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;}

        if(initialPasswordString.equals(checkPasswordString)){
            SharedPreferences settings = getSharedPreferences("PREFS", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("password", initialPasswordString);
            editor.apply();

        }

        Intent i = new Intent(LoginSetup.this, MainActivity.class);
        startActivity(i);

    }

}
