package com.example.mahdiye.csns.survey;

import java.io.Serializable;

/**
 * Created by Mahdiye on 5/9/2016.
 */
public class Question implements Serializable {

    private long id;
    private String description;
    private int pointValue;
    private String type;


    public Question(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPointValue() {
        return pointValue;
    }

    public void setPointValue(int pointValue) {
        this.pointValue = pointValue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
