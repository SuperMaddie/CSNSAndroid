package com.example.mahdiye.csns;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mahdiye.csns.survey.Answer;
import com.example.mahdiye.csns.survey.AnswerSection;
import com.example.mahdiye.csns.survey.AnswerSheet;
import com.example.mahdiye.csns.survey.ChoiceAnswer;
import com.example.mahdiye.csns.survey.ChoiceQuestion;
import com.example.mahdiye.csns.survey.Question;
import com.example.mahdiye.csns.survey.QuestionSection;
import com.example.mahdiye.csns.survey.Survey;
import com.example.mahdiye.csns.survey.SurveyResponse;
import com.example.mahdiye.csns.survey.TextAnswer;
import com.example.mahdiye.csns.survey.TextQuestion;
import com.example.mahdiye.csns.utils.SharedPreferencesUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * A placeholder fragment containing a simple view.
 */
public class SurveyDetailActivityFragment extends Fragment {

    final String LOG_TAG = SurveyDetailActivityFragment.class.getSimpleName();
    private static Survey survey;
    private static View rootView;
    private static ViewPager pager;
    private List<ImageView> dots;
    private static int sectionsCount = 0;

    public SurveyDetailActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_survey_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_submit_survey) {
            final String TOKEN = SharedPreferencesUtil.getSharedValues(getString(R.string.user_token_key), getActivity());
            String DEPT = "cs";
            submitSurvey(DEPT, TOKEN);
            Intent intent = new Intent(getActivity(), SurveyActivity.class);
            startActivity(intent);
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_survey_detail, container, false);

        /* set sections count for pager*/
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra("survey")) {
            survey = (Survey) intent.getSerializableExtra("survey");
            sectionsCount = survey.getQuestionSheet().getSections().size();
        }

        pager = (ViewPager)rootView.findViewById(R.id.viewpager_questions);
        pager.setAdapter(new MyPagerAdapter(getActivity().getSupportFragmentManager()));
        addDots();
        selectDot(0);

        return rootView;
    }


    /*----------------View Pager Custom Adapter----------------*/
    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int pos) {
            SubFragment subFragment = new SubFragment();
            subFragment.setPosition(pos);
            subFragment.setQuestionAdapter(new MyCustomAdapter());
            return subFragment;
        }

        @Override
        public int getCount() {
            return sectionsCount;
        }
    }

    public static class SubFragment extends Fragment {
        private int position;
        private MyCustomAdapter questionAdapter;

        public SubFragment(){}

        public void setPosition(int position) {
            this.position = position;
        }

        public void setQuestionAdapter(MyCustomAdapter questionAdapter) {
            this.questionAdapter = questionAdapter;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.sub_fragment_survey_detail, container, false);

            ListView listView = (ListView) rootView.findViewById(R.id.survey_detail_questions_listview);
            listView.setItemsCanFocus(true);

            QuestionSection section = survey.getQuestionSheet().getSections().get(position);

            List<Question> questions = section.getQuestions();
            for (Question q : questions) {
                if (q instanceof ChoiceQuestion) {
                    questionAdapter.addChoiceItem((ChoiceQuestion) q);
                } else if (q instanceof TextQuestion) {
                    questionAdapter.addTextItem((TextQuestion) q);
                }
            }

            listView.setAdapter(questionAdapter);

            return rootView;
        }
    }

    public void addDots() {
        dots = new ArrayList<>();
        LinearLayout dotsLayout = (LinearLayout)rootView.findViewById(R.id.viewpager_dots);

        for(int i = 0; i < sectionsCount; i++) {
            ImageView dot = new ImageView(getActivity());
            dot.setImageDrawable(getResources().getDrawable(R.drawable.pager_dot_not_selected));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(10, 0, 10, 0);
            dot.setLayoutParams(params);

            dotsLayout.addView(dot, params);

            dots.add(dot);
        }

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                selectDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void selectDot(int idx) {
        for(int i = 0; i < sectionsCount; i++) {
            int drawableId = (i == idx) ? (R.drawable.pager_dot_selected) : (R.drawable.pager_dot_not_selected);
            Drawable drawable = ContextCompat.getDrawable(getActivity(), drawableId);
            dots.get(i).setImageDrawable(drawable);
        }
    }

    /*----------------------------------------------------------*/
    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    public int getId(String identifier) {
        return getResources().getIdentifier(identifier, "id", getContext().getPackageName());
    }

    private class MyCustomAdapter extends BaseAdapter {

        private static final int TYPE_CHOICE = 0;
        private static final int TYPE_TEXT = 1;
        private static final int TYPE_MAX_COUNT = TYPE_TEXT + 1;

        private TreeSet mTextSet = new TreeSet();

        private List<Question> mData = new ArrayList<>();
        private LayoutInflater mInflater;

        public MyCustomAdapter() {
            mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void addChoiceItem(ChoiceQuestion item) {
            mData.add(item);
            notifyDataSetChanged();
        }

        public void addTextItem(TextQuestion item) {
            mData.add(item);
            mTextSet.add(mData.size() - 1);
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            return mTextSet.contains(position) ? TYPE_TEXT : TYPE_CHOICE;
        }

        @Override
        public int getViewTypeCount() {
            return TYPE_MAX_COUNT;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Question getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout ll = null;
            ChoiceViewHolder choiceViewHolder = null;
            TextViewHolder textViewHolder = null;
            int type = getItemViewType(position);

            switch (type) {
                case TYPE_CHOICE: {
                    choiceViewHolder = new ChoiceViewHolder();
                    ll = (LinearLayout) mInflater.inflate(R.layout.list_item_question_choice, null);
                    /* if question is of type choice set choices to visible one by one */
                    ChoiceQuestion question = (ChoiceQuestion) getItem(position);
                    int choiceIndex = 0;

                    for (String choice : question.getChoices()) {
                        String identifier = "survey_detail_checkbox_" + choiceIndex;
                        int id = getId(identifier);
                        if(id > 0) {
                            choiceViewHolder.checkbox = (CheckBox) ll.findViewById(id);
                            choiceViewHolder.checkbox.setVisibility(View.VISIBLE);
                            choiceViewHolder.checkbox.setText(choice);
                        }
                        choiceIndex++;
                    }
                    choiceViewHolder.description = (TextView) ll.findViewById(R.id.survey_detail_question_description);
                    choiceViewHolder.description.setText(getItem(position).getDescription().toString());
                    convertView = ll;
                    convertView.setTag(choiceViewHolder);
                    break;
                }
                case TYPE_TEXT: {
                    textViewHolder = new TextViewHolder();
                    ll = (LinearLayout) mInflater.inflate(R.layout.list_item_question_text, null);

                    textViewHolder.description = (TextView) ll.findViewById(R.id.survey_detail_question_description);
                    textViewHolder.description.setText(getItem(position).getDescription().toString());
                    textViewHolder.answerEditText = (EditText) ll.findViewById(R.id.survey_detail_edittext_answer);

                    convertView = ll;
                    convertView.setTag(textViewHolder);
                    break;
                }
            }

            return convertView;
        }

        public class TextViewHolder{
            public TextView description;
            public EditText answerEditText;
        }

        public class ChoiceViewHolder{
            public TextView description;
            public CheckBox checkbox;
        }
    }
    /*----------------------------------------------------------*/

    public void submitSurvey(String dept, String token) {
        SurveyResponse response = new SurveyResponse(survey);
        AnswerSheet answerSheet = new AnswerSheet();
        AnswerSection answerSection;
        List<Answer> answers;
        /* get answers from view pager */
        for(int i = 0; i<pager.getChildCount(); i++) {
            View view = pager.getChildAt(i);
            ListView listView = (ListView)view.findViewById(R.id.survey_detail_questions_listview);

            answerSection = new AnswerSection();
            answers = new ArrayList<>();

            int count = listView.getCount();
            for(int itemIndex = 0; itemIndex<count; itemIndex++){
                Question question = (Question) listView.getItemAtPosition(itemIndex);
                View itemView = getViewByPosition(itemIndex, listView);

                if(question instanceof ChoiceQuestion) {
                    /* get checked choices */
                    ChoiceAnswer answer = new ChoiceAnswer();
                    answer.setQuestion(question);
                    for(int chIndex = 0; chIndex<((ChoiceQuestion) question).getChoices().size(); chIndex++) {
                        String identifier = "survey_detail_checkbox_" + chIndex;
                        CheckBox checkbox = (CheckBox)itemView.findViewById(getId(identifier));
                        if(checkbox.isChecked()) {
                            answer.getSelections().add(chIndex);
                        }
                    }
                    answers.add(answer);
                }else if(question instanceof TextQuestion) {
                    /* get text answer */
                    TextAnswer answer = new TextAnswer();
                    answer.setQuestion(question);
                    EditText editText = (EditText)itemView.findViewById(R.id.survey_detail_edittext_answer);
                    answer.setText(editText.getText().toString());
                    answers.add(answer);
                }
            }
            answerSection.setAnswers(answers);
            answerSheet.getSections().add(answerSection);
        }
        response.setAnswerSheet(answerSheet);
        /* check survey answers */
        if(validateAnswers(response)){
            /* create answers json object  and send back to API */
            Gson gson = new GsonBuilder().serializeNulls().create();
            String json = gson.toJson(response);
            Log.e("response", json);
            /* send response to API */
            /*SendResponseTask responseTask = new SendResponseTask();
            responseTask.execute(dept, token, json);*/
        }else{
            /* show error and show detail activity */
        }

    }

    public boolean validateAnswers(SurveyResponse response) {
        return true;
    }

    public class SendResponseTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            HttpURLConnection connection = null;
            OutputStream os = null;

            try{
                Uri buildUri = Uri.parse(BuildConfig.SURVEYS_BASE_URL + "/saveAnswers").buildUpon()
                        .appendQueryParameter("dept", params[0]).build();

                URL url = new URL(buildUri.toString());

                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestProperty("Authorization", "Bearer " + params[1]);
                connection.setRequestProperty("Content-Type","application/json");
                connection.setRequestMethod("POST");
                connection.connect();

                byte[] outputInBytes = params[2].getBytes("UTF-8");
                os = connection.getOutputStream();
                os.write( outputInBytes );

                int status = connection.getResponseCode();

                if(status == HttpURLConnection.HTTP_OK) {
                    /* answers are saved successfully on server */
                }else{
                    /* reset users token */
                    SharedPreferencesUtil.setSharedValues(getString(R.string.user_token_key), null, getActivity());
                    startLoginActivity();
                }
            }catch(IOException e) {
                Log.e(LOG_TAG, "Error", e);
            }finally{
                if(connection != null){
                    connection.disconnect();
                }
                if(os != null){
                    try {
                        os.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return null;
        }
    }

    public void startLoginActivity(){
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }


}
