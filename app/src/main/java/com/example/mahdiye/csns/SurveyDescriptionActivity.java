package com.example.mahdiye.csns;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mahdiye.csns.survey.Survey;

public class SurveyDescriptionActivity extends AppCompatActivity {

    private static Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_description);
    }

    public static class SurveyDescriptionActivityFragment extends Fragment {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        public SurveyDescriptionActivityFragment() {


        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_survey_description, container, false);
            TextView descriptionTextView = (TextView)rootView.findViewById(R.id.textview_survey_description);
            Button startSurveyButton = (Button)rootView.findViewById(R.id.button_start_survey);

            intent = getActivity().getIntent();

            if(intent != null && intent.hasExtra("survey")) {
                Survey survey = (Survey) intent.getSerializableExtra("survey");
                descriptionTextView.setText(survey.getName());
            }

            startSurveyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Survey survey = (Survey) intent.getSerializableExtra("survey");
                    if(survey != null) {
                        Intent intent = new Intent(getActivity(), SurveyDetailActivity.class);
                        intent.putExtra("survey", survey);
                        startActivity(intent);
                    }
                }
            });

            return rootView;
        }
    }
}
