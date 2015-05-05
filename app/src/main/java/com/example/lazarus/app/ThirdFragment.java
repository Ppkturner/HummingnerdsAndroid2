package com.example.lazarus.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

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
            }
        });

        return view;
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
}