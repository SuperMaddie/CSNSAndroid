package com.example.mahdiye.csns;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mahdiye.csns.survey.ChoiceQuestion;
import com.example.mahdiye.csns.survey.QuestionSection;
import com.example.mahdiye.csns.survey.QuestionSheet;
import com.example.mahdiye.csns.survey.Survey;
import com.example.mahdiye.csns.survey.TextQuestion;
import com.example.mahdiye.csns.utils.SharedPreferencesUtil;

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
        final String TOKEN = SharedPreferencesUtil.getSharedValues(getString(R.string.user_token_key), getActivity());

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
                Intent intent = new Intent(getActivity(), SurveyDescriptionActivity.class);
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
                Uri buildUri = Uri.parse(BuildConfig.SURVEYS_BASE_URL + "/list").buildUpon()
                        .appendQueryParameter("dept", params[0]).build();

                URL url = new URL(buildUri.toString());

                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestProperty("Authorization", "Bearer " + params[1]);
                connection.setRequestMethod("GET");
                connection.connect();

                int status = connection.getResponseCode();

                if(status == HttpURLConnection.HTTP_OK) {
                    InputStream is = connection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if (is != null) {
                        reader = new BufferedReader(new InputStreamReader(is));

                        String line;
                        while ((line = reader.readLine()) != null) {
                            buffer.append(line + "\n");
                        }
                    }

                    if (buffer.length() > 0) {
                        jsonString = buffer.toString();
                    }

                    try {
                        return getSurveyDataFromJson(jsonString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //Log.e(LOG_TAG, "Error parsing json");
                    }

                }else{
                    SharedPreferencesUtil.setSharedValues(getString(R.string.user_token_key), null, getActivity());
                    startLoginActivity();
                }
            }catch(IOException e) {
                Log.e(LOG_TAG, "Error", e);
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
            return null;
        }

        public String[] getSurveyDataFromJson(String jsonString) throws JSONException {
            final String SURVEYS_LIST = "surveys";
            final String SURVEY_NAME = "name";
            final String SURVEY_TYPE = "type";
            final String QUESTION_SHEET = "questionSheet";
            final String QUESTION_SHEET_DESCRIPTION = "description";
            final String SECTIONS_LIST = "sections";
            final String SECTION_DESCRIPTION = "description";
            final String QUESTIONS_LIST = "questions";
            final String POINT_VALUE = "pointValue";
            final String QUESTION_TYPE = "type";
            final String QUESTION_TYPE_CHOICE = "CHOICE";
            final String QUESTION_TYPE_TEXT = "TEXT";
            final String QUESTION_DESCRIPTION = "description";
            final String CHOICES = "choices";
            final String CORRECT_SELECTIONS = "correctSelections";
            final String MIN_SELECTIONS = "minSelections";
            final String MAX_SELECTIONS = "maxSelections";
            final String TEXT_LENGTH = "textLength";

            Log.e("json", jsonString);

            Survey survey;
            QuestionSheet questionSheet;
            QuestionSection questionSection;

            JSONObject object = new JSONObject(jsonString);
            JSONArray surveysArray = object.getJSONArray(SURVEYS_LIST);

            String results[] = new String[surveysArray.length()];
            for(int i = 0; i<surveysArray.length(); i++){
                JSONObject surveyObject = surveysArray.getJSONObject(i);

                survey = new Survey();
                /*Long id = (Long.valueOf(surveyObject.get("id").toString()));
                survey.setId(id);*/
                survey.setName(surveyObject.getString(SURVEY_NAME));
                survey.setType(surveyObject.getString(SURVEY_TYPE));
                survey.setType(surveyObject.getString(SURVEY_TYPE));

                JSONObject questionSheetObject = surveyObject.getJSONObject(QUESTION_SHEET);
                questionSheet = new QuestionSheet();
                /*id = questionSheetObject.getLong("id");
                questionSheet.setId((id == null) ? null : id);*/
                questionSheet.setDescription(questionSheetObject.getString(QUESTION_SHEET_DESCRIPTION));

                JSONArray sectionsArray = questionSheetObject.getJSONArray(SECTIONS_LIST);

                /* add all sections to survey */
                for(int sec = 0; sec<sectionsArray.length(); sec++) {
                    JSONObject sectionObject = sectionsArray.getJSONObject(sec);
                    questionSection = new QuestionSection();
                    /*id = sectionObject.getLong("id");
                    questionSection.setId((id == null) ? null : id);*/
                    questionSection.setDescription(sectionObject.getString(SECTION_DESCRIPTION));

                    JSONArray questionsArray = sectionObject.getJSONArray(QUESTIONS_LIST);
                    for (int q = 0; q < questionsArray.length(); q++) {
                        JSONObject questionObject = questionsArray.getJSONObject(q);
                        String questionType = questionObject.getString(QUESTION_TYPE);
                        if (questionType.equalsIgnoreCase(QUESTION_TYPE_CHOICE)) {
                            ChoiceQuestion question = new ChoiceQuestion();
                            question.setType(QUESTION_TYPE_CHOICE);
                            /*id = questionObject.getLong("id");
                            question.setId((id == null) ? null : id);*/
                            question.setDescription(questionObject.getString(QUESTION_DESCRIPTION));
                            question.setPointValue(questionObject.getInt(POINT_VALUE));

                            JSONArray choicesArray = questionObject.getJSONArray(CHOICES);
                            for (int ch = 0; ch < choicesArray.length(); ch++) {
                                question.getChoices().add(choicesArray.getString(ch));
                            }
                            JSONArray correctSelectionsArray = questionObject.getJSONArray(CORRECT_SELECTIONS);
                            for (int cs = 0; cs < correctSelectionsArray.length(); cs++) {
                                question.getCorrectSelections().add(correctSelectionsArray.getInt(cs));
                            }
                            question.setMinSelections(questionObject.getInt(MIN_SELECTIONS));
                            question.setMaxSelections(questionObject.getInt(MAX_SELECTIONS));

                            questionSection.getQuestions().add(question);
                        } else if (questionType.equalsIgnoreCase(QUESTION_TYPE_TEXT)) {
                            TextQuestion question = new TextQuestion();
                            question.setType(QUESTION_TYPE_TEXT);
                            /*id = questionObject.getLong("id");
                            question.setId((id == null) ? null : id);*/
                            question.setDescription(questionObject.getString(QUESTION_DESCRIPTION));
                            question.setTextLength(questionObject.getInt(TEXT_LENGTH));

                            questionSection.getQuestions().add(question);
                        }
                    }
                    questionSheet.getSections().add(questionSection);
                }
                survey.setQuestionSheet(questionSheet);
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

    public void startLoginActivity(){
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

}
