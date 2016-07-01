package com.example.mahdiye.csns;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.util.SparseArray;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mahdiye.csns.data.CSNSContract;
import com.example.mahdiye.csns.models.survey.Answer;
import com.example.mahdiye.csns.models.survey.AnswerSection;
import com.example.mahdiye.csns.models.survey.AnswerSheet;
import com.example.mahdiye.csns.models.survey.ChoiceAnswer;
import com.example.mahdiye.csns.models.survey.ChoiceQuestion;
import com.example.mahdiye.csns.models.survey.ChoiceType;
import com.example.mahdiye.csns.models.survey.Question;
import com.example.mahdiye.csns.models.survey.QuestionSection;
import com.example.mahdiye.csns.models.survey.Survey;
import com.example.mahdiye.csns.models.survey.SurveyResponse;
import com.example.mahdiye.csns.models.survey.TextAnswer;
import com.example.mahdiye.csns.models.survey.TextQuestion;
import com.example.mahdiye.csns.utils.SharedPreferencesUtil;
import com.example.mahdiye.csns.utils.SurveyUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * A placeholder fragment containing a simple view.
 */
public class SurveyDetailActivityFragment extends Fragment implements Serializable{

    final String LOG_TAG = SurveyDetailActivityFragment.class.getSimpleName();
    private static Survey survey;
    private static SurveyResponse response;
    private static SurveyResponse tempResponse;
    private static View rootView;
    private static ViewPager pager;
    private transient List<ImageView> dots;
    private static int sectionsCount = 0;
    private boolean lastPageSeen = false;
    private Context context;

    private String DEPT;
    private String TOKEN;

    public SurveyDetailActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        context = getActivity();
        DEPT = getActivity().getString(R.string.department);
        TOKEN = SharedPreferencesUtil.getSharedValues(getString(R.string.user_token_key), getActivity());
        response = null;

