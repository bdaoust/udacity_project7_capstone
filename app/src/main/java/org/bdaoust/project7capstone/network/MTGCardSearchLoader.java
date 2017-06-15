package org.bdaoust.project7capstone.network;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import io.magicthegathering.javasdk.api.CardAPI;
import io.magicthegathering.javasdk.exception.HttpRequestFailedException;
import io.magicthegathering.javasdk.resource.Card;

public class MTGCardSearchLoader extends AsyncTaskLoader<List<List<Card>>> {

    private final static String TAG = "SearchForCardsTask";
    private final static long SEARCH_DELAY = 500; //ms
    private String mSearchTerm;
    private Comparator<List<Card>> mCardsListComparator;
    private boolean mIsWaitingToSearch;
    private boolean mRequestFailed;
    private long mSearchRequestTimestamp;
    private String mSets;

    private List<List<Card>> mSearchCardsResult;

    public MTGCardSearchLoader(Context context, String searchTerm, long searchRequestTimestamp) {
        super(context);

        mSearchTerm = searchTerm;
        mSearchRequestTimestamp = searchRequestTimestamp;
        mRequestFailed = false;

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

        mSets = "";
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        if(mSearchTerm == null){
            return;
        }

        // Checking to see if the search results were already cached, if so we simply return the results.
        // This is base on the code for the Udacity Smarter GitHub Repo Search
        // https://github.com/udacity/ud851-Exercises/blob/student/Lesson05b-Smarter-GitHub-Repo-Search/T05b.03-Solution-PolishAsyncTask/app/src/main/java/com/example/android/asynctaskloader/MainActivity.java
        if(mSearchCardsResult != null){
            deliverResult(mSearchCardsResult);
        } else {
            forceLoad();
        }
    }

    @Override
    public List<List<Card>> loadInBackground() {
        List<String> queryOptions;
        List<Card> cards;
        List<List<Card>> cardsLists;
        HashMap<String, List<Card>> cardsMap;
        String key;

        mIsWaitingToSearch = true;

        while ((System.currentTimeMillis() - mSearchRequestTimestamp < SEARCH_DELAY)) {
            try {
                Thread.sleep(20);
                if (isLoadInBackgroundCanceled()) {
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        mIsWaitingToSearch = false;

        if (!isLoadInBackgroundCanceled()) {
            cardsMap = new HashMap<>();

            // First we search for cards where the name matches exactly the searchTerm.
            // Note: It is important to do a search for the exact name, since otherwise the
            // card might not show up in the "wildcard" search. For example a "wildcard" search
            // for "ow" might not find the card "Ow" given that there are so many other cards that
            // contain "ow" ("All Hallow's Eve", "Brown Ouphe", "Burrowing", "City of Shadows", etc.)
            // and we want to make sure that we always "find" cards that match the searchTerm exactly.
            try {
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
                    if (key.equals(mSearchTerm.toLowerCase())) {
                        continue;
                    }
                    if (!cardsMap.containsKey(key)) {
                        cardsMap.put(key, new ArrayList<Card>());
                    }
                    cardsMap.get(key).add(card);
                }

            } catch (HttpRequestFailedException e) {
                Log.w(TAG, "Request to MTG API failed: " + e.getMessage());
                mRequestFailed = true;
            }

            cardsLists = new ArrayList<>(cardsMap.values());
            Collections.sort(cardsLists, mCardsListComparator);

            return cardsLists;
        }

        return new ArrayList<>();
    }

    public boolean requestFailed(){
        return mRequestFailed;
    }

    @Override
    public void deliverResult(List<List<Card>> data) {
        mSearchCardsResult = data;

        super.deliverResult(data);
    }


    public void setSets(String sets){
        mSets = sets;
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
        if(!mSets.equals("")){
            queryOptions.add("set=" + mSets);
        }

        return queryOptions;
    }

}
