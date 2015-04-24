package com.example.lazarus.app;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class UserMenu extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        FragmentManager fm = getSupportFragmentManager();

        viewPager.setAdapter(new SampleFragmentPagerAdapter(fm));
    }

    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 5;

        public SampleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position){
                case 0:
                    return FirstFragment.create(position);
                case 1:
                    return SecondFragment.create(position);
                case 2:
                    return ThirdFragment.create(position);
                default:
                    return PageFragment.create(position);
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position){
                case 0:
                    return("User Settings");
                case 1:
                    return("Birds");
                case 2:
                    return("Bird Feeders");
                default:
                    return("Page" + position);
            }
        }
    }

    // A new class for each different kind of page tab

    public static class FirstFragment extends Fragment{
        public static final String ARG_PAGE = "ARG_PAGE";

        private int mPage;

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
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.first_fragment, container, false);
            TextView textView = (TextView) view.findViewById(R.id.listTextView);
            textView.setText("Fragment #" + mPage);
            return view;
        }
    }

    public static class SecondFragment extends Fragment{
        public static final String ARG_PAGE = "ARG_PAGE";

        private int mPage;

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
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.second_fragment, container, false);
            TextView textView = (TextView) view.findViewById(R.id.listTextView);
            textView.setText("Fragment #" + mPage);
            return view;
        }
    }

    public static class ThirdFragment extends Fragment{
        public static final String ARG_PAGE = "ARG_PAGE";

        private int mPage;

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
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.third_fragment, container, false);
            TextView textView = (TextView) view.findViewById(R.id.listTextView);
            textView.setText("Fragment #" + mPage);
            return view;
        }
    }

    // What each page in the tab bar looks like.
    public static class PageFragment extends Fragment {
        public static final String ARG_PAGE = "ARG_PAGE";

        private int mPage;

        public static PageFragment create(int page) {
            Bundle args = new Bundle();
            args.putInt(ARG_PAGE, page);
            PageFragment fragment = new PageFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mPage = getArguments().getInt(ARG_PAGE);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_page, container, false);
            TextView textView = (TextView) view.findViewById(R.id.listTextView);
            textView.setText("Fragment #" + mPage);
            return view;
        }
    }
}
