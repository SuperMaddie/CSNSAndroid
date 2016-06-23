package com.example.mahdiye.csns;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.mahdiye.csns.models.survey.Survey;
import com.example.mahdiye.csns.utils.SurveyUtils;

/**
 * Created by Mahdiye on 6/21/2016.
 */
public class SurveyAdapter extends CursorAdapter {
    public SurveyAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    private String convertCursorRowToUXFormat(Cursor cursor) {
        Survey survey = SurveyUtils.getSurveyFromCursor(cursor);
        return survey.getName();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_survey, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv = (TextView)view;
        tv.setText(convertCursorRowToUXFormat(cursor));
    }
}
