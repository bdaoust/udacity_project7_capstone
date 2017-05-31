package org.bdaoust.project7capstone.firebasemodels;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@IgnoreExtraProperties
public class MTGDeckModel {

    private List<MTGCardModel> mMTGCardModels;
    private String mName;
    private long mLastUpdatedTimestamp = 1491006413000L;
    private ColorPercentages mColorPercentages;

    public MTGDeckModel() {
        mColorPercentages = new ColorPercentages();
    }

    public List<MTGCardModel> getMTGCardModels() {
        return mMTGCardModels;
    }

    public void setMTGCardModels(List<MTGCardModel> mtgCardModels) {
        mMTGCardModels = mtgCardModels;

        Collections.sort(mMTGCardModels, new Comparator<MTGCardModel>() {
            @Override
            public int compare(MTGCardModel mtgCardModel1, MTGCardModel mtgCardModel2) {
                return mtgCardModel1.getName().compareTo(mtgCardModel2.getName());
            }
        });
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
    public int getNumbCards() {
        int numbCards = 0;

        for (MTGCardModel mtgCardModel : mMTGCardModels) {
            numbCards += mtgCardModel.getNumbCopies();
        }

        return numbCards;
    }

    @Exclude
    public ColorPercentages getColorPercentages() {
        mColorPercentages.reset();

        if (mMTGCardModels.size() < 0) {
            return mColorPercentages;
        }

        for (MTGCardModel mtgCard : mMTGCardModels) {
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

    private float calcCardColorPercentageContribution(int numbCopies, int numbColors) {
        return 100f * (((float) numbCopies / (float) numbColors) / (float) getNumbCards());
    }

    public class ColorPercentages {
        public float black;
        public float blue;
        public float green;
        public float red;
        public float white;

        public void reset() {
            black = 0;
            blue = 0;
            green = 0;
            red = 0;
            white = 0;
        }
    }
}
