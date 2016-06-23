package com.example.mahdiye.csns.models.survey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mahdiye on 5/9/2016.
 */
public abstract class Question implements Serializable {

    protected Long id;
    protected String description;
    protected int pointValue;
    protected List<Answer> answers;

    public Question(){
        pointValue = 1;
        answers = new ArrayList<>();
    }

    public abstract Answer createAnswer();
    public abstract String getType();

    public int getNumOfAnswers() {
        return answers.size();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPointValue() {
        return pointValue;
    }

    public void setPointValue(int pointValue) {
        this.pointValue = pointValue;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
