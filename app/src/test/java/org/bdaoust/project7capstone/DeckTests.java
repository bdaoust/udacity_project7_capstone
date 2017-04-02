package org.bdaoust.project7capstone;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import io.magicthegathering.javasdk.api.CardAPI;
import io.magicthegathering.javasdk.resource.Card;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DeckTests {

    private String mDeckName;
    private Deck mDeck;

    @Before
    public void setup(){
        mDeckName = "Test Deck Name";
    }

    @Test
    public void testConstructor(){
        mDeck = new Deck(mDeckName);
        assertEquals(mDeckName, mDeck.getDeckName());
        assertTrue(mDeck.getCards().isEmpty());
    }

    @Test
    public void testGetSetDeckName(){
        String testDeckName;

        testDeckName = "Test Deck Name 2";
        mDeck = new Deck(mDeckName);
        mDeck.setDeckName(testDeckName);
        assertEquals(testDeckName, mDeck.getDeckName());
    }

    @Test
    public void testGetSetLastUpdatedTimestamp(){
        long lastUpdatedTimestamp;

        lastUpdatedTimestamp = 123456;
        mDeck = new Deck(mDeckName);
        mDeck.setLastUpdatedTimestamp(lastUpdatedTimestamp);
        assertEquals(123456, mDeck.getLastUpdatedTimestamp());
    }

    @Test
    public void testAddCardCopies(){
        int multiverseId;
        Card card1;
        Card card2;
        Card card3;
        ArrayList<Card> cards;

        mDeck = new Deck(mDeckName);
        cards = mDeck.getCards();
        assertEquals(0, cards.size());

        multiverseId = 191089; //Lightning Bolt (Magic 2010)
        card1 = CardAPI.getCard(multiverseId);
        mDeck.addCardCopies(card1, 1);
        cards = mDeck.getCards();
        //Check all cards are in the collection and sorted alphabetically
        assertEquals(1, cards.size());
        assertEquals(card1, cards.get(0));
        //Check that the Deck has the right number of copies for each card.
        assertEquals(1, mDeck.getNumbCopies(191089));

        multiverseId = 191076; //Fireball (Magic 2010)
        card2 = CardAPI.getCard(multiverseId);
        mDeck.addCardCopies(card2, 2);
        cards = mDeck.getCards();
        //Check all cards are in the collection and sorted alphabetically
        assertEquals(2, cards.size());
        assertEquals(card2, cards.get(0));
        assertEquals(card1, cards.get(1));
        //Check that the Deck has the right number of copies for each card.
        assertEquals(1, mDeck.getNumbCopies(191089));
        assertEquals(2, mDeck.getNumbCopies(191076));

        multiverseId = 3263; //Phyrexian Dreadnought (Mirage)
        card3 = CardAPI.getCard(multiverseId);
        mDeck.addCardCopies(card3, 3);
        cards = mDeck.getCards();
        //Check all cards are in the collection and sorted alphabetically
        assertEquals(3, cards.size());
        assertEquals(card2, cards.get(0));
        assertEquals(card1, cards.get(1));
        assertEquals(card3, cards.get(2));
        //Check that the Deck has the right number of copies for each card.
        assertEquals(1, mDeck.getNumbCopies(191089));
        assertEquals(2, mDeck.getNumbCopies(191076));
        assertEquals(3, mDeck.getNumbCopies(3263));

        //Add more copies of Fireball (Magic 2010)
        mDeck.addCardCopies(card2, 7);
        cards = mDeck.getCards();
        //Check all cards are in the collection and sorted alphabetically
        assertEquals(3, cards.size());
        assertEquals(card2, cards.get(0));
        assertEquals(card1, cards.get(1));
        assertEquals(card3, cards.get(2));
        //Check that the Deck has the right number of copies for each card.
        assertEquals(1, mDeck.getNumbCopies(191089));
        assertEquals(9, mDeck.getNumbCopies(191076));
        assertEquals(3, mDeck.getNumbCopies(3263));

        //Test adding a negative number of cards
        try{
            mDeck.addCardCopies(card1, -5);
            fail("An IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException ignored){

        }
    }

    @Test
    public void testRemoveCardCopies(){
        int multiverseId;
        Card card1;
        Card card2;
        Card card3;
        ArrayList<Card> cards;

        mDeck = new Deck(mDeckName);

        multiverseId = 191089; //Lightning Bolt (Magic 2010)
        card1 = CardAPI.getCard(multiverseId);
        mDeck.addCardCopies(card1, 6);

        multiverseId = 191076; //Fireball (Magic 2010)
        card2 = CardAPI.getCard(multiverseId);
        mDeck.addCardCopies(card2, 3);

        multiverseId = 3263; //Phyrexian Dreadnought (Mirage)
        card3 = CardAPI.getCard(multiverseId);
        mDeck.addCardCopies(card3, 25);

        mDeck.removeCardCopies(191076); //Removing all copies of Fireball (Magic 2010)
        cards = mDeck.getCards();
        assertEquals(2, cards.size());
        assertEquals(0, mDeck.getNumbCopies(191076));
        assertEquals(card1, cards.get(0));
        assertEquals(card3, cards.get(1));

        mDeck.removeCardCopies(3263); //Removing all copies of Phyrexian Dreadnought (Mirage)
        cards = mDeck.getCards();
        assertEquals(1, cards.size());
        assertEquals(0, mDeck.getNumbCopies(3263));
        assertEquals(card1, cards.get(0));

        //Attempt to remove a Card that is not part of the Deck (which should have no effect)
        mDeck.removeCardCopies(1234);
        cards = mDeck.getCards();
        assertEquals(1, cards.size());
        assertEquals(0, mDeck.getNumbCopies(3263));
        assertEquals(card1, cards.get(0));
    }

    @Test
    public void testSetCardCopies(){
        int multiverseId;
        Card card1;
        Card card2;
        Card card3;

        mDeck = new Deck(mDeckName);

        multiverseId = 191089; //Lightning Bolt (Magic 2010)
        card1 = CardAPI.getCard(multiverseId);
        mDeck.addCardCopies(card1, 5);

        multiverseId = 191076; //Fireball (Magic 2010)
        card2 = CardAPI.getCard(multiverseId);
        mDeck.addCardCopies(card2, 3);

        multiverseId = 3263; //Phyrexian Dreadnought (Mirage)
        card3 = CardAPI.getCard(multiverseId);

        assertEquals(5, mDeck.getNumbCopies(191089));
        assertEquals(3, mDeck.getNumbCopies(191076));

        mDeck.setCardCopies(card1, 23);
        mDeck.setCardCopies(card2, 13);

        assertEquals(23, mDeck.getNumbCopies(191089));
        assertEquals(13, mDeck.getNumbCopies(191076));
        assertEquals(2, mDeck.getCards().size());

        //Setting the number of copies to 0 should remove the Card from the Deck
        mDeck.setCardCopies(card1, 0);
        assertEquals(0, mDeck.getNumbCopies(191089));
        assertEquals(13, mDeck.getNumbCopies(191076));
        assertEquals(1, mDeck.getCards().size());

        //If the Card isn't part of the Deck it should be added
        mDeck.setCardCopies(card3, 5);
        assertEquals(13, mDeck.getNumbCopies(191076));
        assertEquals(5, mDeck.getNumbCopies(3263));
        assertEquals(2, mDeck.getCards().size());

        //Test setting a negative number of cards
        try{
            mDeck.setCardCopies(card3, -1);
            fail("An IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException ignored){

        }
    }

    @Test
    public void testGetNumbCards(){
        mDeck = new Deck(mDeckName);

        assertEquals(0, mDeck.getNumbCards());

        int multiverseId;
        Card card1;
        Card card2;
        Card card3;

        mDeck = new Deck(mDeckName);

        multiverseId = 191089; //Lightning Bolt (Magic 2010)
        card1 = CardAPI.getCard(multiverseId);
        mDeck.addCardCopies(card1, 17);
        assertEquals(17, mDeck.getNumbCards());

        multiverseId = 191076; //Fireball (Magic 2010)
        card2 = CardAPI.getCard(multiverseId);
        mDeck.addCardCopies(card2, 5);
        assertEquals(22, mDeck.getNumbCards());

        multiverseId = 3263; //Phyrexian Dreadnought (Mirage)
        card3 = CardAPI.getCard(multiverseId);
        mDeck.addCardCopies(card3, 7);
        assertEquals(29, mDeck.getNumbCards());

        //Change the number of Phyrexian Dreadnought (Mirage) copies
        mDeck.setCardCopies(card3, 5);
        assertEquals(27, mDeck.getNumbCards());
    }
}
