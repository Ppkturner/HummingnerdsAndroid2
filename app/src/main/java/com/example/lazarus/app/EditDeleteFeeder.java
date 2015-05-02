package com.example.lazarus.app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class EditDeleteFeeder extends ActionBarActivity implements View.OnClickListener {

    Button edit_button, delete_button;

    String feeder_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete_feeder);
        edit_button = (Button) findViewById(R.id.edit_button);
        delete_button = (Button) findViewById(R.id.delete_button);

        edit_button.setOnClickListener(this);
        delete_button.setOnClickListener(this);

        Intent intent = getIntent();

        feeder_data = intent.getStringExtra("FEEDER_DATA");

        Log.v("EditActivity", feeder_data);
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.edit_button:
                break;
            case R.id.delete_button:
                break;
        }
    }

}
