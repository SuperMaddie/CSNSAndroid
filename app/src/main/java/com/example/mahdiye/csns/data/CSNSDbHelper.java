package com.example.mahdiye.csns.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mahdiye on 6/7/2016.
 */
public class CSNSDbHelper  extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 6;

    static final String DATABASE_NAME = "csns.db";

    public CSNSDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*final String SQL_CREATE_CHOICE_QUESTION_TABLE = "CREATE TABLE " + CSNSContract.ChoiceQuestionEntry.TABLE_NAME +
                " (" + CSNSContract.ChoiceQuestionEntry._ID + " INTEGER PRIMARY KEY, " +
                CSNSContract.ChoiceQuestionEntry.COLUMN_DESCRIPTION + " TEXT, " +
                CSNSContract.ChoiceQuestionEntry.COLUMN_CHOICES_JSON + " TEXT, " +
                CSNSContract.ChoiceQuestionEntry.COLUMN_SECTION_KEY + " INTEGER NOT NULL, " +
                " FOREIGN KEY (" + CSNSContract.ChoiceQuestionEntry.COLUMN_SECTION_KEY + ") REFERENCES " +
                CSNSContract.QuestionSectionEntry.TABLE_NAME + " (" + CSNSContract.QuestionSectionEntry._ID + "), " +
                " UNIQUE (" + CSNSContract.ChoiceQuestionEntry._ID + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_TEXT_QUESTION_TABLE = "CREATE TABLE " + CSNSContract.TextQuestionEntry.TABLE_NAME +
                " (" + CSNSContract.TextQuestionEntry._ID + " INTEGER PRIMARY KEY, " +
                CSNSContract.TextQuestionEntry.COLUMN_DESCRIPTION + " TEXT, " +
                CSNSContract.TextQuestionEntry.COLUMN_SECTION_KEY + " INTEGER NOT NULL, " +
                " FOREIGN KEY (" + CSNSContract.TextQuestionEntry.COLUMN_SECTION_KEY + ") REFERENCES " +
                CSNSContract.QuestionSectionEntry.TABLE_NAME + " (" + CSNSContract.QuestionSectionEntry._ID + "), " +
                " UNIQUE (" + CSNSContract.TextQuestionEntry._ID + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_QUESTION_SECTION_TABLE = "CREATE TABLE " + CSNSContract.QuestionSectionEntry.TABLE_NAME +
                " (" + CSNSContract.QuestionSectionEntry._ID + " INTEGER PRIMARY KEY, " +
                CSNSContract.QuestionSectionEntry.COLUMN_DESCRIPTION + " TEXT, " +
                CSNSContract.QuestionSectionEntry.COLUMN_QUESTION_SHEET_KEY + " INTEGER NOT NULL, " +
                " FOREIGN KEY (" + CSNSContract.QuestionSectionEntry.COLUMN_QUESTION_SHEET_KEY + ") REFERENCES " +
                CSNSContract.QuestionSheetEntry.TABLE_NAME + " (" + CSNSContract.QuestionSheetEntry._ID + "), " +
                " UNIQUE (" + CSNSContract.QuestionSectionEntry._ID + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_QUESTION_SHEET_TABLE = "CREATE TABLE " + CSNSContract.QuestionSheetEntry.TABLE_NAME +
                " (" + CSNSContract.QuestionSheetEntry._ID + " INTEGER PRIMARY KEY, " +
                CSNSContract.QuestionSheetEntry.COLUMN_DESCRIPTION + " TEXT, " +
                " UNIQUE (" + CSNSContract.QuestionSheetEntry._ID + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_SURVEY_TABLE = "CREATE TABLE " + CSNSContract.SurveyEntry.TABLE_NAME +
                " (" + CSNSContract.SurveyEntry._ID + " INTEGER PRIMARY KEY, " +
                CSNSContract.SurveyEntry.COLUMN_NAME + " TEXT, " +
                CSNSContract.SurveyEntry.COLUMN_TYPE + " TEXT, " +
                CSNSContract.SurveyEntry.COLUMN_QUESTION_SHEET_KEY + " INTEGER NOT NULL, " +
                " FOREIGN KEY (" + CSNSContract.SurveyEntry.COLUMN_QUESTION_SHEET_KEY + ") REFERENCES " +
                CSNSContract.QuestionSheetEntry.TABLE_NAME + " (" + CSNSContract.QuestionSheetEntry._ID + "), " +
                " UNIQUE (" + CSNSContract.SurveyEntry._ID + ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_CHOICE_QUESTION_TABLE);
        db.execSQL(SQL_CREATE_TEXT_QUESTION_TABLE);
        db.execSQL(SQL_CREATE_QUESTION_SECTION_TABLE);
        db.execSQL(SQL_CREATE_QUESTION_SHEET_TABLE);*/

        final String SQL_CREATE_SURVEY_TABLE = "CREATE TABLE " + CSNSContract.SurveyEntry.TABLE_NAME +
                " (" + CSNSContract.SurveyEntry._ID + " INTEGER PRIMARY KEY, " +
                CSNSContract.SurveyEntry.COLUMN_SURVEY_JSON + " TEXT, " +
                " UNIQUE (" + CSNSContract.SurveyEntry._ID + ") ON CONFLICT REPLACE);";
        db.execSQL(SQL_CREATE_SURVEY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CSNSContract.SurveyEntry.TABLE_NAME);
        /*db.execSQL("DROP TABLE IF EXISTS " + CSNSContract.QuestionSheetEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CSNSContract.QuestionSectionEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CSNSContract.TextQuestionEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CSNSContract.ChoiceQuestionEntry.TABLE_NAME);*/
        onCreate(db);
    }

}
