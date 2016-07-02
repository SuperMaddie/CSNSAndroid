package com.example.mahdiye.csns;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.mahdiye.csns.data.CSNSContract;
import com.example.mahdiye.csns.models.survey.ChoiceQuestion;
import com.example.mahdiye.csns.models.survey.QuestionSection;
import com.example.mahdiye.csns.models.survey.QuestionSheet;
import com.example.mahdiye.csns.models.survey.Survey;
import com.example.mahdiye.csns.models.survey.TextQuestion;
import com.example.mahdiye.csns.utils.SurveyUtils;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Mahdiye on 6/20/2016.
 */
public class FetchSurveyTask extends AsyncTask<String, Void, Void> {

    private String LOG_TAG = FetchSurveyTask.class.getSimpleName();

    private final Context mContext;

    public FetchSurveyTask(Context context) {
        mContext = context;
    }


    Long saveSurvey(Survey survey) {
        Long surveyId;

        ContentValues surveyValues = new ContentValues();
        surveyValues.put(CSNSContract.SurveyEntry._ID, survey.getId());
        surveyValues.put(CSNSContract.SurveyEntry.COLUMN_SURVEY_JSON, SurveyUtils.getSurveyBytes(survey));
        surveyValues.put(CSNSContract.SurveyEntry.COLUMN_TYPE, survey.getType());
        surveyValues.put(CSNSContract.SurveyEntry.COLUMN_DELETED, survey.isDeleted());
        surveyValues.put(CSNSContract.SurveyEntry.COLUMN_PUBLISH_DATE, survey.getPublishDate().getTimeInMillis());
        surveyValues.put(CSNSContract.SurveyEntry.COLUMN_CLOSE_DATE, survey.getCloseDate().getTimeInMillis());

        /* First, check if the survey with this id exists in the db */
        Cursor surveyCursor = mContext.getContentResolver().query(
                CSNSContract.SurveyEntry.CONTENT_URI,
                new String[]{CSNSContract.SurveyEntry._ID},
                CSNSContract.SurveyEntry._ID + " = ?",
                new String[]{survey.getId().toString()},
                null);

        if (surveyCursor.moveToFirst()) {
            /* if the id exists update it */
            int rowsUpdated = mContext.getContentResolver().update(
                    CSNSContract.SurveyEntry.CONTENT_URI,
                    surveyValues,
                    CSNSContract.SurveyEntry._ID + " = ?",
                    new String[]{survey.getId().toString()}
            );
            //int questionIdIndex = questionCursor.getColumnIndex(CSNSContract.QuestionSheetEntry._ID);
            surveyId = Long.valueOf(survey.getId());
        } else {
            /* insert the survey into db */
            Uri insertedUri = mContext.getContentResolver().insert(
                    CSNSContract.SurveyEntry.CONTENT_URI,
                    surveyValues
            );
            // The resulting URI contains the ID for the row.  Extract the questionId from the Uri.
            surveyId = ContentUris.parseId(insertedUri);
        }
        surveyCursor.close();
        return surveyId;
    }


    @Override
    protected Void doInBackground(String... params) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String jsonString = null;

