package com.example.mahdiye.csns.models.survey;

/**
 * Created by Mahdiye on 5/11/2016.
 */
public class TextAnswer extends Answer {
    private String text;

    public TextAnswer()
    {
    }

    public TextAnswer( TextQuestion textQuestion ) {
        super( textQuestion );
    }

    /*@Override
    public int check() {
        return 0;
    }*/

    @Override
    public String toString() {
        return text != null ? text : "";
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
