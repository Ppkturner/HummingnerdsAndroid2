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
import java.util.List;


public class EditDeleteBirds extends ActionBarActivity  implements View.OnClickListener {

    Spinner spec_list, bird_age;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexMale;
    private RadioButton radioSexFemale;
    private EditText birdTag, birdBand;
    private Button edit_button, delete_button, cancel_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete_birds);
        setTitle("Modify Birds");

        edit_button = (Button) findViewById(R.id.edit_button);
        delete_button = (Button) findViewById(R.id.delete_button);
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

        Intent i = getIntent();
        String bird_data = i.getStringExtra("BIRD_DATA");
        String[] split_data = bird_data.split(",");
        // 0 --> birdId
        // 1 --> gender
        // 2 --> age
        // 3 --> feederId
        // 4 --> name
        // 5 --> taggeddate
        // 6 --> bandNum
        // 7 --> tagType
        // 8 --> RfidTag
        bird_age.setSelection(Integer.parseInt(split_data[2]) - 1);
        // Get the gender
        // If M, then check the male button
        // Else, check the female radio button
        if(split_data[1].equals("M")) {
            radioSexMale.setChecked(true);
            //radioSexFemale.setChecked(false);
        }
        if(split_data[1].equals("F")) {
            radioSexFemale.setChecked(true);
            //radioSexFemale.setChecked(false);
        }

        birdTag.setText(split_data[6]);
        birdBand.setText(split_data[8]);

        edit_button.setOnClickListener(this);
        delete_button.setOnClickListener(this);
        cancel_button.setOnClickListener(this);
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
                                    Intent i = new Intent(EditDeleteBirds.this, UserMenu.class);
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
            } catch (Exception ex) {
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
        String bird_data = intent.getStringExtra("BIRD_DATA");
        String[] split_data = bird_data.split(",");

        // 0 --> birdId
        // 1 --> gender
        // 2 --> age
        // 3 --> feederId
        // 4 --> name
        // 5 --> taggeddate
        // 6 --> bandNum
        // 7 --> tagType
        // 8 --> RfidTag

        // get selected radio button from radioGroup
        int selectedId = radioSexGroup.getCheckedRadioButtonId();
        RadioButton radioSexButton = (RadioButton) findViewById(selectedId);

        URL url = null;
        String response = null;
        String parameters = "rfidTag="+birdTag.getText().toString()
                +"&species="+spec_list.getSelectedItem().toString().replaceAll(" ", "%20")
                +"&age="+bird_age.getSelectedItem().toString()
                +"&gender="+radioSexButton.getText().charAt(0) + ""
                +"&bandnum="+birdBand.getText().toString()
                +"&Submit=true";

        Log.v("EditDeleteBirds", parameters);
        try
        {
            //url = new URL("bla");
            url = new URL("http://rfid.hummingbirdhealth.org/android.php?bird=edit"+"&bid="+split_data[0]+"&uid="+split_data[split_data.length - 1]);

            Log.v("EditDeleteFeeder", "http://rfid.hummingbirdhealth.org/android.php?bird=edit&bid="+split_data[0]);
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
            Log.v("EditDeleteBirds", "response = " + response);
            isr.close();
            reader.close();

        }
        catch(Exception e)
        {
            // Error
            e.printStackTrace();
            Log.v("EditDeleteBirds", "Failed to execute POST on EDIT action.");
        }

    }

   public  void  DeleteFeeder()  throws UnsupportedEncodingException
    {
        HttpURLConnection connection;
        OutputStreamWriter request;

        Intent intent = getIntent();
        String bird_data = intent.getStringExtra("BIRD_DATA");
        String[] split_data = bird_data.split(",");

        String parameters = "";
        URL url = null;
        String response = null;

        Log.v("Delete Bird", split_data[0]);

        try
        {
            url = new URL("http://rfid.hummingbirdhealth.org/android.php?bird=delete&bid="+split_data[0]+"&uid="+split_data[split_data.length - 1]);
            Log.v("EditDeleteBird", "http://www.193.dwellis.com/android.php?feeder=edit&bid="+split_data[0]);
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
            Log.v("EditDeleteBirds", "response = " + response);
            isr.close();
            reader.close();

        }
        catch(Exception e)
        {
            // Error
            e.printStackTrace();
            Log.v("EditDeleteBirds", "Failed to execute POST on DELETE action.");
        }

    }
}
