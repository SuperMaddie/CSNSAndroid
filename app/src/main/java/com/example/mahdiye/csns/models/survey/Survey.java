package com.example.mahdiye.csns.models.survey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
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
    private Calendar publishDate;
    private Calendar closeDate;
    boolean deleted;

    public Survey(){
        responses = new ArrayList<>();
        deleted = false;
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

    public Calendar getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Calendar publishDate) {
        this.publishDate = publishDate;
    }

    public Calendar getCloseDate() {
        return closeDate;
    }

    public void setCloseDate( Calendar closeDate ) {
        this.closeDate = closeDate;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString(){
        return name;
    }

    public static class SurveyType{
        public static final String TYPE_ANONYMOUS = "ANONYMOUS";
        public static final String TYPE_RECORDED = "RECORDED";
        public static final String TYPE_NAMED = "NAMED";
    }
}
