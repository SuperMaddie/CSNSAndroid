package com.example.mahdiye.csns;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mahdiye.csns.models.survey.Survey;
import com.example.mahdiye.csns.utils.SharedPreferencesUtil;
import com.example.mahdiye.csns.utils.SurveyUtils;

/**
 * Created by Mahdiye on 7/1/2016.
 */
public class SurveyDescriptionActivityFragment extends Fragment {

    private static Intent intent;

    public static Activity surveyDescriptionActivity;
    private String TOKEN;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        /*if(surveyDescriptionActivity != null){
            finishOldActivity();
        }*/

        String check = SharedPreferencesUtil.getSharedValues("check", getActivity());
        TOKEN = SharedPreferencesUtil.getSharedValues(getString(R.string.user_token_key), getActivity());
        surveyDescriptionActivity = getActivity();
    }

    public void finishOldActivity(){
        surveyDescriptionActivity.finish();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_survey_description, menu);
        MenuItem logout = menu.findItem(R.id.action_logout);
        MenuItem login = menu.findItem(R.id.action_login);
        if(TOKEN == null){
            logout.setVisible(false);
            getActivity().invalidateOptionsMenu();
        }else{
            login.setVisible(false);
            getActivity().invalidateOptionsMenu();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_login) {
            startLoginActivity();
            return true;
        }
        if (id == R.id.action_logout) {
            SharedPreferencesUtil.setSharedValues(getString(R.string.user_token_key), null, getActivity());
            //SurveyUtils.sendBroadcast(getActivity(), getActivity().getString(R.string.logout_broadcast_action));
            SurveyUtils.finishSurveyActivity();
            SurveyUtils.finishMainActivity();

            startMainActivity();
            getActivity().finish();
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
        TextView titleTextView = (TextView)rootView.findViewById(R.id.survey_description_survey_title_textview);
        ImageButton infoImageButton = (ImageButton)rootView.findViewById(R.id.imagebutton_survey_description_type_hint);
        TextView descriptionTextView = (TextView)rootView.findViewById(R.id.textview_survey_description);
        Button startSurveyButton = (Button)rootView.findViewById(R.id.button_start_survey);

        intent = getActivity().getIntent();

        if(intent != null && intent.hasExtra("survey")) {
            final Survey survey = (Survey) intent.getSerializableExtra("survey");
            titleTextView.setText(survey.getName());
            descriptionTextView.setText(survey.getQuestionSheet().getDescription());

            infoImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopup(getHint(survey.getType()));
                }
            });
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
        intent.putExtra(getString(R.string.source_activity), LoginActivity.SourceActivity.SURVEY_DECRIPTION);
        startActivity(intent);
    }

    private void startMainActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    public String getHint(String type){
        switch (type){
            case(Survey.SurveyType.TYPE_ANONYMOUS):
                return surveyDescriptionActivity.getString(R.string.survey_info_anonymous);
            case(Survey.SurveyType.TYPE_RECORDED):
                return surveyDescriptionActivity.getString(R.string.survey_info_recorded);
            case(Survey.SurveyType.TYPE_NAMED):
                return surveyDescriptionActivity.getString(R.string.survey_info_named);
            default:
                return "";
        }
    }

    public void showPopup(final String message){
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder alert  = new AlertDialog.Builder(getActivity());
                alert.setMessage(Html.fromHtml(message));
                alert.setPositiveButton("OK", null);
                alert.setCancelable(true);
                alert.create().show();

                alert.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
            }
        });
    }
}