        try{
            Uri buildUri = Uri.parse(BuildConfig.SURVEYS_BASE_URL + "/list").buildUpon()
                    .appendQueryParameter("dept", params[0]).build();

            URL url = new URL(buildUri.toString());

            connection = (HttpURLConnection)url.openConnection();
            // connection.setRequestProperty("Authorization", "Bearer " + params[1]);
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
                    getSurveyDataFromJson(jsonString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else{
                Log.e(LOG_TAG, "HTTP returned with status " + status);
            }
        }catch(IOException e) {
            Log.e(LOG_TAG, "IO Exception Getting Surveys", e);
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

    public Survey[] getSurveyDataFromJson(String jsonString) throws JSONException {
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
        final String PUBLISH_DATE = "publishDate";
        final String CLOSE_DATE = "closeDate";
        final String DELETED = "deleted";

        final String SECTION_INDICES = "sectionIndices";

        Log.e("json", jsonString);

        Survey survey;
        QuestionSheet questionSheet;
        QuestionSection questionSection;

        JSONObject object = new JSONObject(jsonString);
        JSONObject sectionIndicesJsonObject = object.getJSONObject(SECTION_INDICES);
        JSONArray surveysArray = object.getJSONArray(SURVEYS_LIST);

        /* get section indices */
        Map<Long, Integer> sectionIndices = new HashMap<>();

        Iterator iterator = sectionIndicesJsonObject.keys();
        while(iterator.hasNext()){
            String key = (String)iterator.next();
            int value = sectionIndicesJsonObject.getInt(key);
            sectionIndices.put(Long.valueOf(key),value);
        }

        Survey results[] = new Survey[surveysArray.length()];
        for(int i = 0; i<surveysArray.length(); i++){
            JSONObject surveyObject = surveysArray.getJSONObject(i);

            survey = new Survey();
            if(!surveyObject.isNull("id")) {
                survey.setId(surveyObject.getLong("id"));
            }
            survey.setName(surveyObject.getString(SURVEY_NAME));
            survey.setType(surveyObject.getString(SURVEY_TYPE));
            survey.setDeleted(surveyObject.getBoolean(DELETED));
            survey.setPublishDate(SurveyUtils.getCalendarFromMillis(surveyObject.getLong(PUBLISH_DATE)));
            survey.setCloseDate(SurveyUtils.getCalendarFromMillis(surveyObject.getLong(CLOSE_DATE)));

            JSONObject questionSheetObject = surveyObject.getJSONObject(QUESTION_SHEET);
            questionSheet = new QuestionSheet();
            if(!questionSheetObject.isNull("id")) {
                questionSheet.setId(questionSheetObject.getLong("id"));
            }
            questionSheet.setDescription(questionSheetObject.getString(QUESTION_SHEET_DESCRIPTION));

            JSONArray sectionsArray = questionSheetObject.getJSONArray(SECTIONS_LIST);
            /*for(int sec = 0; sec<sectionsArray.length(); sec++) {
                questionSheet.getSections().add(new QuestionSection());
            }*/

            List<QuestionSection> sections = new ArrayList<>();
            for(int sec = 0; sec<sectionsArray.length(); sec++) {
                sections.add(new QuestionSection());
            }

            /* add all sections to survey */
            for(int sec = 0; sec<sectionsArray.length(); sec++) {
                JSONObject sectionObject = sectionsArray.getJSONObject(sec);
                Long sectionId = null;
                if(!sectionObject.isNull("id")) {
                    sectionId = sectionObject.getLong("id");
                }

                int index = sectionIndices.get(sectionId);

                questionSection = sections.get(index);
                questionSection.setId(sectionId);

                questionSection.setDescription(sectionObject.getString(SECTION_DESCRIPTION));

                JSONArray questionsArray = sectionObject.getJSONArray(QUESTIONS_LIST);
                for (int q = 0; q < questionsArray.length(); q++) {
                    JSONObject questionObject = questionsArray.getJSONObject(q);
                    String questionType = questionObject.getString(QUESTION_TYPE);
                    if (questionType.equalsIgnoreCase(QUESTION_TYPE_CHOICE)) {
                        ChoiceQuestion question = new ChoiceQuestion();
                        if(!questionObject.isNull("id")) {
                            question.setId(questionObject.getLong("id"));
                        }
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
                        if(!questionObject.isNull("id")){
                            question.setId(questionObject.getLong("id"));
                        }
                        question.setDescription(questionObject.getString(QUESTION_DESCRIPTION));
                        question.setTextLength(questionObject.getInt(TEXT_LENGTH));

                        questionSection.getQuestions().add(question);
                    }
                }
            }
            questionSheet.setSections(sections);
            survey.setQuestionSheet(questionSheet);
            results[i] = survey;
            /* save survey */
            Long surveyId = saveSurvey(survey);
        }

        return null;
    }

}

