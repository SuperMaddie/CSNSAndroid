package com.example.mahdiye.csns.models.survey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Mahdiye on 6/5/2016.
 */
public class AnswerSheet implements Serializable {
    /*private Long id;
    private QuestionSheet questionSheet;*/
    private List<AnswerSection> sections;
    private Date date;
    private Long questionSheetId;

    public AnswerSheet() {
        sections = new ArrayList<>();
    }

    public AnswerSheet( QuestionSheet questionSheet )
    {
        //this.questionSheet = questionSheet;
        this.questionSheetId = questionSheet.getId();

        sections = new ArrayList<>();
        /*for( int i = 0; i < questionSheet.getSections().size(); ++i )
        {
            AnswerSection answerSection = new AnswerSection( this, i );
            List<Question> questions = questionSheet.getSections()
                    .get( i )
                    .getQuestions();
            for( int j = 0; j < questions.size(); ++j )
            {
                Answer answer = questions.get( j ).createAnswer();
                answer.setSection( answerSection );
                answer.setIndex( j );
                answerSection.getAnswers().add( answer );
            }
            sections.add( answerSection );
        }*/
    }

    /*public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuestionSheet getQuestionSheet() {
        return questionSheet;
    }

    public void setQuestionSheet(QuestionSheet questionSheet) {
        this.questionSheet = questionSheet;
    }*/

    public Long getQuestionSheetId() {
        return questionSheetId;
    }

    public void setQuestionSheetId(Long questionSheetId) {
        this.questionSheetId = questionSheetId;
    }

    public List<AnswerSection> getSections() {
        return sections;
    }

    public void setSections(List<AnswerSection> sections) {
        this.sections = sections;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
