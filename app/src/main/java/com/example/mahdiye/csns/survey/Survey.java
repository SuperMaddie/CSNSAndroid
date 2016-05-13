package com.example.mahdiye.csns.survey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mahdiye on 5/9/2016.
 */
public class Survey implements Serializable {

    private long id;
    private String name;
    private String type;
    private List<Question> questions;

    public Survey(){
        questions = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
