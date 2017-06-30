package org.bdaoust.project7capstone.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import org.bdaoust.project7capstone.adapters.SearchCardListAdapter;
import org.bdaoust.project7capstone.loaders.MTGCardSearchLoader;
import org.bdaoust.project7capstone.R;

import java.util.ArrayList;
import java.util.List;

import io.magicthegathering.javasdk.resource.Card;

public class SearchCardsDialogFragment extends DialogFragment implements LoaderManager.LoaderCallbacks<List<List<Card>>> {

    private static final String TAG = "SearchCardsDialogFrag";
    private SearchCardListAdapter mSearchCardListAdapter;
    private List<List<Card>> mCardsLists;
    private ProgressBar mProgressBar;
    private int mToastStringResource;
    private int mCurrentLoaderId = 0;
    private String mLastSearchTerm = "";
    private final static String SEARCH_TERM = "searchTerm";
    private final static String LOADER_ID = "loaderId";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView;
        EditText inputCardName;
        View rootView;

        rootView = inflater.inflate(R.layout.fragment_search_cards_dialog, container, false);
        inputCardName = (EditText) rootView.findViewById(R.id.inputCardName);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        mToastStringResource = 0;

        if (savedInstanceState != null) {
            mLastSearchTerm = savedInstanceState.getString(SEARCH_TERM, "");
            mCurrentLoaderId = savedInstanceState.getInt(LOADER_ID, 0);
        }

        if (!isConnected()) {
            showToastWhenReady(R.string.no_network_connection);
        }

        inputCardName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String searchTerm;

                searchTerm = charSequence.toString().trim();
                if (isConnected()) {

                    mCardsLists.clear();
                    mSearchCardListAdapter.clearSpinnerPositionCache();
                    mSearchCardListAdapter.notifyDataSetChanged();

                    if (searchTerm.length() > 0) {
                        mProgressBar.setVisibility(View.VISIBLE);
                        if (!mLastSearchTerm.equals(searchTerm)) {
                            initiateCardSearch(searchTerm, false);
                            mLastSearchTerm = searchTerm;
                        } else {
                            initiateCardSearch(searchTerm, true);
                        }

                    } else {
                        mProgressBar.setVisibility(View.GONE);
                        mLastSearchTerm = "";
                    }
                } else {
                    showToastWhenReady(R.string.no_network_connection);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mCardsLists = new ArrayList<>();
        mSearchCardListAdapter = new SearchCardListAdapter(getContext(), mCardsLists);

        mSearchCardListAdapter.setOnCardAddedListener(new SearchCardListAdapter.OnCardAddedListener() {
            @Override
            public void onCardAdded(Card card) {
                ((SearchCardListAdapter.OnCardAddedListener) getActivity()).onCardAdded(card);
                dismiss();
            }
        });

        recyclerView = (RecyclerView) rootView.findViewById(R.id.searchCardsList);
        recyclerView.setAdapter(mSearchCardListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));


        return rootView;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(SEARCH_TERM, mLastSearchTerm);
        outState.putInt(LOADER_ID, mCurrentLoaderId);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mToastStringResource > 0) {
            Toast.makeText(getContext(), mToastStringResource, Toast.LENGTH_SHORT).show();
            mToastStringResource = 0;
        }
    }


    private void showToastWhenReady(int stringResource) {
        if (getContext() != null) {
            Toast.makeText(getContext(), stringResource, Toast.LENGTH_SHORT).show();
        } else {
            mToastStringResource = stringResource;
        }
    }

    private void initiateCardSearch(String searchTerm, boolean loadFromCache) {
        MTGCardSearchLoader mtgCardSearchLoader;
        Loader<List<List<Card>>> loader;
        LoaderManager loaderManager;
        Bundle bundle;

        bundle = new Bundle();
        bundle.putString(SEARCH_TERM, searchTerm);

        loaderManager = getLoaderManager();
        loader = loaderManager.getLoader(mCurrentLoaderId);
        mtgCardSearchLoader = (MTGCardSearchLoader) loader;

        if (loadFromCache) {
            if (mtgCardSearchLoader.requestFailed()) {
                Log.d(TAG, "Last request failed, retrying request #: " + mCurrentLoaderId);
                loaderManager.restartLoader(mCurrentLoaderId, bundle, this);
            } else {
                Log.d(TAG, "Requesting data from cache of loader #: " + mCurrentLoaderId);
                loaderManager.initLoader(mCurrentLoaderId, bundle, this);
            }
        } else {

            if (mtgCardSearchLoader != null) {
                // In order to reduce unnecessary network requests, the MTGCardSearchLoader waits (~500ms) before
                // executing a new search. This allows us to cancel "old" search requests in the event that the
                // user is typing quickly, and that the MTGCardSearchLoader is still waiting.
                if (mtgCardSearchLoader.isWaitingToSearch()) {
                    Log.v(TAG, "Cancelling loader #: " + mtgCardSearchLoader.getId());
                    mtgCardSearchLoader.cancelLoad();
                }
            }
            mCurrentLoaderId++;
            Log.d(TAG, "Requesting data from new loader #: " + mCurrentLoaderId);
            loaderManager.initLoader(mCurrentLoaderId, bundle, this);
        }

    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager;
        NetworkInfo activeNetwork;

        connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = connectivityManager.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public Loader<List<List<Card>>> onCreateLoader(int id, Bundle args) {
        MTGCardSearchLoader mtgCardSearchLoader;
        String searchTerm;

        searchTerm = args.getString(SEARCH_TERM);
        mtgCardSearchLoader = new MTGCardSearchLoader(getContext(), searchTerm, System.currentTimeMillis());

        return mtgCardSearchLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<List<Card>>> loader, List<List<Card>> cardsList) {
        // Compare this loader's id with the current loader id to make sure that the contents should be updated.
        // If loader.getId() < mCurrentLoaderId, there is a newer loader that was initiated so we ignore the
        // data from this loader. This can happen if for example the user typed slow enough to not have the search request
        // canceled, but fast enough that one search request doesn't have the time to complete the search and update
        // the contents before another search is requested.
        if (loader.getId() < mCurrentLoaderId || mLastSearchTerm.equals("")) {
            Log.d(TAG, "Ignoring data from Loader #: " + loader.getId());
        } else {
            if (cardsList.size() == 0) {
                if (((MTGCardSearchLoader) loader).requestFailed()) {
                    showToastWhenReady(R.string.error_loading_search_results);
                } else {
                    showToastWhenReady(R.string.no_cards_found);
                }

            } else {
                Log.d(TAG, "Loading data using Loader #: " + loader.getId());

                for (List<Card> cards : cardsList) {
                    mCardsLists.add(cards);
                }

                mSearchCardListAdapter.notifyDataSetChanged();
            }

            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<List<Card>>> loader) {

    }

}