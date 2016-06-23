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
import com.example.mahdiye.csns.utils.SharedPreferencesUtil;
import com.example.mahdiye.csns.utils.SurveyUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Mahdiye on 6/20/2016.
 */
public class FetchSurveyTask extends AsyncTask<String, Void, Void> {

    private String LOG_TAG = FetchSurveyTask.class.getSimpleName();

    //private ArrayAdapter<Survey> surveyArrayAdapter;
    private final Context mContext;

    public FetchSurveyTask(Context context) {
        mContext = context;
    }

    /*Long addChoiceQuestion(Long id, String description, String choices, Long sectionId) {
        Long questionId;

        ContentValues questionValues = new ContentValues();
        questionValues.put(CSNSContract.ChoiceQuestionEntry._ID, id);
        questionValues.put(CSNSContract.ChoiceQuestionEntry.COLUMN_DESCRIPTION, description);
        questionValues.put(CSNSContract.ChoiceQuestionEntry.COLUMN_CHOICES_JSON, choices);
        questionValues.put(CSNSContract.ChoiceQuestionEntry.COLUMN_SECTION_KEY, sectionId);

        *//* First, check if the question with this id exists in the db *//*
        Cursor questionCursor = mContext.getContentResolver().query(
                CSNSContract.ChoiceQuestionEntry.CONTENT_URI,
                new String[]{CSNSContract.ChoiceQuestionEntry._ID},
                CSNSContract.ChoiceQuestionEntry._ID + " = ?",
                new String[]{id.toString()},
                null);

        if (questionCursor.moveToFirst()) {
            *//* if the id exists update it *//*
            int rowsUpdated = mContext.getContentResolver().update(
                    CSNSContract.ChoiceQuestionEntry.CONTENT_URI,
                    questionValues,
                    CSNSContract.ChoiceQuestionEntry._ID + " = ?",
                    new String[]{id.toString()}
            );
            //int questionIdIndex = questionCursor.getColumnIndex(CSNSContract.ChoiceQuestionEntry._ID);
            questionId = id;
        } else {
            *//* insert the question into db *//*
            Uri insertedUri = mContext.getContentResolver().insert(
                    CSNSContract.ChoiceQuestionEntry.CONTENT_URI,
                    questionValues
            );

            // The resulting URI contains the ID for the row.  Extract the questionId from the Uri.
            questionId = ContentUris.parseId(insertedUri);
        }

        questionCursor.close();
        return questionId;
    }

    Long addTextQuestion(Long id, String description, Long sectionId) {
        Long questionId;

        ContentValues questionValues = new ContentValues();
        questionValues.put(CSNSContract.TextQuestionEntry._ID, id);
        questionValues.put(CSNSContract.TextQuestionEntry.COLUMN_DESCRIPTION, description);
        questionValues.put(CSNSContract.TextQuestionEntry.COLUMN_SECTION_KEY, sectionId);

        *//* First, check if the question with this id exists in the db *//*
        Cursor questionCursor = mContext.getContentResolver().query(
                CSNSContract.TextQuestionEntry.CONTENT_URI,
                new String[]{CSNSContract.TextQuestionEntry._ID},
                CSNSContract.TextQuestionEntry._ID + " = ?",
                new String[]{id.toString()},
                null);

        if (questionCursor.moveToFirst()) {
            *//* if the id exists update it *//*
            int rowsUpdated = mContext.getContentResolver().update(
                    CSNSContract.TextQuestionEntry.CONTENT_URI,
                    questionValues,
                    CSNSContract.TextQuestionEntry._ID + " = ?",
                    new String[]{id.toString()}
            );
            //int questionIdIndex = questionCursor.getColumnIndex(CSNSContract.TextQuestionEntry._ID);
            questionId = Long.valueOf(id);
        } else {
            *//* insert the question into db *//*
            Uri insertedUri = mContext.getContentResolver().insert(
                    CSNSContract.TextQuestionEntry.CONTENT_URI,
                    questionValues
            );

            // The resulting URI contains the ID for the row.  Extract the questionId from the Uri.
            questionId = ContentUris.parseId(insertedUri);
        }

        questionCursor.close();
        return questionId;
    }

    Long addQuestionSection(Long id, String description, Long questionSheetId) {
        Long sectionId;

        ContentValues sectionValues = new ContentValues();
        sectionValues.put(CSNSContract.QuestionSectionEntry._ID, id);
        sectionValues.put(CSNSContract.QuestionSectionEntry.COLUMN_DESCRIPTION, description);
        sectionValues.put(CSNSContract.QuestionSectionEntry.COLUMN_QUESTION_SHEET_KEY, questionSheetId);

        *//* First, check if the question with this id exists in the db *//*
        Cursor sectionCursor = mContext.getContentResolver().query(
                CSNSContract.QuestionSectionEntry.CONTENT_URI,
                new String[]{CSNSContract.QuestionSectionEntry._ID},
                CSNSContract.QuestionSectionEntry._ID + " = ?",
                new String[]{id.toString()},
                null);

        if (sectionCursor.moveToFirst()) {
            *//* if the id exists update it *//*
            int rowsUpdated = mContext.getContentResolver().update(
                    CSNSContract.QuestionSectionEntry.CONTENT_URI,
                    sectionValues,
                    CSNSContract.QuestionSectionEntry._ID + " = ?",
                    new String[]{id.toString()}
            );
            //int questionIdIndex = questionCursor.getColumnIndex(CSNSContract.QuestionSectionEntry._ID);
            sectionId = Long.valueOf(id);
        } else {
            *//* insert the question into db *//*
            Uri insertedUri = mContext.getContentResolver().insert(
                    CSNSContract.QuestionSectionEntry.CONTENT_URI,
                    sectionValues
            );

            // The resulting URI contains the ID for the row.  Extract the questionId from the Uri.
            sectionId = ContentUris.parseId(insertedUri);
        }

        sectionCursor.close();
        return sectionId;
    }

    Long addQuestionSheet(Long id, String description) {
        Long sectionId;

        ContentValues questionSheetValues = new ContentValues();
        questionSheetValues.put(CSNSContract.QuestionSheetEntry._ID, id);
        questionSheetValues.put(CSNSContract.QuestionSheetEntry.COLUMN_DESCRIPTION, description);

        *//* First, check if the question with this id exists in the db *//*
        Cursor questioSheetCursor = mContext.getContentResolver().query(
                CSNSContract.QuestionSheetEntry.CONTENT_URI,
                new String[]{CSNSContract.QuestionSheetEntry._ID},
                CSNSContract.QuestionSheetEntry._ID + " = ?",
                new String[]{id.toString()},
                null);

        if (questioSheetCursor.moveToFirst()) {
            *//* if the id exists update it *//*
            int rowsUpdated = mContext.getContentResolver().update(
                    CSNSContract.QuestionSheetEntry.CONTENT_URI,
                    questionSheetValues,
                    CSNSContract.QuestionSheetEntry._ID + " = ?",
                    new String[]{id.toString()}
            );
            //int questionIdIndex = questionCursor.getColumnIndex(CSNSContract.QuestionSheetEntry._ID);
            sectionId = Long.valueOf(id);
        } else {
            *//* insert the question into db *//*
            Uri insertedUri = mContext.getContentResolver().insert(
                    CSNSContract.QuestionSheetEntry.CONTENT_URI,
                    questionSheetValues
            );

            // The resulting URI contains the ID for the row.  Extract the questionId from the Uri.
            sectionId = ContentUris.parseId(insertedUri);
        }

        questioSheetCursor.close();
        return sectionId;
    }*/

