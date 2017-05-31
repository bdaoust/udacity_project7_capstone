package org.bdaoust.project7capstone.firebasemodels;


import java.util.List;

public class MTGDeckModel {

    private List<MTGCardModel> mMTGCardModels;
    private String mName;
    private long mLastUpdatedTimestamp = 1491006413000L;

    public MTGDeckModel(){
    }

    public List<MTGCardModel> getMTGCardModels(){
        return mMTGCardModels;
    }

    public void setMTGCardModels(List<MTGCardModel> mtgCardModels){
        mMTGCardModels = mtgCardModels;
    }

    public String getName(){
        return mName;
    }

    public void setName(String name){
        mName = name;
    }

    public long getLastUpdatedTimestamp(){
        return mLastUpdatedTimestamp;
    }

    public void setLastUpdatedTimestamp(long lastUpdatedTimestamp){
        mLastUpdatedTimestamp = lastUpdatedTimestamp;
    }

}
