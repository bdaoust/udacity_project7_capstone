package org.bdaoust.project7capstone;

import org.bdaoust.project7capstone.firebasemodels.SampleDeck;
import org.bdaoust.project7capstone.firebasemodels.MTGCardModel;
import org.bdaoust.project7capstone.firebasemodels.MTGDeckModel;
import org.junit.Before;
import org.junit.Test;

import io.magicthegathering.javasdk.api.CardAPI;
import io.magicthegathering.javasdk.resource.Card;

import static org.junit.Assert.assertEquals;

public class MTGDeckModelTests {

    private MTGDeckModel mMTGDeck;
    private float mDelta = 0.25f;

    @Before
    public void setup(){
        mMTGDeck = new MTGDeckModel();
    }

    @Test
    public void testConstructor(){
        mMTGDeck = new MTGDeckModel();

        assertEquals(null, mMTGDeck.getName());
        assertEquals(null, mMTGDeck.getFirebaseKey());
        assertEquals(0, mMTGDeck.getLastUpdatedTimestamp());
        assertEquals(0, mMTGDeck.getMTGCards().size());
        assertEquals(0, mMTGDeck.getNumbCards());
    }

    @Test
    public void testGetSetName(){
        String testDeckName;

        testDeckName = "Test Deck Name";
        mMTGDeck = new MTGDeckModel();
        mMTGDeck.setName(testDeckName);
        assertEquals(testDeckName, mMTGDeck.getName());
    }

    @Test
    public void testGetSetLastUpdatedTimestamp(){
        long lastUpdatedTimestamp;

        lastUpdatedTimestamp = 123456;
        mMTGDeck.setLastUpdatedTimestamp(lastUpdatedTimestamp);
        assertEquals(123456, mMTGDeck.getLastUpdatedTimestamp());
    }

    @Test
    public void testGetNumbCards(){
        int multiverseId;
        MTGCardModel mtgCard1;
        MTGCardModel mtgCard2;
        MTGCardModel mtgCard3;
        Card card;

        assertEquals(0, mMTGDeck.getNumbCards());

        multiverseId = 191089; //Lightning Bolt (Magic 2010)
        card = CardAPI.getCard(multiverseId);
        mtgCard1 = new MTGCardModel(card);
        mtgCard1.setNumbCopies(17);
        mMTGDeck.addCard(mtgCard1);
        assertEquals(17, mMTGDeck.getNumbCards());

        multiverseId = 191076; //Fireball (Magic 2010)
        card = CardAPI.getCard(multiverseId);
        mtgCard2 = new MTGCardModel(card);
        mtgCard2.setNumbCopies(5);
        mMTGDeck.addCard(mtgCard2);
        assertEquals(22, mMTGDeck.getNumbCards());

        multiverseId = 3263; //Phyrexian Dreadnought (Mirage)
        card = CardAPI.getCard(multiverseId);
        mtgCard3 = new MTGCardModel(card);
        mtgCard3.setNumbCopies(7);
        mMTGDeck.addCard(mtgCard3);
        assertEquals(29, mMTGDeck.getNumbCards());

        //Change the number of Phyrexian Dreadnought (Mirage) copies
        mtgCard3.setNumbCopies(5);
        assertEquals(27, mMTGDeck.getNumbCards());
    }

    @Test
    public void testGetColorPercentages_0Cards(){
        MTGDeckModel.ColorPercentages colorPercentages;

        colorPercentages = mMTGDeck.getColorPercentages();
        assertEquals(0, colorPercentages.black, mDelta);
        assertEquals(0, colorPercentages.blue, mDelta);
        assertEquals(0, colorPercentages.green, mDelta);
        assertEquals(0, colorPercentages.red, mDelta);
        assertEquals(0, colorPercentages.white, mDelta);
    }

