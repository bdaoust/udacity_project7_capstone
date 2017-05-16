package org.bdaoust.project7capstone.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.bdaoust.project7capstone.adapters.DeckListAdapter;
import org.bdaoust.project7capstone.data.Deck;
import org.bdaoust.project7capstone.R;
import org.bdaoust.project7capstone.data.SampleDeck;

public class DecksFragment extends Fragment{

    private RecyclerView mRecyclerView;
    private CreateDeckDialogFragment mCreateDeckDialogFragment;
    private View mEmptyDeckListView;
    private int mSelectedPosition = 0;

    private static final String SELECTED_KEY = "selected_position";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView;
        Deck[] decks;
        FloatingActionButton createDeckFAB;
        DeckListAdapter deckListAdapter;

        rootView = inflater.inflate(R.layout.fragment_decks, container, false);
        mEmptyDeckListView = rootView.findViewById(R.id.emptyDeckList);

        decks = new Deck[5];
        for(int i=0; i<decks.length; i++){
            decks[i] = new SampleDeck();
        }

        // Solution for keeping track of the selected position is based on
        // Project Sunshine (https://github.com/udacity/Sunshine-Version-2/blob/sunshine_master/app/src/main/java/com/example/android/sunshine/app/ForecastFragment.java)
        if(savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)){
            mSelectedPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.deckList);
        deckListAdapter = new DeckListAdapter(getContext(), decks, mSelectedPosition);
        deckListAdapter.setOnDeckClickedListener(new DeckListAdapter.OnDeckClickedListener() {
            @Override
            public void onDeckClicked(int deckId) {
                ((DecksFragment.OnDeckSelectedListener)getActivity()).onDeckSelected(deckId);
            }
        });
        if(decks.length == 0) {
            mEmptyDeckListView.setVisibility(View.VISIBLE);
        }
        mRecyclerView.setAdapter(deckListAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mCreateDeckDialogFragment = new CreateDeckDialogFragment();

        createDeckFAB = (FloatingActionButton) rootView.findViewById(R.id.createDeckFAB);
        createDeckFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCreateDeckDialogFragment.show(getFragmentManager(), "CreateDeck");
            }
        });


        return rootView;
    }

    public interface OnDeckSelectedListener {
        void onDeckSelected(int someDeckId);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_KEY, mSelectedPosition);

        super.onSaveInstanceState(outState);
    }
}
