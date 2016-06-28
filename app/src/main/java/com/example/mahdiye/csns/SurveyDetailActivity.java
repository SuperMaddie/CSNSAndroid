package com.example.mahdiye.csns;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SurveyDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_detail);
    }

    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }
}
