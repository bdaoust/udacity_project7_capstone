package org.bdaoust.project7capstone.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.bdaoust.project7capstone.adapters.SearchCardListAdapter;
import org.bdaoust.project7capstone.R;
import org.bdaoust.project7capstone.network.SearchForCardsTask;

import java.util.ArrayList;
import java.util.List;

import io.magicthegathering.javasdk.resource.Card;

public class SearchCardsDialogFragment extends DialogFragment {

    private SearchCardListAdapter mSearchCardListAdapter;
    private SearchForCardsTask mSearchForCardsTask;
    private List<List<Card>> mCardsLists;
    private long mLastSearchRequestTimestamp;

    private static final String TAG = "SearchCardsDialogFrag";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView;
        EditText inputCardName;
        View rootView;

        rootView = inflater.inflate(R.layout.fragment_search_cards_dialog, container, false);
        inputCardName = (EditText) rootView.findViewById(R.id.inputCardName);

        inputCardName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                mLastSearchRequestTimestamp = System.currentTimeMillis();

                mCardsLists.clear();
                mSearchCardListAdapter.notifyDataSetChanged();

                //In order to reduce unnecessary network requests, the SearchForCardsTask waits (~500ms) before executing a new search. This allows us
                //to cancel "old" search requests in the event that the user is typing quickly, and that the SearchForCardsTask is still waiting.
                if (mSearchForCardsTask != null && mSearchForCardsTask.isWaitingToSearch()) {
                    Log.d(TAG, "Cancelling search for \"" + mSearchForCardsTask.getSearchTerm() + "\" due to newer search request");
                    mSearchForCardsTask.cancel(true);
                }

                if (count > 0) {
                    initiateCardSearch(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mCardsLists = new ArrayList<>();
        mSearchCardListAdapter = new SearchCardListAdapter(getContext(), mCardsLists);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.searchCardsList);
        recyclerView.setAdapter(mSearchCardListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        return rootView;
    }

    private void initiateCardSearch(String searchTerm){
        mSearchForCardsTask = new SearchForCardsTask(searchTerm, mLastSearchRequestTimestamp);

        mSearchForCardsTask.setOnSearchCompletedListener(new SearchForCardsTask.OnSearchCompletedListener() {
            @Override
            public void OnSearchCompleted(List<List<Card>> cardsList, String searchTerm, long searchRequestTimestamp) {
                // Compare the search request timestamps to make sure that the contents should be updated.
                // If searchRequestTimestamp >= lastSearchRequestTimestamp, everything is fine and the content should be
                // updated. However if searchRequestTimestamp < lastSearchRequestTimestamp then we should ignore the content since
                // there is a more recent search that was requested. This can happen if for example the user typed slow enough to
                // not have the search request canceled, but fast enough that one search request doesn't have the time to complete
                // the search and update the contents before another search is requested.
                if (searchRequestTimestamp >= mLastSearchRequestTimestamp) {

                    if (cardsList.size() == 0) {
                        Toast.makeText(getContext(), R.string.no_cards_found, Toast.LENGTH_SHORT).show();
                    } else {
                        for (List<Card> cards : cardsList) {
                            mCardsLists.add(cards);
                        }
                        mSearchCardListAdapter.notifyDataSetChanged();
                    }
                } else {
                    Log.d(TAG, "Ignoring \"" + searchTerm + "\" search results due to newer search request");
                }
            }
        });

        mSearchForCardsTask.execute();
    }

}