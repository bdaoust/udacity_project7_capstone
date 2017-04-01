package org.bdaoust.project7capstone;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import io.magicthegathering.javasdk.resource.Card;

public class Deck {

    private String mDeckName;
    private long mLastUpdatedTimestamp;
    private HashMap<Integer, Card> mCards;
    private HashMap<Integer, Integer> mNumbCopies;

    public Deck(String deckName){
        mDeckName = deckName;
        mCards = new HashMap<>();
        mNumbCopies = new HashMap<>();
    }

    public String getDeckName(){
        return mDeckName;
    }

    public void setDeckName(String deckName){
        mDeckName = deckName;
    }

    public long getLastUpdatedTimestamp(){
        return mLastUpdatedTimestamp;
    }

    public void setLastUpdatedTimestamp(long lastUpdatedTimestamp){
        mLastUpdatedTimestamp = lastUpdatedTimestamp;
    }

    public void addCardCopies(@NonNull Card card, int numb){
        int multiverseId;

        multiverseId = card.getMultiverseid();
        if(numb < 0){
            throw new IllegalArgumentException("The number of copies can't be negative");
        }

        if(mCards.containsKey(multiverseId)){
            mNumbCopies.put(multiverseId, mNumbCopies.get(multiverseId) + numb);
        } else {
            mCards.put(multiverseId, card);
            mNumbCopies.put(multiverseId,numb);
        }
    }

    public void setCardCopies(@NonNull Card card, int numb){
        int multiverseId;

        multiverseId = card.getMultiverseid();
        if(numb < 0){
            throw new IllegalArgumentException("The number of copies can't be negative");
        }

        if(numb > 0){
            if(mCards.containsKey(multiverseId)){
                mNumbCopies.put(multiverseId, numb);
            } else {
                mCards.put(multiverseId, card);
                mNumbCopies.put(multiverseId, numb);
            }
        } else {
            if(mCards.containsKey(multiverseId)){
                mCards.remove(multiverseId);
                mNumbCopies.remove(multiverseId);
            }
        }
    }

    public int getNumbCopies(int multiverseId){
        if(mCards.containsKey(multiverseId)){
            return mNumbCopies.get(multiverseId);
        } else {
            return 0;
        }
    }

    public void removeCardCopies(int multiverseId){
        if(mCards.containsKey(multiverseId)){
            mCards.remove(multiverseId);
            mNumbCopies.remove(multiverseId);
        }
    }

    public ArrayList<Card> getCards(){
        ArrayList<Card> sortedCards;

        sortedCards = new ArrayList<>(mCards.values());

        Collections.sort(sortedCards, new Comparator<Card>() {
            @Override
            public int compare(Card card1, Card card2) {
                return card1.getName().compareTo(card2.getName());
            }
        });

        return sortedCards;
    }
}
