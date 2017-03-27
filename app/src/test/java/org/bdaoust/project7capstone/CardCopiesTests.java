package org.bdaoust.project7capstone;

import org.junit.Before;
import org.junit.Test;

import io.magicthegathering.javasdk.resource.Card;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class CardCopiesTests {

    private Card mCard;

    @Before
    public void setup(){
        mCard = new Card();
    }

    @Test
    public void testConstructors(){
        CardCopies cardCopies;

        cardCopies = new CardCopies(mCard);
        assertEquals(mCard, cardCopies.getCard());
        assertEquals(1, cardCopies.getNumbCopies());

        cardCopies = new CardCopies(mCard, 4);
        assertEquals(mCard, cardCopies.getCard());
        assertEquals(4, cardCopies.getNumbCopies());
    }

    @Test
    public void testConstructorsWithIllegalArguments(){

        try {
            new CardCopies(mCard, 0);
            fail("An IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e){
        }

        try {
            new CardCopies(mCard, -1);
            fail("An IllegalArgumentException should have been thrown.");

        } catch (IllegalArgumentException e){
        }
    }

    @Test
    public void testGetSetNumbCopies(){
        CardCopies cardCopies;

        cardCopies = new CardCopies(mCard);
        cardCopies.setNumbCopies(3);
        assertEquals(3, cardCopies.getNumbCopies());
    }

    @Test
    public void testSetNumbCopiesWithIllegalArguments(){
        CardCopies cardCopies;

        cardCopies = new CardCopies(mCard);
        try {
            cardCopies.setNumbCopies(0);
            fail("An IllegalArgumentException should have been thrown.");
        } catch (Exception e){
        }

        try {
            cardCopies.setNumbCopies(-1);
            fail("An IllegalArgumentException should have been thrown.");
        } catch (Exception e){
        }
    }
}