    @Test
    public void testGetColorPercentages_1Card(){
        MTGDeckModel.ColorPercentages colorPercentages;
        MTGCardModel mtgCard;
        Card card;
        int multiverseId;

        //**************** Land ********************/
        mMTGDeck = new MTGDeckModel();//Deck(mDeckName);
        multiverseId = 191401; //Mountain (Magic 2010)
        card = CardAPI.getCard(multiverseId);
        mtgCard = new MTGCardModel(card);
        mMTGDeck.addCard(mtgCard);

        colorPercentages = mMTGDeck.getColorPercentages();
        assertEquals(0, colorPercentages.black, mDelta);
        assertEquals(0, colorPercentages.blue, mDelta);
        assertEquals(0, colorPercentages.green, mDelta);
        assertEquals(0, colorPercentages.red, mDelta);
        assertEquals(0, colorPercentages.white, mDelta);

        //**************** Special Land ********************/
        mMTGDeck = new MTGDeckModel();
        multiverseId = 136196; //Dryad Arbor (Future Sight)
        card = CardAPI.getCard(multiverseId);
        mtgCard = new MTGCardModel(card);
        mMTGDeck.addCard(mtgCard);

        colorPercentages = mMTGDeck.getColorPercentages();
        assertEquals(0, colorPercentages.black, mDelta);
        assertEquals(0, colorPercentages.blue, mDelta);
        assertEquals(100, colorPercentages.green, mDelta);
        assertEquals(0, colorPercentages.red, mDelta);
        assertEquals(0, colorPercentages.white, mDelta);

        //**************** Black card ********************/
        mMTGDeck = new MTGDeckModel();
        multiverseId = 190566; //Hypnotic Specter (Magic 2010)
        card = CardAPI.getCard(multiverseId);
        mtgCard = new MTGCardModel(card);
        mMTGDeck.addCard(mtgCard);

        colorPercentages = mMTGDeck.getColorPercentages();
        assertEquals(100, colorPercentages.black, mDelta);
        assertEquals(0, colorPercentages.blue, mDelta);
        assertEquals(0, colorPercentages.green, mDelta);
        assertEquals(0, colorPercentages.red, mDelta);
        assertEquals(0, colorPercentages.white, mDelta);

        //**************** Blue card ********************/
        mMTGDeck = new MTGDeckModel();
        multiverseId = 129633; //Mahamoti Djinn (Tenth Edition)
        card = CardAPI.getCard(multiverseId);
        mtgCard = new MTGCardModel(card);
        mMTGDeck.addCard(mtgCard);

        colorPercentages = mMTGDeck.getColorPercentages();
        assertEquals(0, colorPercentages.black, mDelta);
        assertEquals(100, colorPercentages.blue, mDelta);
        assertEquals(0, colorPercentages.green, mDelta);
        assertEquals(0, colorPercentages.red, mDelta);
        assertEquals(0, colorPercentages.white, mDelta);

        //**************** Green card ********************/
        mMTGDeck = new MTGDeckModel();
        multiverseId = 2576; //Lhurgoyf (Ice Age)
        card = CardAPI.getCard(multiverseId);
        mtgCard = new MTGCardModel(card);
        mMTGDeck.addCard(mtgCard);

        colorPercentages = mMTGDeck.getColorPercentages();
        assertEquals(0, colorPercentages.black, mDelta);
        assertEquals(0, colorPercentages.blue, mDelta);
        assertEquals(100, colorPercentages.green, mDelta);
        assertEquals(0, colorPercentages.red, mDelta);
        assertEquals(0, colorPercentages.white, mDelta);

        //**************** Red card ********************/
        mMTGDeck = new MTGDeckModel();
        multiverseId = 191089; //Lightning Bolt (Magic 2010)
        card = CardAPI.getCard(multiverseId);
        mtgCard = new MTGCardModel(card);
        mMTGDeck.addCard(mtgCard);

        colorPercentages = mMTGDeck.getColorPercentages();
        assertEquals(0, colorPercentages.black, mDelta);
        assertEquals(0, colorPercentages.blue, mDelta);
        assertEquals(0, colorPercentages.green, mDelta);
        assertEquals(100, colorPercentages.red, mDelta);
        assertEquals(0, colorPercentages.white, mDelta);

        //**************** White card ********************/
        mMTGDeck = new MTGDeckModel();
        multiverseId = 382853; //Balance (Vintage Masters)
        card = CardAPI.getCard(multiverseId);
        mtgCard = new MTGCardModel(card);
        mMTGDeck.addCard(mtgCard);

        colorPercentages = mMTGDeck.getColorPercentages();
        assertEquals(0, colorPercentages.black, mDelta);
        assertEquals(0, colorPercentages.blue, mDelta);
        assertEquals(0, colorPercentages.green, mDelta);
        assertEquals(0, colorPercentages.red, mDelta);
        assertEquals(100, colorPercentages.white, mDelta);

        //**************** Green/White card ********************/
        mMTGDeck = new MTGDeckModel();
        multiverseId = 87988; //Selesnya Guildmage (Ravnica: City of Guilds)
        card = CardAPI.getCard(multiverseId);
        mtgCard = new MTGCardModel(card);
        mMTGDeck.addCard(mtgCard);

        colorPercentages = mMTGDeck.getColorPercentages();
        assertEquals(0, colorPercentages.black, mDelta);
        assertEquals(0, colorPercentages.blue, mDelta);
        assertEquals(50, colorPercentages.green, mDelta);
        assertEquals(0, colorPercentages.red, mDelta);
        assertEquals(50, colorPercentages.white, mDelta);

        //**************** 5 color card ********************/
        mMTGDeck = new MTGDeckModel();
        multiverseId = 179496; //Progenitus (Conflux)
        card = CardAPI.getCard(multiverseId);
        mtgCard = new MTGCardModel(card);
        mMTGDeck.addCard(mtgCard);

        colorPercentages = mMTGDeck.getColorPercentages();
        assertEquals(20, colorPercentages.black, mDelta);
        assertEquals(20, colorPercentages.blue, mDelta);
        assertEquals(20, colorPercentages.green, mDelta);
        assertEquals(20, colorPercentages.red, mDelta);
        assertEquals(20, colorPercentages.white, mDelta);
    }

