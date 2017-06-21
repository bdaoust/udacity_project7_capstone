package org.bdaoust.project7capstone.network;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.bdaoust.project7capstone.firebasemodels.MTGDeckModel;
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

        mtgSampleDeckFetcher = new MTGSampleDeckFetcher(getResources());
        mtgDeck = mtgSampleDeckFetcher.fetch();

        if(mtgSampleDeckFetcher.isDownloadSuccessful()) {
            firebaseDatabase = FirebaseDatabase.getInstance();
            referenceUserRoot = MTGTools.createUserRootReference(firebaseDatabase, null);
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
