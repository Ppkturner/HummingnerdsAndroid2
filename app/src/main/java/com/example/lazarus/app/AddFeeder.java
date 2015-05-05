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
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;


public class AddFeeder extends ActionBarActivity implements View.OnClickListener {

    private Button add_button, cancel_button;
    private EditText edit_location, zip_code, feeder_type, feeder_serial, feeder_vol, feeder_manu, feeder_freq;
    private Spinner country_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_feeder);
        add_button = (Button) findViewById(R.id.add_button);
        cancel_button = (Button) findViewById(R.id.cancel_button);

        // Find the specific buttons
        edit_location = (EditText) findViewById(R.id.edit_location);
        zip_code = (EditText) findViewById(R.id.zip_code);
        feeder_type = (EditText) findViewById(R.id.feeder_type);
        feeder_serial = (EditText) findViewById(R.id.feeder_serial);
        feeder_vol = (EditText) findViewById(R.id.feeder_vol);
        feeder_manu = (EditText) findViewById(R.id.feeder_manu);
        country_list = (Spinner) findViewById(R.id.country_list);
        feeder_freq = (EditText) findViewById(R.id.feeder_freq);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.countries_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        country_list.setAdapter(adapter);

        // United States default country
        country_list.setSelection(0);

        add_button.setOnClickListener(this);
        cancel_button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.add_button:
                try {
                    new PostDataTask().execute();
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                    builder1.setMessage("Successfully added Feeder.");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton("Okay",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent i = new Intent(AddFeeder.this, UserMenu.class);
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


        URL url = null;
        String response = null;
        String parameters = "FeederSN="+feeder_serial.getText().toString()
                +"&location="+edit_location.getText().toString()
                +"&freq="+feeder_freq.getText().toString()
                +"&country="+(country_list.getSelectedItemPosition() + 1)
                +"&zipcode="+zip_code.getText().toString()
                +"&type="+feeder_type.getText().toString()
                +"&maker="+feeder_manu.getText().toString()
                +"&volume="+feeder_vol.getText().toString()
                +"&Submit=true";

        Log.v("AddFeeder", parameters);
        Intent intent = getIntent();
        String user_id = intent.getStringExtra("USER_PREFS_DATA");
        try
        {
            url = new URL("http://www.193.dwellis.com/android.php?feeder=add&uid="+user_id);
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
            Log.v("AddFeeder", "response = " + response);
            isr.close();
            reader.close();

        }
        catch(Exception e)
        {
            // Error
            e.printStackTrace();
            Log.v("AddFeeder", "Failed to execute POST on ADD action.");
        }

    }
}
