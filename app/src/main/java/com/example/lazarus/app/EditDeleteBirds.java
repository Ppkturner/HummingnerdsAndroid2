package com.example.lazarus.app;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;


public class EditDeleteBirds extends ActionBarActivity {

    Spinner spec_list, bird_age;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexMale;
    private RadioButton radioSexFemale;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete_birds);

        radioSexGroup = (RadioGroup) findViewById(R.id.bird_radio_group);
        radioSexMale = (RadioButton) findViewById(R.id.radioMale);
        radioSexFemale = (RadioButton) findViewById(R.id.radioFemale);

        spec_list = (Spinner) findViewById(R.id.species_list);
        bird_age = (Spinner) findViewById(R.id.bird_age_list);

        ArrayList<Integer> x = new ArrayList<Integer>();
        for(int i = 0; i < 19; i++) x.add(i + 1);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, x);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bird_age.setAdapter(adapter);

        ArrayList<String> tempSpecies = new ArrayList<String>();
        tempSpecies.add("Temp Species");
        ArrayAdapter<String> speciesAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, tempSpecies);
        speciesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spec_list.setAdapter(speciesAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_delete_birds, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
