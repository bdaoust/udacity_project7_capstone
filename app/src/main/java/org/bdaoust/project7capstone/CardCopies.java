package org.bdaoust.project7capstone;

import android.support.annotation.NonNull;

import io.magicthegathering.javasdk.resource.Card;

public class CardCopies {
    private Card mCard;
    private int mNumbCopies;

    public CardCopies(@NonNull Card card){
        mCard = card;
        mNumbCopies = 1;
    }

    public CardCopies(@NonNull Card card, int numbCopies){
        if(numbCopies < 1){
            throw new IllegalArgumentException("Number of card copies can't be less than 1.");
        }

        mCard = card;
        mNumbCopies = numbCopies;
    }

    public Card getCard(){
        return mCard;
    }

    public int getNumbCopies(){
        return mNumbCopies;
    }

    public void setNumbCopies(int numbCopies){
        if(numbCopies < 1){
            throw new IllegalArgumentException("Number of card copies can't be less than 1.");
        }
        mNumbCopies = numbCopies;
    }
}
