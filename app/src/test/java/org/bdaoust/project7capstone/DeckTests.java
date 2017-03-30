package org.bdaoust.project7capstone;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
}
