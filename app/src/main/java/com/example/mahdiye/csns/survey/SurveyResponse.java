package com.example.mahdiye.csns.survey;

import java.io.Serializable;

/**
 * Created by Mahdiye on 6/5/2016.
 */
public class SurveyResponse implements Serializable {
    private Long id;
    private Survey survey;
    private Long surveyId;
    private AnswerSheet answerSheet;

    public SurveyResponse() {
    }

    public SurveyResponse( Survey survey ) {
        this.survey = survey;
        this.surveyId = survey.getId();
        //this.answerSheet = new AnswerSheet( survey.getQuestionSheet() );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
        this.surveyId = survey.getId();
    }

    public AnswerSheet getAnswerSheet() {
        return answerSheet;
    }

    public void setAnswerSheet(AnswerSheet answerSheet) {
        this.answerSheet = answerSheet;
    }

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }
}
