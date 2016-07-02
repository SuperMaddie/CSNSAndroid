package com.example.mahdiye.csns.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mahdiye on 6/7/2016.
 */
public class CSNSDbHelper  extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 25;

    static final String DATABASE_NAME = "csns.db";

    public CSNSDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_SURVEY_TABLE = "CREATE TABLE " + CSNSContract.SurveyEntry.TABLE_NAME +
                " (" + CSNSContract.SurveyEntry._ID + " INTEGER PRIMARY KEY, " +
                CSNSContract.SurveyEntry.COLUMN_PUBLISH_DATE + " INTEGER NOT NULL, " +
                CSNSContract.SurveyEntry.COLUMN_CLOSE_DATE + " INTEGER NOT NULL, " +
                CSNSContract.SurveyEntry.COLUMN_DELETED + " INTEGER NOT NULL, " +
                CSNSContract.SurveyEntry.COLUMN_TYPE + " TEXT NOT NULL, " +
                CSNSContract.SurveyEntry.COLUMN_SURVEY_JSON + " TEXT NOT NULL, " +
                " UNIQUE (" + CSNSContract.SurveyEntry._ID + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_SURVEY_RESPONSE_TABLE = "CREATE TABLE " + CSNSContract.SurveyResponseEntry.TABLE_NAME +
                " (" + CSNSContract.SurveyResponseEntry._ID + " INTEGER PRIMARY KEY, " +
                CSNSContract.SurveyResponseEntry.COLUMN_SURVEY_RESPONSE_JSON + " TEXT NOT NULL, " +

                " UNIQUE (" + CSNSContract.SurveyResponseEntry._ID + ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_SURVEY_TABLE);
        db.execSQL(SQL_CREATE_SURVEY_RESPONSE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CSNSContract.SurveyEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CSNSContract.SurveyResponseEntry.TABLE_NAME);
        onCreate(db);
    }

}