    @Test
    public void testGetColorPercentages_2Cards(){
        MTGDeckModel.ColorPercentages colorPercentages;
        MTGCardModel mtgCard;
        Card card;
        int multiverseId;

        //***************** Two black cards *****************/
        mMTGDeck = new MTGDeckModel();
        multiverseId = 190566; //Hypnotic Specter (Magic 2010)
        card = CardAPI.getCard(multiverseId);
        mtgCard = new MTGCardModel(card);
        mMTGDeck.addCard(mtgCard);
        multiverseId = 129659; //Nightmare (Tenth Edition)
        card = CardAPI.getCard(multiverseId);
        mtgCard = new MTGCardModel(card);
        mMTGDeck.addCard(mtgCard);

        colorPercentages = mMTGDeck.getColorPercentages();
        assertEquals(100, colorPercentages.black, mDelta);
        assertEquals(0, colorPercentages.blue, mDelta);
        assertEquals(0, colorPercentages.green, mDelta);
        assertEquals(0, colorPercentages.red, mDelta);
        assertEquals(0, colorPercentages.white, mDelta);

        //***************** One green card and one green/red card *****************/
        mMTGDeck = new MTGDeckModel();
        multiverseId = 2576; //Lhurgoyf (Ice Age)
        card = CardAPI.getCard(multiverseId);
        mtgCard = new MTGCardModel(card);
        mMTGDeck.addCard(mtgCard);
        multiverseId = 185053; //Bloodbraid Elf (Alara Reborn)
        card = CardAPI.getCard(multiverseId);
        mtgCard = new MTGCardModel(card);
        mMTGDeck.addCard(mtgCard);

        colorPercentages = mMTGDeck.getColorPercentages();
        assertEquals(0, colorPercentages.black, mDelta);
        assertEquals(0, colorPercentages.blue, mDelta);
        assertEquals(75, colorPercentages.green, mDelta);
        assertEquals(25, colorPercentages.red, mDelta);
        assertEquals(0, colorPercentages.white, mDelta);

        //***************** One green/white card and one 5 color card *****************/
        mMTGDeck = new MTGDeckModel();
        multiverseId = 87988; //Selesnya Guildmage (Ravnica: City of Guilds)
        card = CardAPI.getCard(multiverseId);
        mtgCard = new MTGCardModel(card);
        mMTGDeck.addCard(mtgCard);
        multiverseId = 179496; //Progenitus (Conflux)
        card = CardAPI.getCard(multiverseId);
        mtgCard = new MTGCardModel(card);
        mMTGDeck.addCard(mtgCard);

        colorPercentages = mMTGDeck.getColorPercentages();
        assertEquals(10, colorPercentages.black, mDelta);
        assertEquals(10, colorPercentages.blue, mDelta);
        assertEquals(35, colorPercentages.green, mDelta);
        assertEquals(10, colorPercentages.red, mDelta);
        assertEquals(35, colorPercentages.white, mDelta);
    }

    @Test
    public void testGetColorPercentages_3Cards(){
        MTGDeckModel.ColorPercentages colorPercentages;
        MTGCardModel mtgCard;
        Card card;
        int multiverseId;

        //***************** One red/green card, one artifact, and one red/green/white card *****************/
        mMTGDeck = new MTGDeckModel();
        multiverseId = 185053; //Bloodbraid Elf (Alara Reborn)
        card = CardAPI.getCard(multiverseId);
        mtgCard = new MTGCardModel(card);
        mMTGDeck.addCard(mtgCard);
        multiverseId = 420593; //Aether Vial (Masterpiece Series: Kaladesh Inventions)
        card = CardAPI.getCard(multiverseId);
        mtgCard = new MTGCardModel(card);
        mMTGDeck.addCard(mtgCard);
        multiverseId = 180265; //Knotvine Mystic (Conflux)
        card = CardAPI.getCard(multiverseId);
        mtgCard = new MTGCardModel(card);
        mMTGDeck.addCard(mtgCard);

        colorPercentages = mMTGDeck.getColorPercentages();
        assertEquals(0, colorPercentages.black, mDelta);
        assertEquals(0, colorPercentages.blue, mDelta);
        assertEquals(27.78, colorPercentages.green, mDelta);
        assertEquals(27.78, colorPercentages.red, mDelta);
        assertEquals(11.11, colorPercentages.white, mDelta);
    }

    @Test
    public void testGetColorPercentages_SampleDeck(){
        MTGDeckModel.ColorPercentages colorPercentages;

        mMTGDeck = new SampleDeck();

        colorPercentages = mMTGDeck.getColorPercentages();
        assertEquals(0, colorPercentages.black, mDelta);
        assertEquals(0, colorPercentages.blue, mDelta);
        assertEquals(0, colorPercentages.green, mDelta);
        assertEquals(56.92, colorPercentages.red, mDelta);
        assertEquals(0, colorPercentages.white, mDelta);
    }
}