        /* set sections count for pager*/
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra("survey")) {
            survey = (Survey) intent.getSerializableExtra("survey");
            sectionsCount = survey.getQuestionSheet().getSections().size();
            if(sectionsCount < 2){
                lastPageSeen = true;
            }
        }

        /* get response from db if exists and if survey is not anonymous */
        if(!survey.getType().equals(Survey.SurveyType.TYPE_ANONYMOUS)) {
            Cursor cursor = getResponseCursor(survey.getId());
            if (cursor.moveToFirst()) {
                response = SurveyUtils.getSurveyResponseFromCursor(cursor);
                cursor.close();
                lastPageSeen = true;
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        tempResponse = getResponseFromPager();
        bundle.putSerializable("tempResponse", tempResponse);
        super.onSaveInstanceState(bundle);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!= null && savedInstanceState.containsKey("tempResponse")){
            tempResponse = (SurveyResponse) savedInstanceState.getSerializable("tempResponse");
        }
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
            /* if last page is not seen show a popup and do not send the response */
            if(lastPageSeen == false){
                showPopup("There are some sections that you have missed. \nYou can swipe to navigate through pages.");
                return true;
            }

            final String TOKEN = SharedPreferencesUtil.getSharedValues(getString(R.string.user_token_key), getActivity());
            String DEPT = getActivity().getString(R.string.department);
            boolean responseValid = submitSurvey(DEPT, TOKEN);
            if(responseValid) {
                Intent intent = new Intent(getActivity(), SurveyActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_survey_detail, container, false);

        pager = (ViewPager)rootView.findViewById(R.id.viewpager_questions);
        pager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
        //pager.setPageTransformer(true, new RotateUpTransformer());
        if(sectionsCount > 1) {
            addDots();
            selectDot(0);
        }

        ((SurveyDetailActivity)getActivity()).setActionBarTitle("Page " + (1) + " of " + sectionsCount);

        return rootView;
    }


    /*----------------View Pager Custom Adapter----------------*/
    private class MyPagerAdapter extends FragmentStatePagerAdapter {
        SparseArray<Fragment> registeredFragments = new SparseArray<>();

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return sectionsCount;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int pos) {

            /*FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();*/

            Fragment fragment = null;
            //if(getRegisteredFragment(pos) == null) {
                fragment = (Fragment) super.instantiateItem(container, pos);
                //registeredFragments.put(pos, fragment);
            /*}else{
                fragment = getRegisteredFragment(pos);
            }*/
            return fragment;
        }

        @Override
        public Fragment getItem(int pos) {
            //if(getRegisteredFragment(pos) == null) {
            Fragment fragment = new SubFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("position", pos);
            bundle.putSerializable("customAdapter", new MyCustomAdapter());
            fragment.setArguments(bundle);
            return fragment;
            /*}else {
                return getRegisteredFragment(pos);
            }*/
        }

    }

    public static class SubFragment extends Fragment {
        private int position;
        private MyCustomAdapter questionAdapter;

        public SubFragment(){}

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            Bundle bundle = this.getArguments();
            position = bundle.getInt("position");
            this.questionAdapter = (MyCustomAdapter) bundle.getSerializable("customAdapter");
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.sub_fragment_survey_detail, container, false);

            TextView descriptionTextView = (TextView) rootView.findViewById(R.id.section_description_textview);

            ListView listView = (ListView) rootView.findViewById(R.id.survey_detail_questions_listview);
            listView.setItemsCanFocus(true);

            QuestionSection questionSection = survey.getQuestionSheet().getSections().get(position);
            descriptionTextView.setText(Html.fromHtml(questionSection.getDescription()));

            AnswerSection answerSection = null;
            List<Answer> answers = null;
            if(tempResponse != null){
                answerSection = tempResponse.getAnswerSheet().getSections().get(position);
                answers = answerSection.getAnswers();
            }else if(response != null){
                answerSection = response.getAnswerSheet().getSections().get(position);
                answers = answerSection.getAnswers();
            }

            List<Question> questions = questionSection.getQuestions();
            for (int i = 0; i<questions.size(); i++) {
                Question question = questions.get(i);
                Answer answer = null;
                if(answers != null && answers.size() > i){
                    answer = answers.get(i);
                }
                if (question instanceof ChoiceQuestion) {
                    ChoiceAnswer choiceAnswer = null;
                    if(answer != null){
                        choiceAnswer = (ChoiceAnswer)answer;
                    }
                    questionAdapter.addChoiceItem((ChoiceQuestion) question, choiceAnswer);
                } else if (question instanceof TextQuestion) {
                    TextAnswer textAnswer = null;
                    if(answer != null){
                        textAnswer = (TextAnswer)answer;
                    }
                    questionAdapter.addTextItem((TextQuestion) question, textAnswer);
                }
            }

            listView.setAdapter(questionAdapter);

            return rootView;
        }
    }

    public void addDots() {
        dots = new ArrayList<>();
        LinearLayout dotsLayout = (LinearLayout)rootView.findViewById(R.id.viewpager_dots);

        if(sectionsCount > 1) {
            for (int i = 0; i < sectionsCount; i++) {
                ImageView dot = new ImageView(getActivity());
                dot.setImageDrawable(getResources().getDrawable(R.drawable.pager_dot_not_selected));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(45, 45);
                params.setMargins(10, 0, 10, 0);
                dot.setLayoutParams(params);

                dotsLayout.addView(dot, params);

                dots.add(dot);
            }
        }

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                ((SurveyDetailActivity)getActivity()).setActionBarTitle("Page " + (position + 1) + " of " + sectionsCount);
                selectDot(position);

                /* if last page is seen set the flag to true */
                if(position == sectionsCount - 1){
                    lastPageSeen = true;
                }
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
        return context.getResources().getIdentifier(identifier, "id", context.getPackageName());
    }

    public int createId(Long questionId, int choiceIndex){
        String s = String.valueOf(questionId) + String.valueOf(choiceIndex);
        return Integer.valueOf(s);
    }

    public String getHint(int minSelections, int maxSelections, int numberOfChoices){
        String res = "";
        if(minSelections == 1 && maxSelections == 1){
            res = "<font color=" + context.getResources().getColor(R.color.colorPrimary) + ">&nbsp (Required) </font>";
            return res;
        }

        if(maxSelections < numberOfChoices){
            res = "<font color=" + context.getResources().getColor(R.color.colorPrimary) + ">&nbsp (Please select between " + minSelections + " and "
                    + maxSelections + " of the choices) </font>";
        }
        return res;
    }

    public ChoiceType getTypeOfChoice(int minSelections, int maxSelections){
        if(minSelections == 1 && maxSelections == 1){
            return ChoiceType.RADIO_BUTTON;
        }
        return ChoiceType.CHECK_BOX;
    }

    private class MyCustomAdapter extends BaseAdapter implements Serializable{

        private static final int TYPE_CHOICE = 0;
        private static final int TYPE_TEXT = 1;
        private static final int TYPE_MAX_COUNT = TYPE_TEXT + 1;

        private TreeSet mTextSet = new TreeSet();

        private List<Question> mData = new ArrayList<>();
        private List<Answer> mAnswerData = new ArrayList<>();
        private transient LayoutInflater mInflater;

        public MyCustomAdapter() {
            mInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void addChoiceItem(ChoiceQuestion item, ChoiceAnswer answer) {
            mData.add(item);
            if(answer != null) {
                mAnswerData.add(answer);
            }
            notifyDataSetChanged();
        }

        public void addTextItem(TextQuestion item, TextAnswer answer) {
            mData.add(item);
            mTextSet.add(mData.size() - 1);
            if(answer != null) {
                mAnswerData.add(answer);
            }
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

        public Answer getItemAnswer(int position) {
            return (mAnswerData.size() > position) ? mAnswerData.get(position) : null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout ll = null;
            CheckViewHolder checkViewHolder = null;
            RadioViewHolder radioViewHolder = null;
            TextViewHolder textViewHolder = null;
            int type = getItemViewType(position);

            switch (type) {
                case TYPE_CHOICE: {
                    ll = (LinearLayout) mInflater.inflate(R.layout.list_item_question_choice, null);
                    /* if question is of type choice set choices to visible one by one */
                    ChoiceQuestion question = (ChoiceQuestion) getItem(position);
                    ChoiceAnswer answer = null;
                    if(getItemAnswer(position) != null) {
                        answer = (ChoiceAnswer) getItemAnswer(position);
                    }

                    CheckBox checkBox;
                    RadioButton radioButton;
                    List<String> choices = question.getChoices();
                    if(getTypeOfChoice(question.getMinSelections(), question.getMaxSelections()) == ChoiceType.RADIO_BUTTON){

                        //if(convertView == null){
                        radioViewHolder = new RadioViewHolder();
                        RadioGroup radioGroup = new RadioGroup(context);
                        radioGroup.setId((int)(long)question.getId());
                        ll.addView(radioGroup);

                        for (int i = 0; i<choices.size(); i++) {
                            int id = createId(question.getId() , i);
                            radioButton = new RadioButton(context);
                            radioButton.setId(id);
                            radioGroup.addView(radioButton);

                            radioViewHolder.radioGroup = radioGroup;
                            radioViewHolder.radioButton = radioButton;
                            radioViewHolder.radioButton.setText(Html.fromHtml(choices.get(i)));

                            /*if user has selected the choice before perform click it */
                            if(answer != null && answer.getSelections().contains(i)){
                                radioViewHolder.radioButton.setChecked(true);
                            }

                        }
                        convertView = ll;
                        convertView.setTag(radioViewHolder);
                        /*}
                        else {
                            radioViewHolder = (RadioViewHolder) convertView.getTag();
                        }*/

                        radioViewHolder.description = (TextView) ll.findViewById(R.id.survey_detail_question_description);
                        radioViewHolder.description.setText(Html.fromHtml(question.getDescription().toString() +
                            getHint(question.getMinSelections(), question.getMaxSelections(), question.getNumOfChoices())));
                    }else if(getTypeOfChoice(question.getMinSelections(), question.getMaxSelections()) == ChoiceType.CHECK_BOX){
                        //if(convertView == null){
                        checkViewHolder = new CheckViewHolder();
                        for (int i = 0; i<choices.size(); i++) {
                            int id = createId(question.getId() , i);
                            checkBox = new CheckBox(context);
                            checkBox.setId(id);
                            ll.addView(checkBox);

                            checkViewHolder.checkbox = checkBox;
                            checkViewHolder.checkbox.setText(Html.fromHtml(choices.get(i)));

                            /*if user has selected the choice before perform click it */
                            if(answer != null && answer.getSelections().contains(i)){
                                checkViewHolder.checkbox.setChecked(true);
                            }
                        }
                        convertView = ll;
                        convertView.setTag(checkViewHolder);
                        /*}
                        else {
                            checkViewHolder = (CheckViewHolder) convertView.getTag();
                        }*/

                        checkViewHolder.description = (TextView) ll.findViewById(R.id.survey_detail_question_description);
                        checkViewHolder.description.setText(Html.fromHtml(question.getDescription().toString() +
                                getHint(question.getMinSelections(), question.getMaxSelections(), question.getNumOfChoices())));

                    }

                    break;
                }
                case TYPE_TEXT: {
                    textViewHolder = new TextViewHolder();
                    ll = (LinearLayout) mInflater.inflate(R.layout.list_item_question_text, null);

                    TextAnswer answer = null;
                    if(getItemAnswer(position) != null) {
                        answer = (TextAnswer) getItemAnswer(position);
                    }

                    textViewHolder.description = (TextView) ll.findViewById(R.id.survey_detail_question_description);
                    textViewHolder.description.setText(Html.fromHtml(getItem(position).getDescription().toString()));
                    textViewHolder.answerEditText = (EditText) ll.findViewById(R.id.survey_detail_edittext_answer);
                    /* set user's previous answer in text field */
                    if(answer != null) {
                        textViewHolder.answerEditText.setText(answer.getText());
                    }

                    convertView = ll;
                    convertView.setTag(textViewHolder);
                    break;
                }
            }
            return convertView;
        }

        public class TextViewHolder implements Serializable{
            public TextView description;
            public EditText answerEditText;
        }

        public class CheckViewHolder implements Serializable{
            public TextView description;
            public CheckBox checkbox;
        }

        public class RadioViewHolder implements Serializable{
            public TextView description;
            public RadioGroup radioGroup;
            public RadioButton radioButton;
        }
    }
    /*----------------------------------------------------------*/

    public SurveyResponse getResponseFromPager(){
        SurveyResponse response = new SurveyResponse(survey);
        AnswerSheet answerSheet = response.getAnswerSheet();
        AnswerSection answerSection;
        List<Answer> answers;

        /* get answers from view pager */
        for(int i = 0; i<pager.getChildCount(); i++) {
            /*long questionSectionId = fragment.getQuestionSectionId();*/
            SubFragment fragment = (SubFragment) (((MyPagerAdapter)pager.getAdapter()).getRegisteredFragment(i));

            View view = pager.getChildAt(pager.getChildCount() - i - 1);
            ListView listView = (ListView)view.findViewById(R.id.survey_detail_questions_listview);

            answerSection = new AnswerSection(i);
            answers = new ArrayList<>();

            int count = listView.getCount();
            for(int itemIndex = 0; itemIndex<count; itemIndex++){
                Question question = (Question) listView.getItemAtPosition(itemIndex);
                View itemView = getViewByPosition(itemIndex, listView);

                if(question instanceof ChoiceQuestion) {
                    /* get checked choices */
                    ChoiceAnswer answer = new ChoiceAnswer((ChoiceQuestion) question);
                    ChoiceQuestion choiceQuestion = (ChoiceQuestion) question;
                    RadioGroup radioGroup = (RadioGroup) itemView.findViewById((int)(long)question.getId());

                    if(getTypeOfChoice(choiceQuestion.getMinSelections(), choiceQuestion.getMaxSelections()) == ChoiceType.RADIO_BUTTON) {
                        int radioButtonId = radioGroup.getCheckedRadioButtonId();
                        View radioButton = radioGroup.findViewById(radioButtonId);
                        int idx = radioGroup.indexOfChild(radioButton);

                        answer.getSelections().add(idx);
                    } else if(getTypeOfChoice(choiceQuestion.getMinSelections(), choiceQuestion.getMaxSelections()) == ChoiceType.CHECK_BOX) {
                        for (int chIndex = 0; chIndex < choiceQuestion.getChoices().size(); chIndex++) {
                            //String identifier = "survey_detail_checkbox_" + chIndex;
                            int identifier = createId(question.getId(), chIndex);
                            CheckBox checkbox = (CheckBox) itemView.findViewById(identifier);
                            if (checkbox.isChecked()) {
                                answer.getSelections().add(chIndex);
                            }
                        }
                    }
                    answers.add(answer);
                }else if(question instanceof TextQuestion) {
                    /* get text answer */
                    TextAnswer answer = new TextAnswer((TextQuestion) question);
                    EditText editText = (EditText)itemView.findViewById(R.id.survey_detail_edittext_answer);
                    answer.setText(editText.getText().toString());
                    answers.add(answer);
                }
            }
            answerSection.setAnswers(answers);
            answerSheet.getSections().add(answerSection);
        }
        return response;
    }

    public boolean submitSurvey(String dept, String token) {
        SurveyResponse response = getResponseFromPager();
        /* check survey answers */
        String errorToShow = validateAnswers(response);
        if(errorToShow.isEmpty()){
            /* save the respose locally */
            if(TOKEN != null) {
                saveResponse(response);
            }
            Toast.makeText(getActivity(), "Your response is successfully submitted.", Toast.LENGTH_SHORT).show();

            /* create answers json object  and send back to API */
            Gson gson = new GsonBuilder().serializeNulls().create();
            String json = gson.toJson(response);

            Log.e("response", json);
            if(token != null) {
                Log.e("token", token);
            }
            /* send response to API */
            SendResponseTask responseTask = new SendResponseTask();
            responseTask.execute(dept, token, json);
            return true;
        }else{
            /* show error and show detail activity */
            if(lastPageSeen) {
                Toast.makeText(getActivity(), errorToShow, Toast.LENGTH_LONG).show();
            }
            return false;
        }
    }

    public String validateAnswers(SurveyResponse response) {

        List<AnswerSection> sections = response.getAnswerSheet().getSections();
        List<Question> questions;
        /*boolean containsChoiceQuestion = false;

        for(int i = 0; i<sections.size(); i++) {
            questions = survey.getQuestionSheet().getSections().get(i).getQuestions();
            for (int j = 0; j < questions.size(); j++) {
                Question question = questions.get(j);
                if (question instanceof ChoiceQuestion) {
                    containsChoiceQuestion = true;
                    break;
                }
            }
            if(containsChoiceQuestion){
                break;
            }
        }*/

        /* if questions are all text question check to see if response is not empty*/
        /*boolean allAnswersEmpty = true;
        if (!containsChoiceQuestion) {
            for (int i = 0; i < sections.size(); i++) {
                List<Answer> answers = sections.get(i).getAnswers();
                questions = survey.getQuestionSheet().getSections().get(i).getQuestions();
                for (int j = 0; j < questions.size(); j++) {
                    TextAnswer textAnswer = (TextAnswer) answers.get(j);
                    if (!textAnswer.getText().isEmpty()) {
                        allAnswersEmpty = false;
                        break;
                    }
                }
                if(!allAnswersEmpty){
                    break;
                }
            }
            if (allAnswersEmpty) {
                return "You can't submit an empty response.";
            }
        } else {*/
        for (int i = 0; i < sections.size(); i++) {
            List<Answer> answers = sections.get(i).getAnswers();
            questions = survey.getQuestionSheet().getSections().get(i).getQuestions();

            for (int j = 0; j < questions.size(); j++) {
                Question question = questions.get(j);
                if (question instanceof ChoiceQuestion) {
                    ChoiceQuestion choiceQuestion = (ChoiceQuestion) question;

                    ChoiceAnswer choiceAnswer = (ChoiceAnswer) answers.get(j);
                    if (getTypeOfChoice(choiceQuestion.getMinSelections(), choiceQuestion.getMaxSelections()) ==
                            ChoiceType.RADIO_BUTTON && choiceAnswer.getSelections().size() == 0) {
                        return "It is required to answer question " + (j + 1) + " in page " + (i + 1);
                    }else if(getTypeOfChoice(choiceQuestion.getMinSelections(), choiceQuestion.getMaxSelections()) ==
                            ChoiceType.CHECK_BOX){
                        if (choiceAnswer.getSelections().size() > choiceQuestion.getMaxSelections()) {
                            return "You can select at most " + choiceQuestion.getMaxSelections() + " choices in question "
                                    + (j + 1) + " in page " + (i + 1);
                        }
                        if (choiceAnswer.getSelections().size() < choiceQuestion.getMinSelections()) {
                            return "You must select at least " + choiceQuestion.getMinSelections() + " choices in question "
                                    + (j + 1) + " in page " + (i + 1);
                        }
                    }
                }
            }
        }
        //}
        return "";
    }

    public class SendResponseTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            OutputStream os = null;
            String res = null;

            try{
                Uri buildUri = null;
                if(TOKEN == null){
                    buildUri = Uri.parse(BuildConfig.BASE_URL + "/anonymous/survey/saveAnswers").buildUpon()
                            .appendQueryParameter("dept", params[0]).build();
                }
                else {
                    buildUri = Uri.parse(BuildConfig.SURVEYS_BASE_URL + "/saveAnswers").buildUpon()
                            .appendQueryParameter("dept", params[0]).build();
                }
                URL url = new URL(buildUri.toString());

                connection = (HttpURLConnection)url.openConnection();
                if(TOKEN != null) {
                    connection.setRequestProperty("Authorization", "Bearer " + params[1]);
                }
                connection.setRequestProperty("Content-Type","application/json");
                connection.setRequestMethod("POST");
                connection.connect();

                byte[] outputInBytes = params[2].getBytes("UTF-8");
                os = connection.getOutputStream();
                os.write( outputInBytes );

                int status = connection.getResponseCode();

                if(status == HttpURLConnection.HTTP_OK) {
                    /* answers are saved successfully on server */
                    res =  String.valueOf(status);
                }else{
                    /* show error */
                    res = String.valueOf(status);
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
            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s != null){
                Log.e(LOG_TAG, s);
            }
        }
    }

    public void startLoginActivity(){
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    Long saveResponse(SurveyResponse response) {
        Long responseId;

        ContentValues responseValues = new ContentValues();
        responseValues.put(CSNSContract.SurveyResponseEntry._ID, response.getSurveyId());
        responseValues.put(CSNSContract.SurveyResponseEntry.COLUMN_SURVEY_RESPONSE_JSON, SurveyUtils.getSurveyResponseBytes(response));

        /* First, check if the survey with this id exists in the db */
        Cursor responseCursor = getActivity().getContentResolver().query(
                CSNSContract.SurveyResponseEntry.CONTENT_URI,
                new String[]{CSNSContract.SurveyResponseEntry._ID},
                CSNSContract.SurveyResponseEntry._ID + " = ?",
                new String[]{response.getSurveyId().toString()},
                null);

        if (responseCursor.moveToFirst()) {
            /* if the id exists update it */
            int rowsUpdated = getActivity().getContentResolver().update(
                    CSNSContract.SurveyResponseEntry.CONTENT_URI,
                    responseValues,
                    CSNSContract.SurveyResponseEntry._ID + " = ?",
                    new String[]{response.getSurveyId().toString()}
            );
            responseId = Long.valueOf(response.getSurveyId());
        } else {
            /* insert the response into db */
            Uri insertedUri = getActivity().getContentResolver().insert(
                    CSNSContract.SurveyResponseEntry.CONTENT_URI,
                    responseValues
            );
            // The resulting URI contains the ID for the row.  Extract the questionId from the Uri.
            responseId = ContentUris.parseId(insertedUri);
        }

        responseCursor.close();
        return responseId;
    }

    Cursor getResponseCursor(Long id){
        Cursor responseCursor = null;
        responseCursor = getActivity().getContentResolver().query(
                CSNSContract.SurveyResponseEntry.CONTENT_URI,
                null,
                CSNSContract.SurveyResponseEntry._ID + " = ?",
                new String[]{id.toString()},
                null);

        return  responseCursor;
    }
}
