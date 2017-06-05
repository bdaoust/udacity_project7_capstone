package org.bdaoust.project7capstone.firebasemodels;


import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

import io.magicthegathering.javasdk.resource.Card;

public class MTGCardModel {

    private String mFirebaseKey;
    private String mArtist;
    private double mCmc;
    private List<String> mColors;
    private String mFlavorText;
    private String mImageUrl;
    private int mLoyalty;
    private String mManaCost;
    private int mMultiverseId;
    private String mName;
    private int mNumbCopies = 1;
    private String mPower;
    private String mSetName;
    private String mText;
    private String mToughness;
    private String mType;

    public MTGCardModel(){
        mColors = new ArrayList<>();
    }

    public MTGCardModel(Card card){
        mArtist = card.getArtist();
        mCmc = card.getCmc();
        mFlavorText = card.getFlavor();
        mImageUrl = card.getImageUrl();
        mLoyalty = card.getLoyalty();
        mManaCost = card.getManaCost();
        mMultiverseId = card.getMultiverseid();
        mName = card.getName();
        mPower = card.getPower();
        mSetName = card.getSetName();
        mText = card.getText();
        mToughness = card.getToughness();
        mType = card.getType();

        mColors = new ArrayList<>();
        if(card.getColors() != null) {
            for (String color : card.getColors()) {
                mColors.add(color);
            }
        }
    }

    @Exclude
    public String getFirebaseKey(){
        return mFirebaseKey;
    }

    @Exclude
    public void setFirebaseKey(String firebaseKey){
        mFirebaseKey = firebaseKey;
    }

    public String getArtist(){
        return mArtist;
    }

    public void setArtist(String artist){
        mArtist = artist;
    }

    public double getCmc(){
        return mCmc;
    }

    public void setCmc(double cmc){
        mCmc = cmc;
    }

    public List<String> getColors(){
        return mColors;
    }

    public void setColors(List<String> colors){
        mColors = colors;
    }

    public String getFlavorText(){
        return mFlavorText;
    }

    public void setFlavorText(String flavorText){
        mFlavorText = flavorText;
    }

    public String getImageUrl(){
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl){
        mImageUrl = imageUrl;
    }

    public int getLoyalty(){
        return mLoyalty;
    }

    public void setLoyalty(int loyalty){
        mLoyalty = loyalty;
    }

    public String getManaCost(){
        return mManaCost;
    }

    public void setManaCost(String manaCost){
        mManaCost = manaCost;
    }

    public int getMultiverseId(){
        return mMultiverseId;
    }

    public void setMultiverseId(int multiverseId){
        mMultiverseId = multiverseId;
    }

    public String getName(){
        return mName;
    }

    public void setName(String name){
        mName = name;
    }

    public int getNumbCopies() {
        return mNumbCopies;
    }

    public void setNumbCopies(int numbCopies){
        mNumbCopies = numbCopies;
    }

    public String getPower(){
        return mPower;
    }

    public void setPower(String power){
        mPower = power;
    }

    public String getSetName(){
        return mSetName;
    }

    public void setSetName(String setName){
        mSetName = setName;
    }

    public String getText(){
        return mText;
    }

    public void setText(String text){
        mText = text;
    }

    public String getToughness(){
        return mToughness;
    }

    public void setToughness(String toughness){
        mToughness = toughness;
    }

    public String getType(){
        return mType;
    }

    public void setType(String type){
        mType = type;
    }

}
