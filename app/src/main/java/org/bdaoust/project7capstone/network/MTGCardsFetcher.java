package org.bdaoust.project7capstone.network;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import io.magicthegathering.javasdk.api.CardAPI;
import io.magicthegathering.javasdk.resource.Card;

public class MTGCardsFetcher {

    private String mSets;
    private Comparator<List<Card>> mCardsListComparator;

    public MTGCardsFetcher() {
        mSets = "";

        //This card list comparator is only used to compare lists of cards where all cards in the
        // first list have the same name, and all cards in the second list have the same name.
        mCardsListComparator = new Comparator<List<Card>>() {
            @Override
            public int compare(List<Card> cardsList1, List<Card> cardsList2) {
                String list1CardName;
                String list2CardName;

                list1CardName = cardsList1.get(0).getName();
                list2CardName = cardsList2.get(0).getName();

                return list1CardName.compareTo(list2CardName);
            }
        };
    }

    public void setSets(String sets){
        mSets = sets;
    }

    public List<List<Card>> fetch(String searchTerm) {
        List<String> queryOptions;
        List<Card> cards;
        List<List<Card>> cardsLists;
        HashMap<String, List<Card>> cardsMap;
        String key;


        cardsMap = new HashMap<>();

        // First we search for cards where the name matches exactly the searchTerm.
        // Note: It is important to do a search for the exact name, since otherwise the
        // card might not show up in the "wildcard" search. For example a "wildcard" search
        // for "ow" might not find the card "Ow" given that there are so many other cards that
        // contain "ow" ("All Hallow's Eve", "Brown Ouphe", "Burrowing", "City of Shadows", etc.)
        // and we want to make sure that we always "find" cards that match the searchTerm exactly.
        queryOptions = generateQueryOptions(searchTerm, true);
        cards = CardAPI.getAllCards(queryOptions);

        if (cards.size() > 0) {
            key = searchTerm.toLowerCase();
            cardsMap.put(key, cards);
        }

        // Now we search for cards where the name contains the searchTerm but it doesn't need
        // to be an exact match
        queryOptions = generateQueryOptions(searchTerm, false);
        cards = CardAPI.getAllCards(queryOptions);

        for (Card card : cards) {
            key = card.getName().toLowerCase();

            // Ignore cards where the name matches the searchTerm exactly, since
            // those cards have already been added.
            if (key.equals(searchTerm.toLowerCase())) {
                continue;
            }
            if (!cardsMap.containsKey(key)) {
                cardsMap.put(key, new ArrayList<Card>());
            }
            cardsMap.get(key).add(card);
        }


        cardsLists = new ArrayList<>(cardsMap.values());
        Collections.sort(cardsLists, mCardsListComparator);

        return cardsLists;
    }


    private List<String> generateQueryOptions(String searchTerm, boolean findExactMatch) {
        List<String> queryOptions;

        queryOptions = new ArrayList<>();

        if (findExactMatch) {
            queryOptions.add("name=\"" + searchTerm + "\"");
        } else {
            queryOptions.add("name=" + searchTerm);
        }

        queryOptions.add("page=0");
        queryOptions.add("pageSize=100");
        queryOptions.add("contains=multiverseid");
        queryOptions.add("layout=aftermath|double-faced|flip|leveler|normal|split");
        if (!mSets.equals("")) {
            queryOptions.add("set=" + mSets);
        }

        return queryOptions;
    }
}
