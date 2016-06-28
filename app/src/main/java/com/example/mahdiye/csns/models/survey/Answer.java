package com.example.mahdiye.csns.models.survey;

import java.io.Serializable;

/**
 * Created by Mahdiye on 5/11/2016.
 */
public class Answer implements Serializable{
    /*protected Long id;
    protected int index;
    protected AnswerSection section;
    protected Question question;*/

    private Long questionId;
    private int index;

    public Answer() {}

    public Answer(Question question) {
        this.questionId = question.getId();
    }

    /*public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }*/

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    /*public AnswerSection getSection() {
        return section;
    }

    public void setSection(AnswerSection section) {
        this.section = section;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }*/
}
