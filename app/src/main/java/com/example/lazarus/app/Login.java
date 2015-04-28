package com.example.lazarus.app;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.lang.Character;


public class Login extends ActionBarActivity{
    TextView content;
    EditText email, pass;
    String Email, Pass;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        session = new SessionManager(getApplicationContext());
        email = (EditText)findViewById(R.id.usernameField);
        pass =  (EditText)findViewById(R.id.passwordField);
    }

    public void clickFunction(View v)
    {
        Intent intent = new Intent(this, SignUp.class);


        switch(v.getId()){
            case R.id.login_button:
                try {
                    new PostDataTask().execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                session.checkLogin();
                break;
            case R.id.link_to_register:
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
            Log.v("LoginActivity", "SUCCESSS!!");
        }
    }



    public  void  GetText()  throws UnsupportedEncodingException
    {
        // Get user defined values
        Email = email.getText().toString();
        Pass = pass.getText().toString();

        HttpURLConnection connection;
        OutputStreamWriter request;

        URL url = null;
        String response = null;
        String parameters = "email="+Email+"&password="+Pass;
        Log.v("LoginActivity", parameters);
        try
        {
            url = new URL("http://www.193.dwellis.com/android.php?user=login");
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

            int i = 0;
            String[] tokens = response.split("[,]");
            String token_gid = "";
            String token_uid = "";
            String temp = "";
            Log.v("LoginActivity", "value = " + (tokens[0] == response));
            if (!(tokens[0] == response)){
                String[] tokens1 = tokens[1].split("[:]");
                String[] tokens2 = tokens[2].split("[:]");
//                ((GlobalVariable)this.getApplication()).setGroup(tokens1[1]);
//                ((GlobalVariable)this.getApplication()).setUsername(tokens2[1]);
                for(int j = 0; j < tokens1[1].toCharArray().length; j++) {
//                    Log.v("LoginActivity", Character.toString(tokens[1].toCharArray()[j]));
                    if (Character.isDigit(tokens1[1].toCharArray()[j])){
                        temp += tokens1[1].toCharArray()[j];
                    }
                }
                token_gid = temp;
                temp = "";
                for(int j = 0; j < tokens2[1].toCharArray().length; j++){
                    if(Character.isDigit(tokens2[1].toCharArray()[j])){
                        temp += tokens2[1].toCharArray()[j];
                    }
                }
                token_uid = temp;
                temp = "";
                session.createLoginSession(token_uid, token_gid);
                Log.v("LoginActivity", "gid = " + token_gid + " uid = " + token_uid);
            }
            else if (tokens[0] == response){
                Log.v("LoginActivity", "response = " + response);
            }
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
