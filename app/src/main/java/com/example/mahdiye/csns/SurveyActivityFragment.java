package com.example.mahdiye.csns;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mahdiye.csns.survey.ChoiceQuestion;
import com.example.mahdiye.csns.survey.Survey;
import com.example.mahdiye.csns.survey.TextQuestion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class SurveyActivityFragment extends Fragment {

    ArrayAdapter<String> surveyArrayAdapter;
    List<Survey> surveys = new ArrayList<>();

    public SurveyActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* Call API here to get survey data */
        final String TOKEN = getSharedValues(getString(R.string.user_token_key), getActivity());

        String DEPT = "cs";
        updateSurveys(DEPT, TOKEN);

        surveyArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_survey,
                R.id.survey_listview_item, new ArrayList<String>());

        View rootView = inflater.inflate(R.layout.fragment_survey, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.survey_listview);
        listView.setAdapter(surveyArrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), SurveyDetailActivity.class);
                intent.putExtra("survey", surveys.get(position));
                startActivity(intent);
            }
        });

        return rootView;
    }

    public void updateSurveys(String dept, String token){
        FetchSurveyTask surveyTask = new FetchSurveyTask();
        surveyTask.execute(dept, token);
    }

    public class FetchSurveyTask extends AsyncTask<String, Void, String[]> {

        private String LOG_TAG = FetchSurveyTask.class.getSimpleName();

        @Override
        protected String[] doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String jsonString = null;

            try{
                Uri buildUri = Uri.parse(BuildConfig.SURVEYS_BASE_URL).buildUpon()
                        .appendQueryParameter("dept", params[0])
                        .appendQueryParameter("token", params[1]).build();

                URL url = new URL(buildUri.toString());

                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                InputStream is = connection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if(is != null){
                    reader = new BufferedReader(new InputStreamReader(is));

                    String line;
                    while((line = reader.readLine()) != null){
                        buffer.append(line + "\n");
                    }
                }

                if(buffer.length() > 0){
                    jsonString = buffer.toString();
                }
            }catch(IOException e) {
                Log.e(LOG_TAG, "Error", e);
                jsonString = null;
            }finally{
                if(connection != null){
                    connection.disconnect();
                }
                if(reader != null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getSurveyDataFromJson(jsonString);
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error parsing json");
            }
            return null;
        }

        public String[] getSurveyDataFromJson(String jsonString) throws JSONException {
            final String SURVEYS_LIST = "surveys";
            final String SURVEY_NAME = "name";
            final String SURVEY_TYPE = "type";
            final String QUESTION_SHEET = "questionSheet";
            final String SECTIONS_LIST = "sections";
            final String QUESTIONS_LIST = "questions";
            final String QUESTION_TYPE = "type";
            final String QUESTION_TYPE_CHOICE = "CHOICE";
            final String QUESTION_TYPE_TEXT = "TEXT";
            final String QUESTION_DESCRIPTION = "description";
            final String CHOICES = "choices";

            Log.e("json", jsonString);

            Survey survey = null;

            JSONObject object = new JSONObject(jsonString);
            JSONArray surveysArray = object.getJSONArray(SURVEYS_LIST);

            String results[] = new String[surveysArray.length()];
            for(int i = 0; i<surveysArray.length(); i++){
                JSONObject surveyObject = surveysArray.getJSONObject(i);
                JSONObject questionSheetObject = surveyObject.getJSONObject(QUESTION_SHEET);
                JSONArray sectionsArray = questionSheetObject.getJSONArray(SECTIONS_LIST);

                /* add other sections later */
                JSONObject section = sectionsArray.getJSONObject(0);
                JSONArray questionsArray = section.getJSONArray(QUESTIONS_LIST);

                survey = new Survey();
                survey.setName(surveyObject.getString(SURVEY_NAME));
                survey.setType(surveyObject.getString(SURVEY_TYPE));
                for(int j = 0; j<questionsArray.length(); j++) {
                    JSONObject questionObject = questionsArray.getJSONObject(j);
                    String questionType = questionObject.getString(QUESTION_TYPE);
                    if(questionType.equalsIgnoreCase(QUESTION_TYPE_CHOICE)){
                        ChoiceQuestion question = new ChoiceQuestion();
                        question.setDescription(questionObject.getString(QUESTION_DESCRIPTION));
                        question.setType(questionObject.getString(QUESTION_TYPE));
                        JSONArray choicesArray = questionObject.getJSONArray(CHOICES);
                        for(int k = 0; k<choicesArray.length(); k++) {
                            question.getChoices().add(choicesArray.getString(k));
                        }
                        survey.getQuestions().add(question);
                    }
                    else if(questionType.equalsIgnoreCase(QUESTION_TYPE_TEXT)){
                        TextQuestion question = new TextQuestion();
                        question.setDescription(questionObject.getString(QUESTION_DESCRIPTION));
                        question.setType(questionObject.getString(QUESTION_TYPE));

                        survey.getQuestions().add(question);
                    }
                }
                surveys.add(survey);
                results[i] = survey.getName();
            }

            return results;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            if(strings != null){
                surveyArrayAdapter.clear();
                for(String s: strings){
                    surveyArrayAdapter.add(s);
                }
            }
        }
    }

    public void setSharedValues(String key, String value, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getSharedValues(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }
}
