package com.example.mahdiye.csns.models.survey;

import java.io.Serializable;

/**
 * Created by Mahdiye on 6/5/2016.
 */
public class SurveyResponse implements Serializable {
    private Long surveyId;
    private AnswerSheet answerSheet;

    public SurveyResponse() {
    }

    public SurveyResponse( Survey survey ) {
        this.surveyId = survey.getId();
        this.answerSheet = new AnswerSheet( survey.getQuestionSheet() );
    }

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public AnswerSheet getAnswerSheet() {
        return answerSheet;
    }

    public void setAnswerSheet(AnswerSheet answerSheet) {
        this.answerSheet = answerSheet;
    }
}
