package com.example.lazarus.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class NewBird extends ActionBarActivity  implements View.OnClickListener {

    Spinner spec_list, bird_age;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexMale;
    private RadioButton radioSexFemale;
    private EditText birdTag, birdBand;
    private Button add_button, cancel_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bird);

        add_button = (Button) findViewById(R.id.add_button);
        cancel_button = (Button) findViewById(R.id.cancel_button);

        radioSexGroup = (RadioGroup) findViewById(R.id.bird_radio_group);
        radioSexMale = (RadioButton) findViewById(R.id.radioMale);
        radioSexFemale = (RadioButton) findViewById(R.id.radioFemale);

        spec_list = (Spinner) findViewById(R.id.species_list);
        bird_age = (Spinner) findViewById(R.id.bird_age_list);

        birdTag = (EditText) findViewById(R.id.rfid_tag);
        birdBand = (EditText) findViewById(R.id.nbbl_band);

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

        add_button.setOnClickListener(this);
        cancel_button.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_bird, menu);
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

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.add_button:
                try {
                    new PostDataTask().execute();
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                    builder1.setMessage("Successfully added the Bird.");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton("Okay",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent i = new Intent(NewBird.this, UserMenu.class);
                                    startActivity(i);
                                    //finish();

                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    //startActivity(new Intent(this, UserMenu.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.cancel_button:
                startActivity(new Intent(this, UserMenu.class));
                break;
        }
    }

    private class PostDataTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                GetText();
                return "Success";
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.v("AddFeeder", "On Post Edit Executed Successfully!!");
        }
    }

    public  void  GetText()  throws UnsupportedEncodingException
    {
        HttpURLConnection connection;
        OutputStreamWriter request;

        // get selected radio button from radioGroup
        int selectedId = radioSexGroup.getCheckedRadioButtonId();
        RadioButton radioSexButton = (RadioButton) findViewById(selectedId);

        Intent intent = getIntent();
        String bird_data = intent.getStringExtra("USER_PREFS_DATA");

        URL url = null;
        String response = null;
        String parameters = "rfidTag="+birdTag.getText().toString()
                +"&species="+spec_list.getSelectedItem().toString().replaceAll(" ", "%20")
                +"&age="+bird_age.getSelectedItem().toString()
                +"&gender="+radioSexButton.getText().charAt(0) + ""
                +"&bandnum="+birdBand.getText().toString()
                +"&Submit=true";

        Log.v("AddBird", parameters);

        try
        {
            url = new URL("http://www.193.dwellis.com/android.php?bird=add&uid="+bird_data);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("POST");

            request = new OutputStreamWriter(connection.getOutputStream());
            request.write(parameters);
            request.flush();
            request.close();
            String line = "";
            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }

            response = sb.toString();
            Log.v("AddBird", "response = " + response);
            isr.close();
            reader.close();

        }
        catch(Exception e)
        {
            // Error
            e.printStackTrace();
            Log.v("AddBird", "Failed to execute POST on ADD action.");
        }

    }
}
