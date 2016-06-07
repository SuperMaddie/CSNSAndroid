package com.example.mahdiye.csns.survey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mahdiye on 6/5/2016.
 */
public class AnswerSection implements Serializable {
    private Long id;
    private AnswerSheet answerSheet;
    private int index;
    private List<Answer> answers;

    public AnswerSection() {
        this.answers = new ArrayList<Answer>();
    }

    public AnswerSection( AnswerSheet answerSheet, int index ) {
        this.answerSheet = answerSheet;
        this.index = index;
        this.answers = new ArrayList<Answer>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AnswerSheet getAnswerSheet() {
        return answerSheet;
    }

    public void setAnswerSheet(AnswerSheet answerSheet) {
        this.answerSheet = answerSheet;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
