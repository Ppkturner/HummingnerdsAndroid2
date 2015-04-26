package com.example.lazarus.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;


public class SignUp extends ActionBarActivity {

    EditText name, email, pass, confirm_pass,organization;
    RadioGroup user_type;
    String Name, Email, Pass, Confirm_Pass, Organization, User_Type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Grab all the entered fields from the xml

        name = (EditText) findViewById(R.id.usernameField);
        email = (EditText) findViewById(R.id.emailAddressField);
        pass = (EditText) findViewById(R.id.passwordField);
        confirm_pass = (EditText) findViewById(R.id.confirmPasswordField);
        organization = (EditText) findViewById(R.id.organization);
        user_type = (RadioGroup) findViewById(R.id.radioGroup);
    }

    public void clickFunction(View v){
        Intent intent = new Intent(this, Login.class);

        switch (v.getId()){
            case R.id.login_button:
                try {
                    new PostDataTask().execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                startActivity(intent);
                break;
            case R.id.already_member:
                startActivity(intent);
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
            Log.v("RegisterActivity", "SUCCESSS!!");
        }
    }


    public  void  GetText()  throws UnsupportedEncodingException
    {
        // Get user defined values
        Name = name.getText().toString();
        Email = email.getText().toString();
        Pass = pass.getText().toString();
        Confirm_Pass = confirm_pass.getText().toString();
        Organization = organization.getText().toString();

        RadioButton radio_button = (RadioButton)user_type.findViewById(user_type.getCheckedRadioButtonId());
        User_Type = radio_button.getText().toString();

        HttpURLConnection connection;
        OutputStreamWriter request;

        URL url = null;
        String response = null;
        String parameters = "name="+Name
                            +"&email="+Email
                            +"&gid="+User_Type
                            +"&org="+Organization
                            +"&pass="+Pass
                            +"&pass2="+Confirm_Pass;
        Log.v("LoginActivity", parameters);
        try
        {
            url = new URL("http://www.193.dwellis.com/android.php?user=register");
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
            // Response from server after login process will be stored in response variable.
            response = sb.toString();
            Log.v("SignUpActivity", "response = " + response);
            // You can perform UI operations here
            //Toast.makeText(this, "Message from Server: \n" + response, 0).show();
            isr.close();
            reader.close();

        }
        catch(IOException e)
        {
            // Error
        }
        // Show response on activity
        //content.setText( text  );
    }
}
