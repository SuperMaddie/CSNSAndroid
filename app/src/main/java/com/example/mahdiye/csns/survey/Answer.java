package com.example.mahdiye.csns.survey;

/**
 * Created by Mahdiye on 5/11/2016.
 */
public abstract class Answer {
    protected Long id;
    protected int index;
    protected AnswerSection section;
    protected Question question;

    public Answer() {}

    public Answer(Question question) {
        this.question = question;
    }

    public abstract int check();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public AnswerSection getSection() {
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
    }
}
