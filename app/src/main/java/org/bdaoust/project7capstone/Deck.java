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
    private ColorPercentages mColorPercentages;

    public Deck(String deckName) {
        mDeckName = deckName;
        mCards = new HashMap<>();
        mNumbCopies = new HashMap<>();
        mColorPercentages = new ColorPercentages();
    }

    public String getDeckName() {
        return mDeckName;
    }

    public void setDeckName(String deckName) {
        mDeckName = deckName;
    }

    public long getLastUpdatedTimestamp() {
        return mLastUpdatedTimestamp;
    }

    public void setLastUpdatedTimestamp(long lastUpdatedTimestamp) {
        mLastUpdatedTimestamp = lastUpdatedTimestamp;
    }

    public void addCardCopies(@NonNull Card card, int numb) {
        int multiverseId;

        multiverseId = card.getMultiverseid();
        if (numb < 0) {
            throw new IllegalArgumentException("The number of copies can't be negative");
        }

        if (mCards.containsKey(multiverseId)) {
            mNumbCopies.put(multiverseId, mNumbCopies.get(multiverseId) + numb);
        } else {
            mCards.put(multiverseId, card);
            mNumbCopies.put(multiverseId, numb);
        }
    }

    public void setCardCopies(@NonNull Card card, int numb) {
        int multiverseId;

        multiverseId = card.getMultiverseid();
        if (numb < 0) {
            throw new IllegalArgumentException("The number of copies can't be negative");
        }

        if (numb > 0) {
            if (mCards.containsKey(multiverseId)) {
                mNumbCopies.put(multiverseId, numb);
            } else {
                mCards.put(multiverseId, card);
                mNumbCopies.put(multiverseId, numb);
            }
        } else {
            if (mCards.containsKey(multiverseId)) {
                mCards.remove(multiverseId);
                mNumbCopies.remove(multiverseId);
            }
        }
    }

    public int getNumbCopies(int multiverseId) {
        if (mCards.containsKey(multiverseId)) {
            return mNumbCopies.get(multiverseId);
        } else {
            return 0;
        }
    }

    public void removeCardCopies(int multiverseId) {
        if (mCards.containsKey(multiverseId)) {
            mCards.remove(multiverseId);
            mNumbCopies.remove(multiverseId);
        }
    }

    public ArrayList<Card> getCards() {
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

    public int getNumbCards() {
        int numbCards;

        numbCards = 0;
        for (int numbCopies : mNumbCopies.values()) {
            numbCards += numbCopies;
        }

        return numbCards;
    }

    public ColorPercentages getColorPercentages() {
        ArrayList<Card> cards;

        mColorPercentages.reset();

        cards = getCards();
        if (cards.size() < 0) {
            return mColorPercentages;
        }

        for (Card card : cards) {
            String[] colors;
            int numbColors;
            int numbCopies;

            colors = card.getColors();
            numbCopies = getNumbCopies(card.getMultiverseid());

            if (colors != null) {
                numbColors = colors.length;
                for (String color : colors) {
                    switch (color) {
                        case "Black":
                            mColorPercentages.black += calcCardColorPercentageContribution(numbCopies, numbColors);
                            break;
                        case "Blue":
                            mColorPercentages.blue += calcCardColorPercentageContribution(numbCopies, numbColors);
                            break;
                        case "Green":
                            mColorPercentages.green += calcCardColorPercentageContribution(numbCopies, numbColors);
                            break;
                        case "Red":
                            mColorPercentages.red += calcCardColorPercentageContribution(numbCopies, numbColors);
                            break;
                        case "White":
                            mColorPercentages.white += calcCardColorPercentageContribution(numbCopies, numbColors);
                            break;
                    }
                }
            }
        }

        return mColorPercentages;
    }

    private float calcCardColorPercentageContribution(int numbCopies, int numbColors) {
        return 100f * (((float)numbCopies / (float)numbColors) / (float)getNumbCards());
    }

    public class ColorPercentages {
        float black;
        float blue;
        float green;
        float red;
        float white;

        public void reset() {
            black = 0;
            blue = 0;
            green = 0;
            red = 0;
            white = 0;
        }
    }
}
