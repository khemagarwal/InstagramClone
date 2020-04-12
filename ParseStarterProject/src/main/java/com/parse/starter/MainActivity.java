/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnKeyListener {

  Boolean signUpModeActive=true;
  TextView login;
  EditText username;
  EditText password;

  public void signUpClicked(View view)
  {


    if(username.getText().toString().matches("") || password.getText().toString().matches(""))
    {
      Toast.makeText(this, "A username and password required", Toast.LENGTH_SHORT).show();
    }
    else
    {
      if(signUpModeActive)
      {

      ParseUser user=new ParseUser();
      user.setUsername(username.getText().toString());
      user.setPassword(password.getText().toString());

      user.signUpInBackground(new SignUpCallback() {
        @Override
        public void done(ParseException e) {
          if(e ==null)
          {
            Log.i("Signup","Success");
          }
          else
          {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
          }
        }
      });

      }

      else
      {
        ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
          @Override
          public void done(ParseUser user, ParseException e) {
            if(user !=null)
            {
              Log.i("login","Successfull");
            }
            else
            {
              Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
          }
        });
      }

    }

  }
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    login=findViewById(R.id.login);
    login.setOnClickListener(this);
    username=findViewById(R.id.username);
    password=findViewById(R.id.password);
    ImageView logo=findViewById(R.id.logo);
    RelativeLayout backgroundLayout=findViewById(R.id.backgroundLayout);
    logo.setOnClickListener(this);
    backgroundLayout.setOnClickListener(this);

    password.setOnKeyListener(this);


    
    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

  @Override
  public void onClick(View v) {

    if(v.getId() == R.id.login)
    {
      Button signUp=findViewById(R.id.signup);
      if(signUpModeActive)
      {
        signUpModeActive=false;
        signUp.setText("Login");
        login.setText("or Sign Up");
      }
      else
      {

        signUpModeActive=true;
        signUp.setText("Sign Up");
        login.setText("or Login");
      }
    }
    else if(v.getId() == R.id.logo || v.getId()== R.id.backgroundLayout)
    {
      InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
      inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }

  }

  @Override
  public boolean onKey(View v, int keyCode, KeyEvent event) {

    if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN )
    {
    signUpClicked(v);
    }
    else
    {

    }
    return false;
  }
}