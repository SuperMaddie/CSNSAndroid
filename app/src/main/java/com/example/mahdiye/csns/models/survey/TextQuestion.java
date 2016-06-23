package com.example.mahdiye.csns.models.survey;

/**
 * Created by Mahdiye on 5/10/2016.
 */
public class TextQuestion extends Question {
    private  String correctAnswer;
    private int textLength;

    public TextQuestion(){
        textLength = 20;
    }

    @Override
    public String getType(){
        return "TEXT";
    }

    @Override
    public Answer createAnswer() {
        TextAnswer answer = new TextAnswer( this );
        answers.add( answer );
        return answer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public int getTextLength() {
        return textLength;
    }

    public void setTextLength(int textLength) {
        this.textLength = textLength;
    }
}
