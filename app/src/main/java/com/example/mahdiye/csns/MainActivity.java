package com.example.mahdiye.csns;

import android.app.Activity;
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

import com.example.mahdiye.csns.utils.SharedPreferencesUtil;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private String TOKEN;
    public static Activity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TOKEN = SharedPreferencesUtil.getSharedValues(getString(R.string.user_token_key), this);
        mainActivity = this;

        /*IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(getString(R.string.logout_broadcast_action));
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("onReceive","Logout in progress");
            }
        }, intentFilter);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem logout = menu.findItem(R.id.action_logout);
        MenuItem login = menu.findItem(R.id.action_login);
        if(TOKEN == null){
            logout.setVisible(false);
            this.invalidateOptionsMenu();
        }else{
            login.setVisible(false);
            this.invalidateOptionsMenu();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_login) {
            startLoginActivity();
            return true;
        }
        if (id == R.id.action_logout) {
            SharedPreferencesUtil.setSharedValues(getString(R.string.user_token_key), null, this);

            recreateActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void startLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(getString(R.string.source_activity), LoginActivity.SourceActivity.MAIN);
        startActivity(intent);
    }

    public void recreateActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(getString(R.string.source_activity), LoginActivity.SourceActivity.MAIN);
        startActivity(intent);
        finish();
    }

    /* Main Activity Fragment Class */
    public static class MainActivityFragment extends Fragment {

        ArrayAdapter<String> mainArrayAdapter;

        public MainActivityFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            /* Create mock data for main view */
            String[] mainList = {"Surveys", "News", "Mailing Lists", "File Manager"};
            mainArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_item_main, R.id.main_listview_item, Arrays.asList(mainList));

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
