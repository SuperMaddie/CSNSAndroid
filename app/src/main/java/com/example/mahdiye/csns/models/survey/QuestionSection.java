package com.example.mahdiye.csns.models.survey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mahdiye on 6/5/2016.
 */
public class QuestionSection implements Serializable {
    private Long id;
    private String description;
    private List<Question> questions;

    public QuestionSection() {
        questions = new ArrayList<Question>();
    }

    public Question getQuestion( Long questionId ) {
        for( Question question : questions )
            if( question.getId() == questionId ) return question;

        return null;
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

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