    Long addSurvey(Survey survey) {
        Long surveyId;

        Gson gson  = new Gson();

        ContentValues surveyValues = new ContentValues();
        surveyValues.put(CSNSContract.SurveyEntry._ID, survey.getId());
        surveyValues.put(CSNSContract.SurveyEntry.COLUMN_SURVEY_JSON, SurveyUtils.getSurveyBytes(survey));

        /* First, check if the question with this id exists in the db */
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
            /* insert the question into db */
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
                    getSurveyDataFromJson(jsonString);
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Log.e(LOG_TAG, "Error parsing json");
                }

            }else{
                SharedPreferencesUtil.setSharedValues(mContext.getString(R.string.user_token_key), null, mContext);
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

        //Log.e("json", jsonString);

        Gson gson = new Gson();
        Survey survey;
        QuestionSheet questionSheet;
        QuestionSection questionSection;

        JSONObject object = new JSONObject(jsonString);
        JSONArray surveysArray = object.getJSONArray(SURVEYS_LIST);

        Survey results[] = new Survey[surveysArray.length()];
        for(int i = 0; i<surveysArray.length(); i++){
            JSONObject surveyObject = surveysArray.getJSONObject(i);

            survey = new Survey();
            if(!surveyObject.isNull("id")) {
                survey.setId(surveyObject.getLong("id"));
            }
            survey.setName(surveyObject.getString(SURVEY_NAME));
            survey.setType(surveyObject.getString(SURVEY_TYPE));

            JSONObject questionSheetObject = surveyObject.getJSONObject(QUESTION_SHEET);
            questionSheet = new QuestionSheet();
            if(!questionSheetObject.isNull("id")) {
                questionSheet.setId(questionSheetObject.getLong("id"));
            }
            questionSheet.setDescription(questionSheetObject.getString(QUESTION_SHEET_DESCRIPTION));

            /* save question sheet in db */
            //Long questionSheetId = addQuestionSheet(questionSheet.getId(), questionSheet.getDescription());
            /* save survey in db */
            //Long surveyId = addSurvey(survey.getId(), survey.getName(), survey.getType(), questionSheetId);

            JSONArray sectionsArray = questionSheetObject.getJSONArray(SECTIONS_LIST);

            /* add all sections to survey */
            for(int sec = 0; sec<sectionsArray.length(); sec++) {
                JSONObject sectionObject = sectionsArray.getJSONObject(sec);
                questionSection = new QuestionSection();
                if(!sectionObject.isNull("id")) {
                    questionSection.setId(sectionObject.getLong("id"));
                }
                questionSection.setDescription(sectionObject.getString(SECTION_DESCRIPTION));
                /* save section to db */
                /*Long sectionId = addQuestionSection(questionSection.getId(),
                        questionSection.getDescription(), questionSheetId);*/

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

                        /* save question in db */
                        /*Long questionId = addChoiceQuestion(question.getId(), question.getDescription(),
                                gson.toJson(question.getChoices()), sectionId);*/
                    } else if (questionType.equalsIgnoreCase(QUESTION_TYPE_TEXT)) {
                        TextQuestion question = new TextQuestion();
                        if(!questionObject.isNull("id")){
                            question.setId(questionObject.getLong("id"));
                        }
                        question.setDescription(questionObject.getString(QUESTION_DESCRIPTION));
                        question.setTextLength(questionObject.getInt(TEXT_LENGTH));

                        questionSection.getQuestions().add(question);

                        /* save question in db */
                        //Long questionId = addTextQuestion(question.getId(), question.getDescription(), sectionId);
                    }
                }
                questionSheet.getSections().add(questionSection);
            }
            survey.setQuestionSheet(questionSheet);
            results[i] = survey;
            /* save survey */
            Long surveyId = addSurvey(survey);
        }

        return null;
    }

}

