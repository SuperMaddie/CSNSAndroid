package com.example.mahdiye.csns.models.survey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mahdiye on 5/9/2016.
 */
public class Survey implements Serializable {

    private Long id;
    private String name;
    private String type;
    private QuestionSheet questionSheet;
    private List<SurveyResponse> responses;

    public Survey(){
        responses = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public QuestionSheet getQuestionSheet() {
        return questionSheet;
    }

    public void setQuestionSheet(QuestionSheet questionSheet) {
        this.questionSheet = questionSheet;
    }

    public List<SurveyResponse> getResponses() {
        return responses;
    }

    public void setResponses(List<SurveyResponse> responses) {
        this.responses = responses;
    }

    @Override
    public String toString(){
        return name;
    }
}
