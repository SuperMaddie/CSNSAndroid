package com.example.mahdiye.csns.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by Mahdiye on 6/17/2016.
 */
public class CSNSProvider extends ContentProvider {

    private CSNSDbHelper mOpenHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    static final int SURVEY = 100;
    /*static final int QUESTION_SHEET = 200;
    static final int QUESTION_SECTION = 300;
    static final int CHOICE_QUESTION = 400;
    static final int TEXT_QUESTION = 500;*/

    static {

    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CSNSContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, CSNSContract.PATH_SURVEY, SURVEY);
        /*matcher.addURI(authority, CSNSContract.PATH_QUESTION_SHEET, QUESTION_SHEET);
        matcher.addURI(authority, CSNSContract.PATH_QUESTION_SECTION, QUESTION_SECTION);
        matcher.addURI(authority, CSNSContract.PATH_CHOICE_QUESTION, CHOICE_QUESTION);
        matcher.addURI(authority, CSNSContract.PATH_TEXT_QUESTION, TEXT_QUESTION);*/

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new CSNSDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)){
            case SURVEY: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        CSNSContract.SurveyEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            /*case QUESTION_SHEET: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        CSNSContract.QuestionSheetEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case QUESTION_SECTION: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        CSNSContract.QuestionSectionEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case CHOICE_QUESTION: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        CSNSContract.ChoiceQuestionEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case TEXT_QUESTION: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        CSNSContract.TextQuestionEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }*/
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SURVEY:
                return CSNSContract.SurveyEntry.CONTENT_TYPE;
            /*case QUESTION_SHEET:
                return CSNSContract.QuestionSheetEntry.CONTENT_TYPE;
            case QUESTION_SECTION:
                return CSNSContract.QuestionSectionEntry.CONTENT_TYPE;
            case CHOICE_QUESTION:
                return CSNSContract.ChoiceQuestionEntry.CONTENT_TYPE;
            case TEXT_QUESTION:
                return CSNSContract.TextQuestionEntry.CONTENT_TYPE;*/
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case SURVEY: {
                long _id = db.insert(CSNSContract.SurveyEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = CSNSContract.SurveyEntry.buildSurveyUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            /*case QUESTION_SHEET: {
                long _id = db.insert(CSNSContract.QuestionSheetEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = CSNSContract.QuestionSheetEntry.buildQuestionSheetUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case QUESTION_SECTION: {
                long _id = db.insert(CSNSContract.QuestionSectionEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = CSNSContract.QuestionSectionEntry.buildQuestionSectionUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case CHOICE_QUESTION: {
                long _id = db.insert(CSNSContract.ChoiceQuestionEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = CSNSContract.ChoiceQuestionEntry.buildQuestionUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case TEXT_QUESTION: {
                long _id = db.insert(CSNSContract.TextQuestionEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = CSNSContract.TextQuestionEntry.buildQuestionUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }*/
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        if ( null == selection ) selection = "1";
        switch (match) {
            case SURVEY: {
                rowsDeleted = db.delete(CSNSContract.SurveyEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            /*case QUESTION_SHEET: {
                rowsDeleted = db.delete(CSNSContract.QuestionSheetEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case QUESTION_SECTION: {
                rowsDeleted = db.delete(CSNSContract.QuestionSectionEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case CHOICE_QUESTION: {
                rowsDeleted = db.delete(CSNSContract.ChoiceQuestionEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case TEXT_QUESTION: {
                rowsDeleted = db.delete(CSNSContract.TextQuestionEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }*/
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case SURVEY: {
                rowsUpdated = db.update(CSNSContract.SurveyEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            /*case QUESTION_SHEET: {
                rowsUpdated = db.update(CSNSContract.QuestionSheetEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case QUESTION_SECTION: {
                rowsUpdated = db.update(CSNSContract.QuestionSectionEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case CHOICE_QUESTION: {
                rowsUpdated = db.update(CSNSContract.ChoiceQuestionEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case TEXT_QUESTION: {
                rowsUpdated = db.update(CSNSContract.TextQuestionEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }*/
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SURVEY: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(CSNSContract.SurveyEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            /*case CHOICE_QUESTION: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(CSNSContract.ChoiceQuestionEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case TEXT_QUESTION: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(CSNSContract.TextQuestionEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }*/
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
