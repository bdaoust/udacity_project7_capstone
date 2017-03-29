package org.bdaoust.project7capstone;

import java.util.ArrayList;

import io.magicthegathering.javasdk.resource.Card;

public class Deck {

    private String mDeckName;
    private ArrayList<Card> mCards;

    public Deck(String deckName){
        mDeckName = deckName;
        mCards = new ArrayList<>();
    }

    public String getDeckName(){
        return mDeckName;
    }

    public void setDeckName(String deckName){
        mDeckName = deckName;
    }

    public void addCardCopies(Card card, int numb){
    }

    public void setCardCopies(Card card, int numb){

    }

    public int getNumbCopies(int multiverseId){
        return 0;
    }

    public void removeCardCopies(int multiverseId){
    }

    public ArrayList<Card> getCards(){
        return null;
    }
}
