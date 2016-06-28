package com.example.mahdiye.csns.models.survey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mahdiye on 6/5/2016.
 */
public class AnswerSection implements Serializable {
    private int index;
    private Long questionSectionId;
    private List<Answer> answers;

    public AnswerSection() {
        this( 0 );
    }

    public AnswerSection( int index ) {
        this.index = index;
        this.answers = new ArrayList<>();
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Long getQuestionSectionId() {
        return questionSectionId;
    }

    public void setQuestionSectionId(Long questionSectionId) {
        this.questionSectionId = questionSectionId;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
