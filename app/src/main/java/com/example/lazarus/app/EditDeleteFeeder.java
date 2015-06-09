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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;


public class EditDeleteFeeder extends ActionBarActivity implements View.OnClickListener {

    private Button edit_button, delete_button, cancel_button;
    private EditText edit_location, zip_code, feeder_type, feeder_serial, feeder_vol, feeder_manu;
    private Spinner country_list;
    private String feeder_data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete_feeder);
        setTitle("Modify Feeder");
        edit_button = (Button) findViewById(R.id.edit_button);
        delete_button = (Button) findViewById(R.id.delete_button);
        cancel_button = (Button) findViewById(R.id.cancel_button);

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
        cancel_button.setOnClickListener(this);

        Log.v("EditDeleteFeeder", feeder_data);
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.edit_button:
                try {
                    new PostDataTask().execute();
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                    builder1.setMessage("Edit action was successful.");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton("Okay",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent i = new Intent(EditDeleteFeeder.this, UserMenu.class);
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
            case R.id.delete_button:
                try {
                    // WARNING:- DESTRUCTIVE ACTION. COMMENTED OUT FOR THE MOMENT
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                    builder2.setMessage("Delete Action was successful.");
                    builder2.setCancelable(true);
                    final AlertDialog alert2 = builder2.create();


                    AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                    builder1.setMessage("Are you sure you want to delete this item?");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    new PostDeleteTask().execute();
                                    alert2.show();
                                    launchIntent();
                                }
                            });
                    builder1.setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.cancel_button:
                startActivity(new Intent(this, UserMenu.class));
                break;
        }
    }

    private void launchIntent() {
        this.finish();
        startActivity(new Intent(this, UserMenu.class));
    }

    private class PostDeleteTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                DeleteFeeder();
                return "Success";
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.v("EditDeleteFeeder", "On Post Delete Executed Successfully!!");
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
            Log.v("EditDeleteFeeder", "On Post Edit Executed Successfully!!");
        }
    }

    public  void  GetText()  throws UnsupportedEncodingException
    {
        HttpURLConnection connection;
        OutputStreamWriter request;

        Intent intent = getIntent();
        feeder_data = intent.getStringExtra("FEEDER_DATA");
        String[] split_data = feeder_data.split(",");


        URL url = null;
        String response = null;
        String parameters = "FeederSN="+feeder_serial.getText().toString()
                +"&location="+edit_location.getText().toString()
                +"&countryID="+(country_list.getSelectedItemPosition() + 1)
                +"&zipcode="+zip_code.getText().toString()
                +"&FeederType="+feeder_type.getText().toString()
                +"&FeederManu="+feeder_manu.getText().toString()
                +"&FeederVolume="+feeder_vol.getText().toString()
                +"&Submit=true";

        Log.v("EditDeleteFeeder", parameters);
        try
        {
            url = new URL("http://rfid.hummingbirdhealth.org/android.php?feeder=edit&fid="+split_data[1]+"&uid="+split_data[split_data.length - 1]);
            Log.v("EditDeleteFeeder", "http://rfid.hummingbirdhealth.org/android.php?feeder=edit&fid="+split_data[1]);
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
            Log.v("EditDeleteFeeder", "response = " + response);
            isr.close();
            reader.close();

        }
        catch(Exception e)
        {
            // Error
            e.printStackTrace();
            Log.v("EditDeleteFeeder", "Failed to execute POST on EDIT action.");
        }

    }

    public  void  DeleteFeeder()  throws UnsupportedEncodingException
    {
        HttpURLConnection connection;
        OutputStreamWriter request;

        Intent intent = getIntent();
        feeder_data = intent.getStringExtra("FEEDER_DATA");
        String[] split_data = feeder_data.split(",");

        String parameters = "";
        URL url = null;
        String response = null;

        try
        {
            url = new URL("http://rfid.hummingbirdhealth.org/android.php?feeder=delete&fid="+split_data[1]+"&uid="+split_data[split_data.length - 1]);
            Log.v("EditDeleteFeeder", "http://rfid.hummingbirdhealth.org/android.php?feeder=edit&fid="+split_data[1]);
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
            Log.v("EditDeleteFeeder", "response = " + response);
            isr.close();
            reader.close();

        }
        catch(Exception e)
        {
            // Error
            e.printStackTrace();
            Log.v("EditDeleteFeeder", "Failed to execute POST on DELETE action.");
        }

    }

}
