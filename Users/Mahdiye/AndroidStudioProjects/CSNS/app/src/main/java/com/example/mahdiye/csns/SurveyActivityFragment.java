package com.example.mahdiye.csns;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class SurveyActivityFragment extends Fragment {

    ArrayAdapter<String> surveyArrayAdapter;

    public SurveyActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String[] mockSurveys = {"survey1", "survey2", "survey3", "survey4", "survey5"};
        final String[] mockSurveyDetails = {"survey1detail", "survey2survey1detail", "survey3survey1detail",
                "survey4survey1detail", "survey5survey1detail"};
        surveyArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_survey,
                R.id.survey_listview_item, Arrays.asList(mockSurveys));

        View rootView = inflater.inflate(R.layout.fragment_survey, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.survey_listview);
        listView.setAdapter(surveyArrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), SurveyDetailActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, mockSurveyDetails[position]);
                startActivity(intent);
            }
        });

        return rootView;
    }
}
