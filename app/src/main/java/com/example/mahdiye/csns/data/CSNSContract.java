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
    public static final String PATH_QUESTION_SHEET = "question_sheets";
    public static final String PATH_QUESTION_SECTION = "question_sections";
    public static final String PATH_CHOICE_QUESTION = "choice_questions";
    public static final String PATH_TEXT_QUESTION = "text_questions";

    /*public static final class ChoiceQuestionEntry implements BaseColumns {
        public static final String TABLE_NAME = "choice_questions";

        public static final String COLUMN_SECTION_KEY = "question_section_id";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_CHOICES_JSON = "choices";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CHOICE_QUESTION).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CHOICE_QUESTION;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CHOICE_QUESTION;

        public static Uri buildQuestionUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class TextQuestionEntry implements BaseColumns {
        public static final String TABLE_NAME = "text_questions";

        public static final String COLUMN_SECTION_KEY = "question_section_id";
        public static final String COLUMN_DESCRIPTION = "description";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TEXT_QUESTION).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TEXT_QUESTION;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TEXT_QUESTION;

        public static Uri buildQuestionUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class QuestionSectionEntry implements BaseColumns {
        public static final String TABLE_NAME = "question_sections";

        public static final String COLUMN_QUESTION_SHEET_KEY = "question_sheet_id";
        public static final String COLUMN_DESCRIPTION = "description";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_QUESTION_SECTION).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_QUESTION_SECTION;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_QUESTION_SECTION;

        public static Uri buildQuestionSectionUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class QuestionSheetEntry implements BaseColumns {
        public static final String TABLE_NAME = "question_sheets";

        public static final String COLUMN_DESCRIPTION = "description";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_QUESTION_SHEET).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_QUESTION_SHEET;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_QUESTION_SHEET;

        public static Uri buildQuestionSheetUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }*/

    public static final class SurveyEntry implements BaseColumns {
        public static final String TABLE_NAME = "surveys";

        public static final String COLUMN_SURVEY_JSON = "survey_json";
        /*public static final String COLUMN_QUESTION_SHEET_KEY = "question_sheet_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TYPE = "type";*/

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_SURVEY).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SURVEY;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SURVEY;

        public static Uri buildSurveyUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class AnswerEntry implements BaseColumns {
        public static final String TABLE_NAME = "answers";

        public static final String COLUMN_INDEX = "index";
        public static final String COLUMN_SECTION_KEY = "answer_section_id";
    }

    public static final class AnswerSectionEntry implements BaseColumns {
        public static final String TABLE_NAME = "answer_sections";

        public static final String COLUMN_INDEX = "index";
        public static final String COLUMN_ANSWER_SHEET_KEY = "answer_sheet_id";
    }

    public static final class AnswerSheetEntry implements BaseColumns {
        public static final String TABLE_NAME = "answer_sheets";

        public static final String COLUMN_QUESTION_SHEET_KEY = "question_sheet_id";
        public static final String COLUMN_DATE = "date";
    }

    public static final class SurveyResponseEntry implements BaseColumns {
        public static final String TABLE_NAME = "servey_responses";

        public static final String COLUMN_SURVEY_KEY = "survey_id";
        public static final String COLUMN_ANSWER_SHEET_KEY = "answer_sheet_id";
    }
}
