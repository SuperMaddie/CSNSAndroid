package com.example.mahdiye.csns;

import android.app.Activity;
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
import android.widget.Toast;

import com.example.mahdiye.csns.data.CSNSContract;
import com.example.mahdiye.csns.models.survey.Survey;
import com.example.mahdiye.csns.utils.SharedPreferencesUtil;
import com.example.mahdiye.csns.utils.SurveyUtils;

import java.util.Calendar;

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

    private String DEPT;
    private String TOKEN;

    public static Activity surveyActivity;

    SurveyAdapter surveyAdapter;

    public SurveyActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        DEPT = getActivity().getString(R.string.department);
        TOKEN = SharedPreferencesUtil.getSharedValues(getString(R.string.user_token_key), getActivity());

        surveyActivity = getActivity();

        /*IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(getString(R.string.logout_broadcast_action));
        getActivity().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("onReceive","Logout in progress");
            }
        }, intentFilter);*/
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_survey, menu);
        MenuItem logout = menu.findItem(R.id.action_logout);
        MenuItem login = menu.findItem(R.id.action_login);
        if(TOKEN == null){
            logout.setVisible(false);
            getActivity().invalidateOptionsMenu();
        }else{
            login.setVisible(false);
            getActivity().invalidateOptionsMenu();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_login) {
            startLoginActivity();
            return true;
        }
        if (id == R.id.action_logout) {
            SharedPreferencesUtil.setSharedValues(getString(R.string.user_token_key), null, getActivity());
            SurveyUtils.finishMainActivity();
            startMainActivity();
            getActivity().finish();
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
                    Survey survey = SurveyUtils.getSurveyFromCursor(cursor);
                    if(survey.getType().equals(Survey.SurveyType.TYPE_RECORDED)){
                        Cursor responseCursor = getResponseCursor(survey.getId());
                        if(responseCursor.moveToFirst()){
                            Toast.makeText(getActivity(), R.string.survey_recorded_type_prevent_fill,
                                    Toast.LENGTH_LONG).show();
                            responseCursor.close();
                            return;
                        }
                    }
                    SharedPreferencesUtil.setSharedValues("survey", SurveyUtils.getSurveyJson(survey), getActivity());

                    Intent intent = new Intent(getActivity(), SurveyDescriptionActivity.class);
                    intent.putExtra("survey", survey);
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
        String sortOrder = CSNSContract.SurveyEntry.COLUMN_PUBLISH_DATE + " DESC";
        Uri surveyUri = CSNSContract.SurveyEntry.CONTENT_URI;

        /* get open surveys only */
        String selection = CSNSContract.SurveyEntry.COLUMN_DELETED + " = ? AND " + CSNSContract.SurveyEntry.COLUMN_CLOSE_DATE + " > ?";
        String[] selectionArgs = new String[]{Long.toString(0), Long.toString(Calendar.getInstance().getTimeInMillis())};

        String anonymousSelection = CSNSContract.SurveyEntry.COLUMN_DELETED + " = ? AND " + CSNSContract.SurveyEntry.COLUMN_CLOSE_DATE +
                " > ? AND " + CSNSContract.SurveyEntry.COLUMN_TYPE + " = ?";
        String[] anonymousSelectionArgs = new String[]{Long.toString(0), Long.toString(Calendar.getInstance().getTimeInMillis()),
                Survey.SurveyType.TYPE_ANONYMOUS};

        if(TOKEN == null){
            return new CursorLoader(
                    getActivity(),
                    surveyUri,
                    SURVEY_COLUMNS,
                    anonymousSelection,
                    anonymousSelectionArgs,
                    sortOrder);
        }

        return new CursorLoader(
                getActivity(),
                surveyUri,
                SURVEY_COLUMNS,
                selection,
                selectionArgs,
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
        intent.putExtra(getString(R.string.source_activity), LoginActivity.SourceActivity.SURVEY);
        startActivity(intent);
    }

    private void startMainActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    Cursor getResponseCursor(Long id){
        Cursor responseCursor = null;
        responseCursor = getActivity().getContentResolver().query(
                CSNSContract.SurveyResponseEntry.CONTENT_URI,
                null,
                CSNSContract.SurveyResponseEntry._ID + " = ?",
                new String[]{id.toString()},
                null);

        return  responseCursor;
    }
}
