package com.example.lazarus.app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class FirstFragment extends Fragment implements View.OnClickListener {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    private SessionManager session;

    private String UserDashBoard;

    private Button button;

    public static FirstFragment create(int page){
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        FirstFragment fragment = new FirstFragment();
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
        View view = inflater.inflate(R.layout.first_fragment, container, false);
        TextView textView = (TextView) view.findViewById(R.id.listTextView);
        textView.setText("Fragment #" + mPage);

        button = (Button) view.findViewById(R.id.logout_button);

        button.setOnClickListener(this);
        return view;
    }

    public void onClick(View v)
    {
        session.logoutUser();
    }

    public void onActivityCreated(Bundle savedInstanceState){
        super. onActivityCreated(savedInstanceState);

        try {
            new PostDataTask().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  String  GetText()  throws UnsupportedEncodingException
    {
        // Get user defined values

        HttpURLConnection connection;
        OutputStreamWriter request;

        URL url = null;
        String response = null;
        HashMap<String, String> user_prefs = session.getUserDetail();
        String parameters = ""/*"uid=" + user_prefs.get("uid")*/;
        try
        {
            url = new URL("http://www.193.dwellis.com/android.php?user=dashboard&uid=" + user_prefs.get("uid"));
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("GET");

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
            Log.v("BirdActivity", "response = " + response);

            try {
                JSONObject jsonObj = new JSONObject(response);

                JSONArray feeders = jsonObj.getJSONArray("Feeders");

                for(int i = 0; i < feeders.length(); i++) {
                    JSONObject feeder = feeders.getJSONObject(i);
                    String loc = feeder.getString("location");
                    Log.v("UserCPActivitySuccess", "location " + loc);

                }
            }
            catch(Exception ex) {
                Log.v("UserCPActivitySuccess", "FAILED PARSING JSON!!!");
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
        return(response);
    }

    private class PostDataTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                UserDashBoard = GetText();
                return "Success";
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.v("UserCPActivitySuccess", UserDashBoard);
            TextView textView = (TextView) getView().findViewById(R.id.listTextView);
            textView.setText(UserDashBoard);

            try {
                UserDashBoard = GetText();


            } catch (IOException e) {
            }
        }
        }
}
