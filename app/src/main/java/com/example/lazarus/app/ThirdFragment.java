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
import java.util.List;

/**
 * Created by Scott on 4/27/2015.
 */

public class ThirdFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    // Private session manager
    private SessionManager session;
    // Add feeder button
    private Button add_button;
    // ListView for feeder list
    private ListView lv1;

    // Arraylist used for feeder list
    private ArrayList<String> feederArray;

    // Arraylist used to store all data of the feeder
    private ArrayList<String> masterDataFeeder;


    public static ThirdFragment create(int page){
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        ThirdFragment fragment = new ThirdFragment();
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
        View view = inflater.inflate(R.layout.third_fragment, container, false);
        TextView textView = (TextView) view.findViewById(R.id.listTextView);
        textView.setText("Fragment #" + mPage);

        add_button = (Button) view.findViewById(R.id.add_feeder_button);

        // Find the listview from the xml
        lv1 = (ListView) view.findViewById(R.id.feederList);

        // Initialize the Feeder ArrayList
        feederArray = new ArrayList<String>();
        // Initialize the masterdata arraylist
        masterDataFeeder = new ArrayList<String>();


        // Attach Click listeners for both the lists
        lv1.setOnItemClickListener(feederClickHandler);

        add_button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            Log.v(ThirdFragment.class.getSimpleName(), "Add Feeder Button was clicked.");
            Intent i = new Intent(getActivity(), AddFeeder.class);
            HashMap<String, String> user_prefs = session.getUserDetail();
            i.putExtra("USER_PREFS_DATA", user_prefs.get("uid"));
            startActivity(i);
            }
        });

        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

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
            url = new URL("http://www.193.dwellis.com/android.php?feeder=list&uid=" + user_prefs.get("uid"));
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
            Log.v("ThirdFragment", "response = " + response);

            try {

                //Parsing JSON, Will need to be place at appropriate location
                JSONObject jsonObj = new JSONObject(response);

                //Getting the feeder information
                JSONArray feeders = jsonObj.getJSONArray("Feeders");
                List<String> fList = new ArrayList<String>();
                for(int i = 0; i < feeders.length(); i++) {
                    JSONObject feeder = feeders.getJSONObject(i);
                    String loc = feeder.getString("location"); //This get the location of the feeder
                    String feederID = feeder.getString("FID"); //Feeder ID
                    String zipcode = feeder.getString("zipcode"); //zipcode
                    String feederManu = feeder.getString("FeederManu"); // Manufacturer
                    String feederVol = feeder.getString("FeederVolume"); // Feeder Vol
                    String feederType = feeder.getString("FeederType"); // Feeder Type
                    //String status = feeder.getString("status"); //feeder status
                    String feederSN = feeder.getString("FeederSN");
                    String country = feeder.getString("name");
                    feederArray.add(loc + "," + zipcode);
                    masterDataFeeder.add(loc + "," + feederID + "," + zipcode + "," + feederManu + "," + feederVol + ","
                            + feederType + "," + feederSN + "," + country);
                    Log.v("ThirdFragment", "location: " + loc + " ID: " + feederID + " zipcode: " + zipcode);
                }


            }
            catch(Exception ex) {
                ex.printStackTrace();
                Log.v("ThirdFragment", "FAILED PARSING JSON!!!");
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
                String response = GetText();
                return "Success";
            } catch (Exception e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_expandable_list_item_1, feederArray);

            lv1.setAdapter(arrayAdapter1);

            ListUtils.setDynamicHeight(lv1);

            arrayAdapter1.notifyDataSetChanged();


        }
    }


    // Create a message handling object as an anonymous class.
    private AdapterView.OnItemClickListener feederClickHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id)
        {
            HashMap<String, String> user_prefs = session.getUserDetail();
            Log.v(this.getClass().getSimpleName(), user_prefs.toString());
            /*Intent i = new Intent(getActivity(), EditDeleteFeeder.class);
            i.putExtra("FEEDER_DATA", masterDataFeeder.get(position) + "," + user_prefs.get("uid"));
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            getActivity().finish();*/
            Log.v(this.getClass().getSimpleName(), "Shit got clicked.");
        }
    };

    public static class ListUtils {
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