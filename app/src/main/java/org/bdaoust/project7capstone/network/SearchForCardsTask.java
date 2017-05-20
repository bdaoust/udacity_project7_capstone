package org.bdaoust.project7capstone.network;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import io.magicthegathering.javasdk.api.CardAPI;
import io.magicthegathering.javasdk.resource.Card;

public class SearchForCardsTask extends AsyncTask<Void, Void, List<List<Card>>> {

    private final static long SEARCH_DELAY = 500; //ms
    private String mSearchTerm;
    private OnSearchCompletedListener mOnSearchCompletedListener;
    private Comparator<List<Card>> mCardsListComparator;
    private boolean mIsWaitingToSearch;
    private long mSearchRequestTimestamp;

    public SearchForCardsTask(String searchTerm, long searchRequestTimestamp) {
        mSearchTerm = searchTerm;
        mSearchRequestTimestamp = searchRequestTimestamp;

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

    public String getSearchTerm() {
        return mSearchTerm;
    }

    @Override
    protected List<List<Card>> doInBackground(Void... params) {

        List<String> queryOptions;
        List<Card> cards;
        List<List<Card>> cardsLists;
        HashMap<String, List<Card>> cardsMap;
        String key;

        mIsWaitingToSearch = true;

        while ((System.currentTimeMillis() - mSearchRequestTimestamp < SEARCH_DELAY)) {
            try {
                Thread.sleep(20);
                if (isCancelled()) {
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        mIsWaitingToSearch = false;

        if (!isCancelled()) {

            cardsMap = new HashMap<>();

            // First we search for cards where the name matches exactly the searchTerm.
            // Note: It is important to do a search for the exact name, since otherwise the
            // card might not show up in the "wildcard" search. For example a "wildcard" search
            // for "ow" might not find the card "Ow" given that there are so many other cards that
            // contain "ow" ("All Hallow's Eve", "Brown Ouphe", "Burrowing", "City of Shadows", etc.)
            // and we want to make sure that we always "find" cards that match the searchTerm exactly.
            queryOptions = generateQueryOptions(mSearchTerm, true);
            cards = CardAPI.getAllCards(queryOptions);

            if (cards.size() > 0) {
                key = mSearchTerm.toLowerCase();
                cardsMap.put(key, cards);
            }

            // Now we search for cards where the name contains the searchTerm but it doesn't need
            // to be an exact match
            queryOptions = generateQueryOptions(mSearchTerm, false);
            cards = CardAPI.getAllCards(queryOptions);

            for (Card card : cards) {
                key = card.getName().toLowerCase();

                // Ignore cards where the name matches the searchTerm exactly, since
                // those cards have already been added.
                if(key.equals(mSearchTerm.toLowerCase())){
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

        return new ArrayList<>();
    }

    @Override
    protected void onPostExecute(List<List<Card>> cardsLists) {
        super.onPostExecute(cardsLists);

        if (mOnSearchCompletedListener != null) {
            mOnSearchCompletedListener.OnSearchCompleted(cardsLists, mSearchTerm, mSearchRequestTimestamp);
        }
    }


    public boolean isWaitingToSearch() {
        return mIsWaitingToSearch;
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

        return queryOptions;
    }

    public void setOnSearchCompletedListener(OnSearchCompletedListener onSearchCompletedListener) {
        mOnSearchCompletedListener = onSearchCompletedListener;
    }

    public interface OnSearchCompletedListener {
        void OnSearchCompleted(List<List<Card>> cardsLists, String searchTerm, long searchRequestTimestamp);
    }

}