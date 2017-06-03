package org.bdaoust.project7capstone.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.bdaoust.project7capstone.MTGKeys;
import org.bdaoust.project7capstone.adapters.CardListAdapter;
import org.bdaoust.project7capstone.data.Deck;
import org.bdaoust.project7capstone.R;
import org.bdaoust.project7capstone.data.SampleDeck;
import org.bdaoust.project7capstone.activities.EditDeckActivity;

public class DeckDetailsFragment extends Fragment{

    private boolean mIsLargeLayout;
    private String mFirebaseDeckKey;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView;
        RecyclerView recyclerView;
        GridLayoutManager gridLayoutManager;
        CardListAdapter cardListAdapter;
        Deck deck;
        int numbColumns;

        rootView = inflater.inflate(R.layout.fragment_deck_details, container, false);
        numbColumns = getResources().getInteger(R.integer.card_list_column_count);
        gridLayoutManager = new GridLayoutManager(getContext(), numbColumns);
        deck = new SampleDeck();
        cardListAdapter = new CardListAdapter(getContext(), deck);

        cardListAdapter.setOnCardClickedListener(new CardListAdapter.OnCardClickedListener() {
            @Override
            public void onCardClicked() {
                showCardDetails();
            }
        });

        recyclerView = (RecyclerView) rootView.findViewById(R.id.cardsList);
        recyclerView.setAdapter(cardListAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);

        mIsLargeLayout = getResources().getBoolean(R.bool.large_layout);

        mFirebaseDeckKey = getArguments().getString(MTGKeys.FIREBASE_DECK_KEY);
        Log.d("DeckDetailsFragment", "----------- The Deck to load is " + mFirebaseDeckKey + " ---------------");

        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_deck_details, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_edit:
                Intent intent;

                intent = new Intent(getContext(), EditDeckActivity.class);
                startActivity(intent);

                return true;
            case R.id.action_delete:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showCardDetails(){
        CardDetailsDialogFragment cardDetailsDialogFragment;
        FragmentManager fragmentManager;

        cardDetailsDialogFragment = new CardDetailsDialogFragment();
        fragmentManager = getFragmentManager();

        if(mIsLargeLayout){
            cardDetailsDialogFragment.show(getFragmentManager(), "CardDetails");
        } else {
            FragmentTransaction fragmentTransaction;

            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.add(android.R.id.content, cardDetailsDialogFragment)
                .addToBackStack(null)
                .commit();
        }

    }
}
