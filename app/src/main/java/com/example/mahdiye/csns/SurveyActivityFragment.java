package com.example.mahdiye.csns;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.mahdiye.csns.data.CSNSContract;
import com.example.mahdiye.csns.utils.SharedPreferencesUtil;
import com.example.mahdiye.csns.utils.SurveyUtils;

/**
 * A placeholder fragment containing a simple view.
 */
public class SurveyActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int SURVEY_LOADER = 0;
    private static final String[] SURVEY_COLUMNS = {
            CSNSContract.SurveyEntry.TABLE_NAME + "." + CSNSContract.SurveyEntry._ID,
            CSNSContract.SurveyEntry.COLUMN_SURVEY_JSON
    };
    public static final int COL_SURVEY_ID = 0;
    public static final int COL_SURVEY_JSON = 1;

    static String DEPT = "cs";
    static String TOKEN;

    SurveyAdapter surveyAdapter;

    public SurveyActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
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
        if (id == R.id.action_logout) {
            SharedPreferencesUtil.setSharedValues(getString(R.string.user_token_key), null, getActivity());
            startLoginActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* Call API here to get survey data */
        TOKEN = SharedPreferencesUtil.getSharedValues(getString(R.string.user_token_key), getActivity());
        surveyAdapter = new SurveyAdapter(getActivity(), null, 0);

        updateSurveys(DEPT, TOKEN);

        View rootView = inflater.inflate(R.layout.fragment_survey, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.survey_listview);
        listView.setAdapter(surveyAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if (cursor != null) {
                    Intent intent = new Intent(getActivity(), SurveyDescriptionActivity.class);
                    intent.putExtra("survey", SurveyUtils.getSurveyFromCursor(cursor));
                    startActivity(intent);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(SURVEY_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        TOKEN = SharedPreferencesUtil.getSharedValues(getString(R.string.user_token_key), getActivity());
        updateSurveys(DEPT, TOKEN);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Sort order:  Ascending, by id.
        String sortOrder = CSNSContract.SurveyEntry._ID + " ASC";
        Uri surveyUri = CSNSContract.SurveyEntry.CONTENT_URI;

        return new CursorLoader(getActivity(),
                surveyUri,
                SURVEY_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        surveyAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        surveyAdapter.swapCursor(null);
    }

    public void updateSurveys(String dept, String token){
        FetchSurveyTask surveyTask = new FetchSurveyTask(getActivity());
        surveyTask.execute(dept, token);
    }

    public void startLoginActivity(){
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

}
