package com.example.mahdiye.csns;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mahdiye.csns.models.survey.Survey;
import com.example.mahdiye.csns.utils.SharedPreferencesUtil;

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
            setHasOptionsMenu(true);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_main, menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.action_settings) {
                return true;
            }
            if (id == R.id.action_about) {
                return true;
            }
            if (id == R.id.action_logout) {
                SharedPreferencesUtil.setSharedValues(getString(R.string.user_token_key), null, getActivity());
                startLoginActivity();
                return true;
            }
            return super.onOptionsItemSelected(item);
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
                descriptionTextView.setText(survey.getQuestionSheet().getDescription());
            }

            startSurveyButton.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            v.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
                            v.invalidate();
                            break;
                        }
                        case MotionEvent.ACTION_UP: {
                            v.getBackground().clearColorFilter();
                            v.invalidate();
                            break;
                        }
                    }
                    return false;
                }
            });

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

        public void startLoginActivity(){
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
    }

}
