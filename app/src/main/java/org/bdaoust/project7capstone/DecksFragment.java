package org.bdaoust.project7capstone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DecksFragment extends Fragment{

    RecyclerView mRecyclerView;
    CreateDeckDialogFragment mCreateDeckDialogFragment;
    View mEmptyDeckListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView;
        Deck[] decks;
        FloatingActionButton createDeckFAB;

        rootView = inflater.inflate(R.layout.fragment_decks, container, false);
        mEmptyDeckListView = rootView.findViewById(R.id.emptyDeckList);

        decks = new Deck[5];
        for(int i=0; i<decks.length; i++){
            decks[i] = new SampleDeck();
        }

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.deckList);
        mRecyclerView.setAdapter(new CustomAdapter(decks));
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

    private class CustomAdapter extends RecyclerView.Adapter<CustomViewHolder>{

        Deck[] mDecks;

        public CustomAdapter(Deck[] decks){
            mDecks = decks;

            if(mDecks.length == 0){
                mEmptyDeckListView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            CustomViewHolder customViewHolder;
            View view;

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.deck_list_item, parent, false);
            customViewHolder = new CustomViewHolder(view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent;

                    intent = new Intent(getContext(), DeckDetailsActivity.class);
                    startActivity(intent);
                }
            });

            return customViewHolder;
        }

        @Override
        public void onBindViewHolder(CustomViewHolder holder, int position) {
            holder.deckName.setText(mDecks[position].getDeckName() + " " + position);
            holder.numbCards.setText(mDecks[position].getNumbCards() + " cards - ");
            holder.lastUpdated.setText(DateUtils.formatDateTime(getContext(), mDecks[position].getLastUpdatedTimestamp(), DateUtils.FORMAT_SHOW_YEAR));
        }

        @Override
        public int getItemCount() {
            return mDecks.length;
        }
    }

    private class CustomViewHolder extends RecyclerView.ViewHolder{

        public TextView deckName;
        public TextView numbCards;
        public TextView lastUpdated;

        public CustomViewHolder(View itemView) {
            super(itemView);

            deckName = (TextView) itemView.findViewById(R.id.deckName);
            numbCards = (TextView) itemView.findViewById(R.id.numbCards);
            lastUpdated = (TextView) itemView.findViewById(R.id.lastUpdated);
        }
    }
}
