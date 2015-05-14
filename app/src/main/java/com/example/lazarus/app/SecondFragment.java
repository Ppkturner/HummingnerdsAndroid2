package com.example.lazarus.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Scott on 4/27/2015.
 */

public class SecondFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    private String BirdData = new String();

    private SessionManager session;

    //Creating an array to store the bird information
    private ArrayList<String> birdsArray;
    private ArrayList<String> masterBirdArray;
    private ListView birdListView;  //List view to display the bird data

    private Button add_bird_butt;

    public static SecondFragment create(int page){
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        SecondFragment fragment = new SecondFragment();
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
        View view = inflater.inflate(R.layout.second_fragment, container, false);
        TextView textView = (TextView) view.findViewById(R.id.listTextView);

        Log.v("BirdActivityOnCreate", BirdData);


        //textView.setText(BirdData);

        //Initialized the array list
        birdsArray = new ArrayList<String>();
        masterBirdArray = new ArrayList<String>();
        // Find the listview from the xml
        birdListView = (ListView) view.findViewById(R.id.birdList);
        birdListView.setOnItemClickListener(birdClickHandler);


        add_bird_butt = (Button) view.findViewById(R.id.add_bird_button);
        add_bird_butt.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Log.v(ThirdFragment.class.getSimpleName(), "Add Bird  Button was clicked.");
                Intent i = new Intent(getActivity(), NewBird.class);
                HashMap<String, String> user_prefs = session.getUserDetail();
                i.putExtra("USER_PREFS_DATA", user_prefs.get("uid"));
                startActivity(i);
            }
        });

        if (Integer.parseInt(session.getUserDetail().get("gid")) == 0){
            add_bird_butt.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
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
        HashMap <String, String> user_prefs = session.getUserDetail();
        String parameters = ""/*"uid=" + user_prefs.get("uid")*/;
        try
        {
            url = new URL("http://www.193.dwellis.com/android.php?bird=list&uid=" + user_prefs.get("uid"));
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

            try
            {
                //convert the response string to json
                JSONObject jsonObj = new JSONObject(response);

                //Get the json array of birds
                JSONArray birds = jsonObj.getJSONArray("Birds");
                for(int i = 0; i < birds.length(); i++)
                {
                    JSONObject bird = birds.getJSONObject(i);
                    String birdID = bird.getString("BID");
                    String gender = bird.getString("gender");
                    String age = bird.getString("age");
                    String feederID = bird.getString("FID");
                    String name = bird.getString("Name");
                    String taggedDate = bird.getString("TaggedDate");
                    String bandNumb = bird.getString("NBBLBandNumb");
                    String tagType = bird.getString("TagType");
                    String rfidTag = bird.getString("rfidTagId");
                    masterBirdArray.add(birdID +"," + gender + "," + age + "," + feederID + "," + name + "," + taggedDate + "," + bandNumb + "," + tagType + "," + rfidTag);
                    birdsArray.add("BID: " + birdID +"| Gender: " + gender + "| Age: " + age);
                    Log.v("BirdActivity", birdID +" " + gender + " " + age + " " + feederID + " " + name + " " + taggedDate + " " + bandNumb + " " + tagType);

                }
            }
            catch (Exception ex)
            {
                Log.v("BirdActivity", "FAILED PARSING JSON!!!");
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
                BirdData = GetText();
                return "Success";
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.v("BirdsActivitySuccess", BirdData);
            // TextView textView = (TextView) getView().findViewById(R.id.listTextView);

            //textView.setText(BirdData);
            ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_expandable_list_item_1, birdsArray);
            birdListView.setAdapter(arrayAdapter1);
            ListUtils_bird.setDynamicHeight(birdListView);
        }
    }

    private AdapterView.OnItemClickListener birdClickHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id)
        {
            HashMap<String, String> user_prefs = session.getUserDetail();
            if(Integer.parseInt(user_prefs.get("gid")) != 0){
                Log.v("SecondFragment", "masterbirdarray[position] = " + masterBirdArray.get(position));
                Intent i = new Intent(getActivity(), EditDeleteBirds.class);
                i.putExtra("BIRD_DATA", masterBirdArray.get(position) + "," + user_prefs.get("uid"));
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                getActivity().finish();
            }
        }
    };

    public static class ListUtils_bird {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }
}