package com.example.mahdiye.csns;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_about) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* Main Activity Fragment Class */
    public static class MainActivityFragment extends Fragment {

        ArrayAdapter<String> mainArrayAdapter;

        public MainActivityFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            /* Create mock data for main view */
            String[] mainList = {"Surveys", "News", "Mailing Lists", "File Manager"};
            mainArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_main, R.id.main_listview_item, Arrays.asList(mainList));

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            ListView listView = (ListView) rootView.findViewById(R.id.main_listview);
            listView.setAdapter(mainArrayAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String pageToOpen = mainArrayAdapter.getItem(position);
                    switch (position){
                        case 0:
                            Intent intent = new Intent(getActivity(), SurveyActivity.class);
                            startActivity(intent);
                            break;
                        case 1:
                            Toast.makeText(getActivity(), pageToOpen, Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            Toast.makeText(getActivity(), pageToOpen, Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });

            return rootView;
        }
    }
}
