package org.bdaoust.project7capstone.fragments;

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

import org.bdaoust.project7capstone.data.Deck;
import org.bdaoust.project7capstone.ui.MTGDeckPieChart;
import org.bdaoust.project7capstone.R;
import org.bdaoust.project7capstone.data.SampleDeck;

public class DecksFragment extends Fragment{

    RecyclerView mRecyclerView;
    CreateDeckDialogFragment mCreateDeckDialogFragment;
    View mEmptyDeckListView;
    int selectedPosition = 0;

    private static final String SELECTED_KEY = "selected_position";

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

        // Solution for keeping track of the selected position is based on
        // Project Sunshine (https://github.com/udacity/Sunshine-Version-2/blob/sunshine_master/app/src/main/java/com/example/android/sunshine/app/ForecastFragment.java)
        if(savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)){
            selectedPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

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

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_deck, parent, false);
            customViewHolder = new CustomViewHolder(view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position;

                    position = (int)view.getTag();
                    if(position != selectedPosition){
                        notifyItemChanged(selectedPosition);
                        selectedPosition = position;
                        view.setSelected(true);
                    }
                    ((OnDeckSelectedListener)getActivity()).onDeckSelected((int)view.getTag());
                }
            });

            return customViewHolder;
        }


        @Override
        public void onBindViewHolder(CustomViewHolder holder, int position) {
            Deck deck;
            String extraInfo;
            String lastUpdated;
            Deck.ColorPercentages colorPercentages;
            long lastUpdatedTimestamp;
            int numbCards;

            deck = mDecks[position];
            numbCards = deck.getNumbCards();
            lastUpdatedTimestamp = deck.getLastUpdatedTimestamp();
            lastUpdated = DateUtils.formatDateTime(getContext(),lastUpdatedTimestamp, DateUtils.FORMAT_SHOW_YEAR);
            extraInfo = getResources().getString(R.string.deck_extra_info, numbCards, lastUpdated);

            holder.deckName.setText(deck.getDeckName() + " " + position);
            holder.deckExtraInfo.setText(extraInfo);
            holder.itemView.setTag(position);

            if(position == selectedPosition){
                holder.itemView.setSelected(true);
            } else {
                holder.itemView.setSelected(false);
            }

            colorPercentages = deck.getColorPercentages();
            holder.mtgDeckPieChart.setBlackPercentage(colorPercentages.black);
            holder.mtgDeckPieChart.setBluePercentage(colorPercentages.blue);
            holder.mtgDeckPieChart.setGreenPercentage(colorPercentages.green);
            holder.mtgDeckPieChart.setRedPercentage(colorPercentages.red);
            holder.mtgDeckPieChart.setWhitePercentage(colorPercentages.white);
        }

        @Override
        public int getItemCount() {
            return mDecks.length;
        }

    }

    private class CustomViewHolder extends RecyclerView.ViewHolder{

        public TextView deckName;
        public TextView deckExtraInfo;
        public MTGDeckPieChart mtgDeckPieChart;

        public CustomViewHolder(View itemView) {
            super(itemView);

            deckName = (TextView) itemView.findViewById(R.id.deckName);
            deckExtraInfo = (TextView) itemView.findViewById(R.id.deckExtraInfo);
            mtgDeckPieChart = (MTGDeckPieChart) itemView.findViewById(R.id.deckPieChart);
        }
    }

    public interface OnDeckSelectedListener {
        void onDeckSelected(int someDeckId);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_KEY, selectedPosition);

        super.onSaveInstanceState(outState);
    }
}
