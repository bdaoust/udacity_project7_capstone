package org.bdaoust.project7capstone.firebasemodels;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@IgnoreExtraProperties
public class MTGDeckModel {

    private List<MTGCardModel> mMTGCards;
    private String mName;
    private long mLastUpdatedTimestamp = 1491006413000L;
    private ColorPercentages mColorPercentages;
    private String mFirebaseKey;

    public MTGDeckModel() {
        mColorPercentages = new ColorPercentages();
    }

    public List<MTGCardModel> getMTGCards() {
        return mMTGCards;
    }

    public void setMTGCards(List<MTGCardModel> mtgCards) {
        mMTGCards = mtgCards;
        sortMTGCards();
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public long getLastUpdatedTimestamp() {
        return mLastUpdatedTimestamp;
    }

    public void setLastUpdatedTimestamp(long lastUpdatedTimestamp) {
        mLastUpdatedTimestamp = lastUpdatedTimestamp;
    }

    @Exclude
    public String getFirebaseKey(){
        return mFirebaseKey;
    }

    @Exclude
    public void setFirebaseKey(String firebaseKey){
        mFirebaseKey = firebaseKey;
    }

    @Exclude
    public int getNumbCards() {
        int numbCards = 0;

        for (MTGCardModel mtgCard : mMTGCards) {
            numbCards += mtgCard.getNumbCopies();
        }

        return numbCards;
    }

    @Exclude
    public void addCard(MTGCardModel mtgCard){
        mMTGCards.add(mtgCard);
        sortMTGCards();
    }

    @Exclude
    public void removeCard(int multiverseId){
        MTGCardModel mtgCardToRemove;

        mtgCardToRemove = null;
        for (MTGCardModel mtgCard : mMTGCards) {
            if(mtgCard.getMultiverseId() == multiverseId){
                mtgCardToRemove = mtgCard;
            }
        }

        if(mtgCardToRemove != null){
            mMTGCards.remove(mtgCardToRemove);
        }
    }

    @Exclude
    public MTGCardModel findCardByMultiverseId(int multiverseId){
        MTGCardModel foundMTGCard;

        foundMTGCard = null;
        for (MTGCardModel mtgCard : mMTGCards) {
            if(mtgCard.getMultiverseId() == multiverseId){
                foundMTGCard = mtgCard;
            }
        }

        return foundMTGCard;
    }

    public int findCardPositionByMultiverseId(int multiverseId){
        int position;

        position = -1;
        for(int i=0; i< mMTGCards.size(); i++){
            if(mMTGCards.get(i).getMultiverseId() == multiverseId){
                position = i;
            }
        }

        return position;
    }

    @Exclude
    public ColorPercentages getColorPercentages() {
        mColorPercentages.reset();

        if (mMTGCards.size() < 0) {
            return mColorPercentages;
        }

        for (MTGCardModel mtgCard : mMTGCards) {
            List<String> colors;
            int numbColors;
            int numbCopies;

            colors = mtgCard.getColors();
            numbCopies = mtgCard.getNumbCopies();

            numbColors = colors.size();
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

        return mColorPercentages;
    }

    private void sortMTGCards(){
        Collections.sort(mMTGCards, new Comparator<MTGCardModel>() {
            @Override
            public int compare(MTGCardModel mtgCard1, MTGCardModel mtgCard2) {
                return mtgCard1.getName().compareTo(mtgCard2.getName());
            }
        });
    }

    private float calcCardColorPercentageContribution(int numbCopies, int numbColors) {
        return 100f * (((float) numbCopies / (float) numbColors) / (float) getNumbCards());
    }

    public class ColorPercentages {
        public float black;
        public float blue;
        public float green;
        public float red;
        public float white;

        void reset() {
            black = 0;
            blue = 0;
            green = 0;
            red = 0;
            white = 0;
        }
    }
}
