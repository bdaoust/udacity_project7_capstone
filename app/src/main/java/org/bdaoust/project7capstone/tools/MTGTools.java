package org.bdaoust.project7capstone.tools;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MTGTools {

    private MTGTools(){}

    public static DatabaseReference createUserRootReference(FirebaseDatabase firebaseDatabase, String userId) {
        return firebaseDatabase.getReference().child("users").child(userId);
    }

    public static DatabaseReference createDeckListReference(DatabaseReference userRootReference){
        return userRootReference.child("decks");
    }

    public static DatabaseReference createDeckReference(DatabaseReference userRootReference, String firebaseDeckKey){
        return userRootReference.child("decks").child(firebaseDeckKey);
    }

    public static DatabaseReference createDeckNameReference(DatabaseReference userRootReference, String firebaseDeckKey){
        return userRootReference.child("decks").child(firebaseDeckKey).child("name");
    }

    public static DatabaseReference createDeckLastUpdatedReference(DatabaseReference userRootReference, String firebaseDeckKey){
        return userRootReference.child("decks").child(firebaseDeckKey).child("lastUpdatedTimestamp");
    }

    public static DatabaseReference createCardListReference(DatabaseReference userRootReference, String firebaseDeckKey){
        return userRootReference.child("decks").child(firebaseDeckKey).child("mtgcards");
    }

    public static DatabaseReference createCardReference(DatabaseReference userRootReference, String firebaseDeckKey, String firebaseCardKey){
        return userRootReference.child("decks").child(firebaseDeckKey).child("mtgcards").child(firebaseCardKey);
    }

    public static DatabaseReference createSampleDeckWasSavedReference(DatabaseReference userRootReference){
        return userRootReference.child("sampleDeckWasSaved");
    }

    public static DatabaseReference createTempDeckReference(DatabaseReference userRootReference, String firebaseTempDeckKey){
        return userRootReference.child(firebaseTempDeckKey);
    }

    public static DatabaseReference createTempDeckNameReference(DatabaseReference userRootReference, String firebaseTempDeckKey) {
        return userRootReference.child(firebaseTempDeckKey).child("name");
    }

    public static DatabaseReference createTempDeckCardsReference(DatabaseReference userRootReference, String firebaseTempDeckKey){
        return userRootReference.child(firebaseTempDeckKey).child("mtgcards");
    }

    public static DatabaseReference createTempDeckCardReference(DatabaseReference userRootReference, String firebaseTempDeckKey, String firebaseTempCardKey){
        return userRootReference.child(firebaseTempDeckKey).child("mtgcards").child(firebaseTempCardKey);
    }

    public static DatabaseReference createTempDeckCardNumbCopiesReference(DatabaseReference userRootReference, String firebaseTempDeckKey, String firebaseTempCardKey){
        return userRootReference.child(firebaseTempDeckKey).child("mtgcards").child(firebaseTempCardKey).child("numbCopies");
    }

}
