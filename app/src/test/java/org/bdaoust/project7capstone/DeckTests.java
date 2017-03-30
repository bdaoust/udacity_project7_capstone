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

        multiverseId = 1824; //Maze of Ith (The Dark)
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
        assertEquals(3, mDeck.getNumbCopies(1824));

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
        assertEquals(3, mDeck.getNumbCopies(1824));

        //Test adding a negative number of cards
        try{
            mDeck.addCardCopies(card1, -5);
            fail("An IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e){

        }

    }

    @Test
    public void testRemoveCardCopies(){

    }
}
