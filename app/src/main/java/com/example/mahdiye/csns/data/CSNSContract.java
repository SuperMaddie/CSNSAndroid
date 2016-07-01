package com.example.mahdiye.csns.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Mahdiye on 6/6/2016.
 */
public class CSNSContract {

    public static final String CONTENT_AUTHORITY = "com.example.mahdiye.csns";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_SURVEY = "surveys";
    public static final String PATH_SURVEY_RESPONSE = "responses";

    public static final class SurveyEntry implements BaseColumns {
        public static final String TABLE_NAME = "surveys";

        public static final String COLUMN_SURVEY_JSON = "survey_json";
        public static final String COLUMN_PUBLISH_DATE = "publish_date";
        public static final String COLUMN_CLOSE_DATE = "close_date";
        public static final String COLUMN_DELETED = "deleted";
        public static final String COLUMN_TYPE = "type";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_SURVEY).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SURVEY;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SURVEY;

        public static Uri buildSurveyUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class SurveyResponseEntry implements BaseColumns {
        public static final String TABLE_NAME = "servey_responses";

        public static final String COLUMN_SURVEY_RESPONSE_JSON = "survey_response_json";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_SURVEY_RESPONSE).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SURVEY_RESPONSE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SURVEY_RESPONSE;

        public static Uri buildSurveyResponseUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
