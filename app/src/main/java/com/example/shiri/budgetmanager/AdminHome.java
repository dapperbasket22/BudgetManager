package com.example.shiri.budgetmanager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;

public class AdminHome extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    static SQLiteDatabase db;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //my work
        db = openOrCreateDatabase("andro_db",MODE_PRIVATE,null);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class UserFragment extends Fragment {
        GridView gd;
        ArrayAdapter<String> x1;
        public UserFragment() {
        }

        public static UserFragment newInstance() {
            return new UserFragment();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_admin_home, container, false);

            gd = (GridView) rootView.findViewById(R.id.gridView);
            gd.setNumColumns(2);
            ArrayList<String> al = new ArrayList<>();
            al.add("User Name");
            al.add("Contact");
            Cursor csu = db.rawQuery("select u_name, contact from user_detail",null);
            csu.moveToFirst();
            while(!csu.isAfterLast()){
                al.add(csu.getString(0));
                al.add(csu.getString(1));
                csu.moveToNext();
            }
            csu.close();
            x1 = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,al);
            gd.setAdapter(x1);
            return rootView;
        }
    }


    public static class BudgetFragment extends Fragment{

        GridView gd;
        ArrayAdapter<String> x2;

        public BudgetFragment() {
        }

        public static BudgetFragment newInstance() {
            return new BudgetFragment();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_admin_home, container, false);
            gd = (GridView) rootView.findViewById(R.id.gridView);
            gd.setNumColumns(3);
            ArrayList<String> al = new ArrayList<>();
            al.add("Budget");
            al.add("User");
            al.add("Cost");
            Cursor csu = db.rawQuery("select * from budget",null);
            csu.moveToFirst();
            while(!csu.isAfterLast()){
                al.add(csu.getString(2));
                al.add(csu.getString(1));
                //Total cost logic
                Cursor cosx = db.rawQuery("select * from '"+ csu.getString(2) +"'",null);
                cosx.moveToFirst();
                int cost = 0;
                while(!cosx.isAfterLast()){
                    cost += Integer.parseInt(cosx.getString(1));
                    cosx.moveToNext();
                }
                cosx.close();
                al.add(""+cost);
                csu.moveToNext();
            }
            csu.close();
            x2 = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,al);
            gd.setAdapter(x2);
            return rootView;
        }
    }

    public static class FeedBackFragment extends Fragment {

        GridView gd;
        ArrayAdapter<String> x3;

        public FeedBackFragment() {
        }

        public static FeedBackFragment newInstance() {
            return new FeedBackFragment();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_admin_home, container, false);
            gd = (GridView) rootView.findViewById(R.id.gridView);
            gd.setNumColumns(3);
            ArrayList<String> al = new ArrayList<>();
            al.add("User");
            al.add("Title");
            al.add("Feedback");
            Cursor csu = db.rawQuery("select u_name, title, review from feedback",null);
            csu.moveToFirst();
            while(!csu.isAfterLast()){
                al.add(csu.getString(0));
                al.add(csu.getString(1));
                al.add(csu.getString(2));
                csu.moveToNext();
            }
            csu.close();
            x3 = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, al);
            gd.setAdapter(x3);
            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) return UserFragment.newInstance();
                else if(position == 1) return BudgetFragment.newInstance();
                else return FeedBackFragment.newInstance();
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Users";
                case 1:
                    return "Budgets";
                case 2:
                    return "Feedbacks";
            }
            return null;
        }
    }
}
