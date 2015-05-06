package com.example.lazarus.app;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Scott on 5/6/2015.
 */
public class FourthFragment extends Fragment implements View.OnClickListener{
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    private SessionManager session;

    private Button confirm_change;

    private EditText pass;

    private EditText confirm_pass;

    String pass_str, confirm_pass_str;

    public static FourthFragment create(int page){
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        FourthFragment fragment = new FourthFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);

        session = new SessionManager(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fourth_fragment, container, false);

        confirm_change = (Button) view.findViewById(R.id.logout_button);
        pass = (EditText) view.findViewById((R.id.passwordField));
        confirm_pass = (EditText) view.findViewById(R.id.confirmPasswordField);

        confirm_change.setOnClickListener(this);

        return(view);
    }

    public void onClick(View v){
        try{
            new PostDataTask().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        startActivity(new Intent(getActivity(), UserMenu.class));
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
            Log.v("UserSettings", "On Post Edit Executed Successfully!!");
        }
    }

    public  void  GetText()  throws UnsupportedEncodingException
    {
        HttpURLConnection connection;
        OutputStreamWriter request;

        URL url = null;
        String response = null;

        pass_str = pass.getText().toString();
        confirm_pass_str = confirm_pass.getText().toString();

        String parameters = "&password="+pass_str+"&password2="+confirm_pass_str;

        Log.v("UserSettings", parameters);
        try
        {
            url = new URL("http://www.193.dwellis.com/android.php?user=settings&uid=" + session.getUserDetail().get("KEY_UID"));
            Log.v("UserSettings", "http://www.193.dwellis.com/android.php?user=settings&uid="+session.getUserDetail().get("KEY_UID"));
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
            Log.v("UserSettings", "response = " + response);
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
}
