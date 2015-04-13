package com.example.lazarus.app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class SignUp extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void clickFunction(View v){
        Intent intent = new Intent(this, Login.class);

        switch (v.getId()){
            case R.id.login_button:
                startActivity(intent);
                break;
            case R.id.already_member:
                startActivity(intent);
                break;
        }
    }
}
