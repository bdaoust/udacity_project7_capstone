package org.bdaoust.project7capstone.network;


import android.content.res.Resources;
import android.util.Log;

import org.bdaoust.project7capstone.R;
import org.bdaoust.project7capstone.firebasemodels.MTGCardModel;
import org.bdaoust.project7capstone.firebasemodels.MTGDeckModel;

import java.util.ArrayList;
import java.util.List;

import io.magicthegathering.javasdk.api.CardAPI;
import io.magicthegathering.javasdk.exception.HttpRequestFailedException;
import io.magicthegathering.javasdk.resource.Card;

public class MTGSampleDeckFetcher {

    private Resources mResources;
    private List<MTGCardModel> mDeckCards;
    private int mNumbCardsRequested = 0;
    private boolean mIsDownloadSuccessful;
    private final static String TAG = "MTGSampleDeckFetcher";

    public MTGSampleDeckFetcher(Resources resources) {
        mResources = resources;
    }

    public MTGDeckModel fetch() {
        MTGDeckModel mtgDeck;

        mtgDeck = new MTGDeckModel();
        mDeckCards = new ArrayList<>();
        mIsDownloadSuccessful = false;

        downloadCard(191401, 12); // 12 x Mountain
        downloadCard(191089, 4); // 4 x Lightning Bolt
        downloadCard(220948, 4); // 4 x Urza's Mine
        downloadCard(220952, 4); // 4 x Urza's Power Plant
        downloadCard(220956, 4); // 4 x Urza's Tower
        downloadCard(134751, 4); // 4 x Incinerate
        downloadCard(191076, 4); // 4 x Fireball
        downloadCard(1824, 1); // 1 x Maze of Ith
        downloadCard(3452, 2); // 2 x Hammer of Bogardan
        downloadCard(401805, 3); // 3 x Akoum Hellkite
        downloadCard(126193, 4); // 4 x Arc Blade
        downloadCard(205386, 3); // 3 x Arc Lightning
        downloadCard(186613, 4); // 4 x Banefire
        downloadCard(114909, 2); // 2 x Conflagrate
        downloadCard(262661, 2); // 2 x Increasing Vengeance
        downloadCard(420882, 3); // 3 x Nevinyrral's Disk
        downloadCard(259205, 2); // 2 x Chandra, the Firebrand
        downloadCard(195402, 1); // 1 x Chandra Ablaze
        downloadCard(393821, 1);// 1 x Chandra Nalaar
        downloadCard(417683, 1); // 1 x Chandra, Torch of Defiance

        if (mDeckCards.size() == mNumbCardsRequested) {
            mIsDownloadSuccessful = true;
        }

        mtgDeck.setName(mResources.getString(R.string.sample_deck_name));
        mtgDeck.setLastUpdatedTimestamp(System.currentTimeMillis());
        mtgDeck.setMTGCards(mDeckCards);

        return mtgDeck;
    }

    public boolean isDownloadSuccessful() {
        return mIsDownloadSuccessful;
    }

    private void downloadCard(int multiverseId, int numbCopies) {
        int numbRetries = 2;
        boolean cardDownloaded = false;

        if (mDeckCards.size() < mNumbCardsRequested) {
            return;
        }

        mNumbCardsRequested++;

        while (!cardDownloaded && numbRetries >= 0) {
            Log.d(TAG, "Attempting to download card (" + multiverseId + ")");
            try {
                Card card;
                MTGCardModel mtgCardModel;

                card = CardAPI.getCard(multiverseId);
                mtgCardModel = new MTGCardModel(card);
                mtgCardModel.setNumbCopies(numbCopies);
                mDeckCards.add(mtgCardModel);
                cardDownloaded = true;
            } catch (HttpRequestFailedException e) {

                Log.d(TAG, "Failed to download card ("
                        + multiverseId + ") -> "
                        + numbRetries + " retries left. :" + e.getMessage());
                numbRetries--;
            }
        }
    }
}
