package org.bdaoust.project7capstone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DecksFragment extends Fragment{

    RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView;
        Deck[] decks;

        rootView = inflater.inflate(R.layout.fragment_decks, container, false);

        decks = new Deck[5];
        for(int i=0; i<decks.length; i++){
            decks[i] = new SampleDeck();
        }

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.deckList);
        mRecyclerView.setAdapter(new CustomAdapter(decks));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return rootView;
    }

    private class CustomAdapter extends RecyclerView.Adapter<CustomViewHolder>{

        Deck[] mDecks;

        public CustomAdapter(Deck[] decks){
            mDecks = decks;
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.deck_list_item, parent, false);

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CustomViewHolder holder, int position) {
            holder.deckName.setText(mDecks[position].getDeckName() + " " + position);
        }

        @Override
        public int getItemCount() {
            return mDecks.length;
        }
    }

    private class CustomViewHolder extends RecyclerView.ViewHolder{

        public TextView deckName;

        public CustomViewHolder(View itemView) {
            super(itemView);

            deckName = (TextView) itemView.findViewById(R.id.deckName);
        }
    }
}
