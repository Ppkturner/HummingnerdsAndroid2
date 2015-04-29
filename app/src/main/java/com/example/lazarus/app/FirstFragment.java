package com.example.lazarus.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FirstFragment extends Fragment implements View.OnClickListener {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    private SessionManager session;

    Button button;

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
}