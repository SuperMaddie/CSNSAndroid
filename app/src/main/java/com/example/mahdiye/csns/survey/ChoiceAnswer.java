package com.example.mahdiye.csns.survey;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Mahdiye on 5/11/2016.
 */
public class ChoiceAnswer extends Answer {
    private Set<Integer> selections;

    public ChoiceAnswer() {
        selections = new HashSet<>();
    }

    public Set<Integer> getSelections() {
        return selections;
    }

    public void setSelections(Set<Integer> selections) {
        this.selections = selections;
    }
}
