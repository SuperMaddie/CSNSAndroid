package com.example.mahdiye.csns.survey;

import com.example.mahdiye.csns.survey.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mahdiye on 5/10/2016.
 */
public class ChoiceQuestion extends Question {

    private int choiceValue;
    private List<String> choices;
    private int minSelections;
    private int maxSelections;
    private int numOfChoices;
    private boolean singleSelection;

    public ChoiceQuestion(){
        choices = new ArrayList<>();
    }

    public int getChoiceValue() {
        return choiceValue;
    }

    public void setChoiceValue(int choiceValue) {
        this.choiceValue = choiceValue;
    }

    public List<String> getChoices() {
        return choices;
    }

    public void setChoices(List<String> choices) {
        this.choices = choices;
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

    public int getNumOfChoices() {
        return numOfChoices;
    }

    public void setNumOfChoices(int numOfChoices) {
        this.numOfChoices = numOfChoices;
    }

    public boolean isSingleSelection() {
        return singleSelection;
    }

    public void setSingleSelection(boolean singleSelection) {
        this.singleSelection = singleSelection;
    }
}
