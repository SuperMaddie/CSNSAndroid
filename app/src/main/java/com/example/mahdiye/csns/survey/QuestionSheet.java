package com.example.mahdiye.csns.survey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Mahdiye on 6/5/2016.
 */
public class QuestionSheet implements Serializable {
    private Long id;
    private String description;
    private List<QuestionSection> sections;
    private Set<AnswerSheet> answerSheets;

    public QuestionSheet() {
        sections = new ArrayList<QuestionSection>();
        answerSheets = new HashSet<AnswerSheet>();
    }

    public int getNumOfSections() {
        return sections.size();
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

    public List<QuestionSection> getSections() {
        return sections;
    }

    public void setSections(List<QuestionSection> sections) {
        this.sections = sections;
    }

    public Set<AnswerSheet> getAnswerSheets() {
        return answerSheets;
    }

    public void setAnswerSheets(Set<AnswerSheet> answerSheets) {
        this.answerSheets = answerSheets;
    }
}
