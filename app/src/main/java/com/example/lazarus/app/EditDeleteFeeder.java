package com.example.lazarus.app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;


public class EditDeleteFeeder extends ActionBarActivity implements View.OnClickListener {

    Button edit_button, delete_button;
    EditText edit_location, zip_code, feeder_type, feeder_serial, feeder_vol, feeder_manu;
    Spinner country_list;

    String feeder_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete_feeder);
        edit_button = (Button) findViewById(R.id.edit_button);
        delete_button = (Button) findViewById(R.id.delete_button);

        // Find the specific buttons
        edit_location = (EditText) findViewById(R.id.edit_location);
        zip_code = (EditText) findViewById(R.id.zip_code);
        feeder_type = (EditText) findViewById(R.id.feeder_type);
        feeder_serial = (EditText) findViewById(R.id.feeder_serial);
        feeder_vol = (EditText) findViewById(R.id.feeder_vol);
        feeder_manu = (EditText) findViewById(R.id.feeder_manu);
        country_list = (Spinner) findViewById(R.id.country_list);

        Intent intent = getIntent();

        feeder_data = intent.getStringExtra("FEEDER_DATA");

        String[] split_data = feeder_data.split(",");

        edit_location.setText(split_data[0]);
        zip_code.setText(split_data[2]);
        feeder_manu.setText(split_data[3]);
        feeder_vol.setText(split_data[4]);
        feeder_type.setText(split_data[5]);
        feeder_serial.setText(split_data[6]);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.countries_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        country_list.setAdapter(adapter);

        country_list.setSelection(Integer.parseInt(split_data[7]) - 1);

        edit_button.setOnClickListener(this);
        delete_button.setOnClickListener(this);



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
