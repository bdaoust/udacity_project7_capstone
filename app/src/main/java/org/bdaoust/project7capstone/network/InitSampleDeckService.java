package org.bdaoust.project7capstone.network;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.bdaoust.project7capstone.firebasemodels.MTGCardModel;
import org.bdaoust.project7capstone.firebasemodels.MTGDeckModel;

import java.util.ArrayList;
import java.util.List;

import io.magicthegathering.javasdk.api.CardAPI;
import io.magicthegathering.javasdk.resource.Card;

public class InitSampleDeckService extends IntentService{

    private static final String TAG = "InitSampleDeckService";

    public InitSampleDeckService(){
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        FirebaseDatabase firebaseDatabase;
        DatabaseReference referenceRoot;
        DatabaseReference referenceDecks;
        DatabaseReference referenceSampleDeckWasSaved;
        MTGDeckModel mtgDeckModel;
        MTGCardModel mtgCardModel;
        List<MTGCardModel> deckCards;
        Card card;

        mtgDeckModel = new MTGDeckModel();
        deckCards = new ArrayList<>();

        // 12 x Mountain
        card = CardAPI.getCard(191401);
        mtgCardModel = new MTGCardModel(card);
        mtgCardModel.setNumbCopies(12);
        deckCards.add(mtgCardModel);

        // 4 x Lightning Bolt
        card = CardAPI.getCard(191089);
        mtgCardModel = new MTGCardModel(card);
        mtgCardModel.setNumbCopies(4);
        deckCards.add(mtgCardModel);

        // 4 x Urza's Mine
        card = CardAPI.getCard(220948);
        mtgCardModel = new MTGCardModel(card);
        mtgCardModel.setNumbCopies(4);
        deckCards.add(mtgCardModel);

        // 4 x Urza's Power Plant
        card = CardAPI.getCard(220952);
        mtgCardModel = new MTGCardModel(card);
        mtgCardModel.setNumbCopies(4);
        deckCards.add(mtgCardModel);

        // 4 x Urza's Tower
        card = CardAPI.getCard(220956);
        mtgCardModel = new MTGCardModel(card);
        mtgCardModel.setNumbCopies(4);
        deckCards.add(mtgCardModel);

        // 4 x Incinerate
        card = CardAPI.getCard(134751);
        mtgCardModel = new MTGCardModel(card);
        mtgCardModel.setNumbCopies(4);
        deckCards.add(mtgCardModel);

        // 4 x Fireball
        card = CardAPI.getCard(191076);
        mtgCardModel = new MTGCardModel(card);
        mtgCardModel.setNumbCopies(4);
        deckCards.add(mtgCardModel);

        // 1 x Maze of Ith
        card = CardAPI.getCard(1824);
        mtgCardModel = new MTGCardModel(card);
        mtgCardModel.setNumbCopies(1);
        deckCards.add(mtgCardModel);

        // 2 x Hammer of Bogardan
        card = CardAPI.getCard(3452);
        mtgCardModel = new MTGCardModel(card);
        mtgCardModel.setNumbCopies(2);
        deckCards.add(mtgCardModel);

        // 3 x Akoum Hellkite
        card = CardAPI.getCard(401805);
        mtgCardModel = new MTGCardModel(card);
        mtgCardModel.setNumbCopies(3);
        deckCards.add(mtgCardModel);

        // 4 x Arc Blade
        card = CardAPI.getCard(126193);
        mtgCardModel = new MTGCardModel(card);
        mtgCardModel.setNumbCopies(4);
        deckCards.add(mtgCardModel);

        // 3 x Arc Lightning
        card = CardAPI.getCard(205386);
        mtgCardModel = new MTGCardModel(card);
        mtgCardModel.setNumbCopies(3);
        deckCards.add(mtgCardModel);

        // 4 x Banefire
        card = CardAPI.getCard(186613);
        mtgCardModel = new MTGCardModel(card);
        mtgCardModel.setNumbCopies(4);
        deckCards.add(mtgCardModel);

        // 2 x Conflagrate
        card = CardAPI.getCard(114909);
        mtgCardModel = new MTGCardModel(card);
        mtgCardModel.setNumbCopies(2);
        deckCards.add(mtgCardModel);

        // 2 x Increasing Vengeance
        card = CardAPI.getCard(262661);
        mtgCardModel = new MTGCardModel(card);
        mtgCardModel.setNumbCopies(2);
        deckCards.add(mtgCardModel);

        // 3 x Nevinyrral's Disk
        card = CardAPI.getCard(420882);
        mtgCardModel = new MTGCardModel(card);
        mtgCardModel.setNumbCopies(3);
        deckCards.add(mtgCardModel);

        // 2 x Chandra, the Firebrand
        card = CardAPI.getCard(259205);
        mtgCardModel = new MTGCardModel(card);
        mtgCardModel.setNumbCopies(2);
        deckCards.add(mtgCardModel);

        // 1 x Chandra Ablaze
        card = CardAPI.getCard(195402);
        mtgCardModel = new MTGCardModel(card);
        mtgCardModel.setNumbCopies(1);
        deckCards.add(mtgCardModel);

        // 1 x Chandra Nalaar
        card = CardAPI.getCard(393821);
        mtgCardModel = new MTGCardModel(card);
        mtgCardModel.setNumbCopies(1);
        deckCards.add(mtgCardModel);

        // 1 x Chandra, Torch of Defiance
        card = CardAPI.getCard(417683);
        mtgCardModel = new MTGCardModel(card);
        mtgCardModel.setNumbCopies(1);
        deckCards.add(mtgCardModel);

        mtgDeckModel.setName("Feel the Burn");
        mtgDeckModel.setLastUpdatedTimestamp(System.currentTimeMillis());
        mtgDeckModel.setMTGCardModels(deckCards);

        firebaseDatabase = FirebaseDatabase.getInstance();
        referenceRoot = firebaseDatabase.getReference();
        referenceDecks = referenceRoot.child("decks");
        referenceSampleDeckWasSaved = referenceRoot.child("sampleDeckWasSaved");

        referenceDecks.push().setValue(mtgDeckModel);
        referenceSampleDeckWasSaved.setValue(true);
    }
}
