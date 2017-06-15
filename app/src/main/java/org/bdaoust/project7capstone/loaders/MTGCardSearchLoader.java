package org.bdaoust.project7capstone.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.bdaoust.project7capstone.network.MTGCardsFetcher;

import java.util.ArrayList;
import java.util.List;

import io.magicthegathering.javasdk.exception.HttpRequestFailedException;
import io.magicthegathering.javasdk.resource.Card;

public class MTGCardSearchLoader extends AsyncTaskLoader<List<List<Card>>> {

    private final static String TAG = "SearchForCardsTask";
    private final static long SEARCH_DELAY = 500; //ms
    private String mSearchTerm;
    private boolean mIsWaitingToSearch;
    private boolean mRequestFailed;
    private long mSearchRequestTimestamp;

    private List<List<Card>> mSearchCardsResult;

    public MTGCardSearchLoader(Context context, String searchTerm, long searchRequestTimestamp) {
        super(context);

        mSearchTerm = searchTerm;
        mSearchRequestTimestamp = searchRequestTimestamp;
        mRequestFailed = false;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        if (mSearchTerm == null) {
            return;
        }

        // Checking to see if the search results were already cached, if so we simply return the results.
        // This is base on the code for the Udacity Smarter GitHub Repo Search
        // https://github.com/udacity/ud851-Exercises/blob/student/Lesson05b-Smarter-GitHub-Repo-Search/T05b.03-Solution-PolishAsyncTask/app/src/main/java/com/example/android/asynctaskloader/MainActivity.java
        if (mSearchCardsResult != null) {
            deliverResult(mSearchCardsResult);
        } else {
            forceLoad();
        }
    }

    @Override
    public List<List<Card>> loadInBackground() {
        MTGCardsFetcher mtgCardsFetcher;
        List<List<Card>> cardsLists;

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
            mtgCardsFetcher = new MTGCardsFetcher();

            try {
                cardsLists = mtgCardsFetcher.fetch(mSearchTerm);

                return cardsLists;
            } catch (HttpRequestFailedException e) {
                Log.w(TAG, "Request to MTG API failed: " + e.getMessage());
                mRequestFailed = true;
            }
        }

        return new ArrayList<>();
    }

    public boolean requestFailed() {
        return mRequestFailed;
    }

    @Override
    public void deliverResult(List<List<Card>> data) {
        mSearchCardsResult = data;

        super.deliverResult(data);
    }

    public boolean isWaitingToSearch() {
        return mIsWaitingToSearch;
    }

}