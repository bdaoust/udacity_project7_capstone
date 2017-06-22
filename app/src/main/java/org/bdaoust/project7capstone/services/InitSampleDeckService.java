package org.bdaoust.project7capstone.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.bdaoust.project7capstone.firebasemodels.MTGDeckModel;
import org.bdaoust.project7capstone.network.MTGSampleDeckFetcher;
import org.bdaoust.project7capstone.tools.MTGKeys;
import org.bdaoust.project7capstone.tools.MTGTools;


public class InitSampleDeckService extends IntentService {

    private static final String TAG = "InitSampleDeckService";

    public InitSampleDeckService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        FirebaseDatabase firebaseDatabase;
        DatabaseReference referenceUserRoot;
        DatabaseReference referenceDecks;
        DatabaseReference referenceSampleDeckWasSaved;
        MTGSampleDeckFetcher mtgSampleDeckFetcher;
        MTGDeckModel mtgDeck;
        String userId;

        userId = "";
        if (intent != null) {
            userId = intent.getStringExtra(MTGKeys.FIREBASE_USER_ID);
        }
        mtgSampleDeckFetcher = new MTGSampleDeckFetcher(getResources());
        mtgDeck = mtgSampleDeckFetcher.fetch();

        if(mtgSampleDeckFetcher.isDownloadSuccessful() && !userId.equals("")) {
            firebaseDatabase = FirebaseDatabase.getInstance();
            referenceUserRoot = MTGTools.createUserRootReference(firebaseDatabase, userId);
            referenceDecks = MTGTools.createDeckListReference(referenceUserRoot);
            referenceSampleDeckWasSaved = MTGTools.createSampleDeckWasSavedReference(referenceUserRoot);

            referenceDecks.push().setValue(mtgDeck);
            referenceSampleDeckWasSaved.setValue(true);
        } else {
            Intent sampleDeckDownloadFailedIntent;

            sampleDeckDownloadFailedIntent = new Intent("org.bdaoust.project7capstone.NOTIFY_SAMPLE_DECK_DOWNLOAD_FAILED");
            getApplicationContext().sendBroadcast(sampleDeckDownloadFailedIntent);

            Log.d(TAG, "Downloading the Sample Deck Failed.... sniff, sniff");
        }
    }

}
