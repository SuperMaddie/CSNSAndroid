package com.example.mahdiye.csns.models.survey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Mahdiye on 6/5/2016.
 */
public class AnswerSheet implements Serializable {
    private List<AnswerSection> sections;
    private Date date;
    private Long questionSheetId;

    public AnswerSheet() {
        sections = new ArrayList<>();
    }

    public AnswerSheet( QuestionSheet questionSheet )
    {
        this.questionSheetId = questionSheet.getId();
        sections = new ArrayList<>();
    }

    public Long getQuestionSheetId() {
        return questionSheetId;
    }

    public void setQuestionSheetId(Long questionSheetId) {
        this.questionSheetId = questionSheetId;
    }

    public List<AnswerSection> getSections() {
        return sections;
    }

    public void setSections(List<AnswerSection> sections) {
        this.sections = sections;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
