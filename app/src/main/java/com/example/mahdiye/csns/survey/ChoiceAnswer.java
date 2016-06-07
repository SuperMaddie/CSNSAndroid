package com.example.mahdiye.csns.survey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Mahdiye on 5/11/2016.
 */
public class ChoiceAnswer extends Answer {
    private Set<Integer> selections;

    public ChoiceAnswer() {
        selections = new HashSet<Integer>();
    }

    public ChoiceAnswer( ChoiceQuestion choiceQuestion ) {
        super( choiceQuestion );
        selections = new HashSet<Integer>();
    }

    @Override
    public int check()
    {
        int points = question.getPointValue();

        Set<Integer> correctSelections = ((ChoiceQuestion) question).getCorrectSelections();

        if( correctSelections.size() == 0 ) return 0;
        if( correctSelections.size() != selections.size() ) return -points;
        for( Integer selection : selections )
            if( !correctSelections.contains( selection ) ) return -points;

        return points;
    }

    @Override
    public String toString()
    {
        List<Integer> list = new ArrayList<Integer>();
        list.addAll( selections );
        Collections.sort( list );

        StringBuilder sb = new StringBuilder();
        for( int i = 0; i < list.size(); ++i )
        {
            sb.append( list.get( i ) );
            if( i < list.size() - 1 ) sb.append( "," );
        }

        return sb.toString();
    }

    public Set<Integer> getSelections() {
        return selections;
    }

    public void setSelections(Set<Integer> selections) {
        this.selections = selections;
    }
}
