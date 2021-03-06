package com.example.mahdiye.csns.models.survey;

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
        selections = new HashSet<>();
    }

    public ChoiceAnswer( ChoiceQuestion choiceQuestion ) {
        super( choiceQuestion );
        selections = new HashSet<>();
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
