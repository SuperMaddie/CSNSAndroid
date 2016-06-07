package com.example.mahdiye.csns.survey;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Mahdiye on 5/10/2016.
 */
public class ChoiceQuestion extends Question {

    private List<String> choices;
    private Set<Integer> correctSelections;
    private int minSelections;
    private int maxSelections;

    public ChoiceQuestion(){
        choices = new ArrayList<>();
        minSelections = 0;
        maxSelections = 4;
    }

    public List<String> getChoices() {
        return choices;
    }

    public void setChoices(List<String> choices) {
        this.choices = choices;
    }

    public Set<Integer> getCorrectSelections() {
        return correctSelections;
    }

    public void setCorrectSelections(Set<Integer> correctSelections) {
        this.correctSelections = correctSelections;
    }

    public int getMinSelections() {
        return minSelections;
    }

    public void setMinSelections(int minSelections) {
        this.minSelections = minSelections;
    }

    public int getMaxSelections() {
        return maxSelections;
    }

    public void setMaxSelections(int maxSelections) {
        this.maxSelections = maxSelections;
    }

    @Override
    public Answer createAnswer() {
        ChoiceAnswer answer = new ChoiceAnswer( this );
        answers.add( answer );
        return answer;
    }

    public boolean isSingleSelection() {
        return maxSelections == 1;
    }

    public int getNumOfChoices() {
        return choices.size();
    }

}
