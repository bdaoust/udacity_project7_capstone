package org.bdaoust.project7capstone;

import android.support.test.runner.AndroidJUnit4;
import android.support.v4.util.ArraySet;

import org.bdaoust.project7capstone.network.SearchForCardsTask;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Set;

import io.magicthegathering.javasdk.resource.Card;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class SearchForCardsTaskTests {

    private List<List<Card>> mCardsLists;
    private boolean mRequestCompleted;
    private boolean mRequestFailed;
    private static final long TIMEOUT = 10000;
    private long mTestStartedTimestamp;

    // This is a list of all sets returned from a query to the MTG API (https://api.magicthegathering.io/v1/sets) as of May 24th 2017.
    // The list of sets is explicitly defined to reduce the chance of our tests failing in the future when new sets are added.
    private static final String LIST_OF_SETS = "LEA|LEB|ARN|2ED|CED|CEI|pDRC|ATQ|3ED|LEG|DRK|FEM|pMEI|pLGM|4ED|ICE|CHR|HML|ALL|RQS|pARL|pCEL|MIR|MGB|ITP|VIS|5ED|pPOD|POR|VAN|WTH|pPRE|TMP|STH|PO2|pJGP|EXO|UGL|pALP|USG|ATH|ULG|6ED|PTK|UDS|S99|pGRU|pWOR|pWOS|MMQ|BRB|pSUS|pFNM|pELP|NMS|S00|PCY|BTD|INV|PLS|7ED|pMPR|APC|ODY|DKM|TOR|JUD|ONS|LGN|SCG|pREL|8ED|MRD|DST|5DN|CHK|UNH|BOK|SOK|9ED|RAV|p2HG|pGTW|GPT|pCMP|DIS|CSP|CST|V11|TSP|TSB|pHHO|PLC|pPRO|pGPX|FUT|10E|pMGD|MED|LRW|EVG|pLPA|MOR|p15A|DDH|SHM|pSUM|EVE|DRB|ME2|pWPN|ALA|DD2|CON|DDC|ARB|M10|V09|HOP|ME3|ZEN|DDD|H09|WWK|DDE|ROE|DPA|ARC|M11|V10|DDF|SOM|PD2|ME4|MBS|DDG|NPH|CMD|M12|ISD|PD3|DKA|DDI|AVR|PC2|M13|V12|DDJ|RTR|CM1|GTC|DDK|pWCQ|DGM|MMA|M14|V13|DDL|THS|C13|BNG|DDM|JOU|MD1|CNS|VMA|M15|CPK|V14|DDN|C15|KTK|C14|DD3_DVD|DD3_EVG|DD3_GVL|DD3_JVC|FRF_UGIN|FRF|DDO|DTK|TPR|MM2|ORI|V15|DDP|BFZ|EXP|OGW|DDQ|W16|SOI|EMA|EMN|V16|CN2|DDR|KLD|MPS|C16|PCA|AER|MM3|DDS|AKH|MPS_AKH";

    @Before
    public void setup(){
        mTestStartedTimestamp = System.currentTimeMillis();
    }

    @Test
    public void testSearchFor_NonExistentCard() throws InterruptedException {
        SearchForCardsTask searchForCardsTask;

        searchForCardsTask = createSearchForCardsTask("xyzthiscarddoesnotexist");
        searchForCardsTask.execute();

        waitForTaskToComplete();

        assertEquals(true, mRequestCompleted);
        assertEquals(0, mCardsLists.size());
    }

    @Test
    public void testSearchFor_Lhurgoyf() throws InterruptedException {
        SearchForCardsTask searchForCardsTask;
        List<Card> mCards;
        Set<Integer> multiverseIds;

        searchForCardsTask = createSearchForCardsTask("Lhurgoyf");
        searchForCardsTask.setSets(LIST_OF_SETS);
        searchForCardsTask.execute();

        waitForTaskToComplete();
        multiverseIds = new ArraySet<>();

        assertEquals(true, mRequestCompleted);
        assertEquals(1, mCardsLists.size());

        mCards = mCardsLists.get(0);


        for(int i=0; i< mCards.size(); i++){
            Card card;

            card = mCards.get(i);
            multiverseIds.add(card.getMultiverseid());
        }

        assertEquals(5, mCards.size());
        assertTrue(multiverseIds.contains(2576)); // Lhurgoyf (Ice Age)
        assertTrue(multiverseIds.contains(3992)); // Lhurgoyf (Fifth Edition)
        assertTrue(multiverseIds.contains(11380)); // Lhurgoyf (Battle Royale Box Set)
        assertTrue(multiverseIds.contains(46622)); // Lhurgoyf (Eighth Edition)
        assertTrue(multiverseIds.contains(247151)); // Lhurgoyf (Magic: The Gathering-Commander)
    }

    @Test
    public void testSearchFor_chandra() throws InterruptedException {
        SearchForCardsTask searchForCardsTask;
        List<Card> mCards;
        Set<Integer> multiverseIds;

        searchForCardsTask = createSearchForCardsTask("chandra");
        searchForCardsTask.setSets(LIST_OF_SETS);
        searchForCardsTask.execute();

        waitForTaskToComplete();

        assertEquals(true, mRequestCompleted);
        assertEquals(17, mCardsLists.size());

        // Chandra Ablaze
        mCards = mCardsLists.get(0);
        assertEquals(1, mCards.size());
        assertEquals(195402, mCards.get(0).getMultiverseid()); // Chandra Ablaze (Zendikar)

        // Chandra Nalaar
        mCards = mCardsLists.get(1);
        multiverseIds = new ArraySet<>();

        for(int i=0; i< mCards.size(); i++){
            Card card;

            card = mCards.get(i);
            multiverseIds.add(card.getMultiverseid());
        }

        assertEquals(5, mCards.size());
        assertTrue(multiverseIds.contains(140176)); // Chandra Nalaar (Lorwyn)
        assertTrue(multiverseIds.contains(185815)); // Chandra Nalaar (Duel Decks: Jace vs. Chandra)
        assertTrue(multiverseIds.contains(191242)); // Chandra Nalaar (Magic 2010)
        assertTrue(multiverseIds.contains(205958)); // Chandra Nalaar (Magic 2011)
        assertTrue(multiverseIds.contains(393821)); // Chandra Nalaar (Duel Decks Anthology, Jace vs. Chandra)

        // Chandra's Fury
        mCards = mCardsLists.get(2);
        multiverseIds = new ArraySet<>();

        for(int i=0; i< mCards.size(); i++){
            Card card;

            card = mCards.get(i);
            multiverseIds.add(card.getMultiverseid());
        }

        assertEquals(2, mCards.size());
        assertTrue(multiverseIds.contains(249682)); // Chandra's Fury (Magic 2013)
        assertTrue(multiverseIds.contains(398632)); // Chandra's Fury (Magic Origins)

        // Chandra's Ignition
        mCards = mCardsLists.get(3);
        assertEquals(1, mCards.size());
        assertEquals(398416, mCards.get(0).getMultiverseid()); // Chandra's Ignition (Magic Origins)

        // Chandra's Outrage
        mCards = mCardsLists.get(4);
        multiverseIds = new ArraySet<>();

        for(int i=0; i< mCards.size(); i++){
            Card card;

            card = mCards.get(i);
            multiverseIds.add(card.getMultiverseid());
        }

        assertEquals(5, mCards.size());
        assertTrue(multiverseIds.contains(205071)); // Chandra's Outrage (Magic 2011)
        assertTrue(multiverseIds.contains(220513)); // Chandra's Outrage (Archenemy)
        assertTrue(multiverseIds.contains(226585)); // Chandra's Outrage (Magic 2012)
        assertTrue(multiverseIds.contains(370659)); // Chandra's Outrage (Magic 2014 Core Set)
        assertTrue(multiverseIds.contains(425917)); // Chandra's Outrage (Modern Masters 2017 Edition)

        // Chandra's Phoenix
        mCards = mCardsLists.get(5);
        multiverseIds = new ArraySet<>();

        for(int i=0; i< mCards.size(); i++){
            Card card;

            card = mCards.get(i);
            multiverseIds.add(card.getMultiverseid());
        }

        assertEquals(2, mCards.size());
        assertTrue(multiverseIds.contains(220298)); // Chandra's Phoenix (Magic 2012)
        assertTrue(multiverseIds.contains(370691)); // Chandra's Phoenix (Magic 2014 Core Set)

        //Chandra's Pyrohelix
        mCards = mCardsLists.get(6);
        assertEquals(1, mCards.size());
        assertEquals(417684, mCards.get(0).getMultiverseid()); // Chandra's Pyrohelix (Kaladesh)

        // Chandra's Revolution
        mCards = mCardsLists.get(7);
        assertEquals(1, mCards.size());
        assertEquals(423744, mCards.get(0).getMultiverseid()); // Chandra's Revolution (Aether Revolt)


        // Chandra's Spitfire
        mCards = mCardsLists.get(8);
        assertEquals(1, mCards.size());
        assertEquals(205026, mCards.get(0).getMultiverseid()); // Chandra's Spitfire (Magic 2011)

        // Chandra, Fire of Kaladesh
        mCards = mCardsLists.get(9);
        assertEquals(1, mCards.size());
        assertEquals(398422, mCards.get(0).getMultiverseid()); // Chandra, Fire of Kaladesh (Magic Origins)

        // Chandra, Flamecaller
        mCards = mCardsLists.get(10);
        assertEquals(1, mCards.size());
        assertEquals(407614, mCards.get(0).getMultiverseid()); // Chandra, Flamecaller (Oath of the Gatewatch)

        // Chandra, Pyrogenius
        mCards = mCardsLists.get(11);
        assertEquals(1, mCards.size());
        assertEquals(420478, mCards.get(0).getMultiverseid()); // Chandra, Pyrogenius (Kaladesh)

        // Chandra, Pyromaster
        mCards = mCardsLists.get(12);
        multiverseIds = new ArraySet<>();

        for(int i=0; i< mCards.size(); i++){
            Card card;

            card = mCards.get(i);
            multiverseIds.add(card.getMultiverseid());
        }

        assertEquals(2, mCards.size());
        assertTrue(multiverseIds.contains(370637)); // Chandra, Pyromaster (Magic 2014 Core Set)
        assertTrue(multiverseIds.contains(383204)); // Chandra, Pyromaster (Magic 2015 Core Set)

        // Chandra, Roaring Flame
        mCards = mCardsLists.get(13);
        assertEquals(1, mCards.size());
        assertEquals(398423, mCards.get(0).getMultiverseid()); // Chandra, Roaring Flame (Magic Origins)

        // Chandra, Torch of Defiance
        mCards = mCardsLists.get(14);
        assertEquals(1, mCards.size());
        assertEquals(417683, mCards.get(0).getMultiverseid()); // Chandra, Torch of Defiance (Kaladesh)

        // Chandra, the Firebrand
        mCards = mCardsLists.get(15);
        multiverseIds = new ArraySet<>();

        for(int i=0; i< mCards.size(); i++){
            Card card;

            card = mCards.get(i);
            multiverseIds.add(card.getMultiverseid());
        }

        assertEquals(2, mCards.size());
        assertTrue(multiverseIds.contains(220146)); // Chandra, the Firebrand (Magic 2012)
        assertTrue(multiverseIds.contains(259205)); // Chandra, the Firebrand (Magic 2013)

        // Oath of Chandra
        mCards = mCardsLists.get(16);
        assertEquals(1, mCards.size());
        assertEquals(407623, mCards.get(0).getMultiverseid()); // Oath of Chandra
    }


    private SearchForCardsTask createSearchForCardsTask(String searchTerm) {
        SearchForCardsTask searchForCardsTask;
        searchForCardsTask = new SearchForCardsTask(searchTerm, 0);

        searchForCardsTask.setOnSearchCompletedListener(new SearchForCardsTask.OnSearchCompletedListener() {
            @Override
            public void onSearchCompleted(List<List<Card>> cardsLists, String searchTerm, long searchRequestTimestamp) {
                mCardsLists = cardsLists;
                mRequestCompleted = true;
            }
        });
        searchForCardsTask.setOnRequestFailedListener(new SearchForCardsTask.OnRequestFailedListener() {
            @Override
            public void onRequestFailed() {
                mRequestFailed = true;
            }
        });

        return searchForCardsTask;
    }

    private void waitForTaskToComplete() throws InterruptedException {
        long timeSpent = 0;

        while(!mRequestCompleted && !mRequestFailed && (timeSpent < TIMEOUT)){
            timeSpent = System.currentTimeMillis() - mTestStartedTimestamp;
            Thread.sleep(20);
        }

        if(mRequestFailed) {
            fail("Test failed due to network failure");
        }

        if(timeSpent >= TIMEOUT){
            fail("Test failed due to timeout");
        }
    }
}
