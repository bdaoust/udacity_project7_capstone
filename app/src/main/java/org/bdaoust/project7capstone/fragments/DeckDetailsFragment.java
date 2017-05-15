package org.bdaoust.project7capstone.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.bdaoust.project7capstone.data.Deck;
import org.bdaoust.project7capstone.R;
import org.bdaoust.project7capstone.data.SampleDeck;
import org.bdaoust.project7capstone.activities.EditDeckActivity;

import io.magicthegathering.javasdk.resource.Card;

public class DeckDetailsFragment extends Fragment{

    private RecyclerView mRecyclerView;
    private boolean mIsLargeLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView;
        GridLayoutManager gridLayoutManager;
        CustomAdapter customAdapter;
        Deck deck;
        int numbColumns;

        rootView = inflater.inflate(R.layout.fragment_deck_details, container, false);
        numbColumns = getResources().getInteger(R.integer.card_list_column_count);
        gridLayoutManager = new GridLayoutManager(getContext(), numbColumns);
        deck = new SampleDeck();
        customAdapter = new CustomAdapter(deck);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.cardsList);
        mRecyclerView.setAdapter(customAdapter);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mIsLargeLayout = getResources().getBoolean(R.bool.large_layout);

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

    private class CustomAdapter extends RecyclerView.Adapter<CustomViewHolder>{

        Deck mDeck;

        public CustomAdapter(Deck deck){
            mDeck = deck;
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            CustomViewHolder customViewHolder;
            View view;

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_card, parent, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showCardDetails();
                }
            });

            customViewHolder = new CustomViewHolder(view);

            return customViewHolder;
        }

        @Override
        public void onBindViewHolder(CustomViewHolder holder, int position) {
            Card card = mDeck.getCards().get(position);

            Glide.with(getContext()).load(card.getImageUrl()).into(holder.cardImage);
            holder.cardNumbCopies.setText(String.valueOf(mDeck.getNumbCopies(card.getMultiverseid())));
        }

        @Override
        public int getItemCount() {
            return mDeck.getCards().size();
        }
    }

    private class CustomViewHolder extends RecyclerView.ViewHolder{
        public ImageView cardImage;
        public TextView cardNumbCopies;

        public CustomViewHolder(View itemView){
            super(itemView);

            cardImage = (ImageView) itemView.findViewById(R.id.cardImage);
            cardNumbCopies = (TextView) itemView.findViewById(R.id.cardNumbCopies);
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
