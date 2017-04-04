package org.bdaoust.project7capstone;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import io.magicthegathering.javasdk.resource.Card;

public class DeckDetailsFragment extends Fragment{

    RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView;

        rootView = inflater.inflate(R.layout.fragment_deck_details, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.cardsList);
        mRecyclerView.setAdapter(new CustomAdapter(new SampleDeck()));
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        return rootView;
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

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list_item, parent, false);
            customViewHolder = new CustomViewHolder(view);

            return customViewHolder;
        }

        @Override
        public void onBindViewHolder(CustomViewHolder holder, int position) {
            Card card = mDeck.getCards().get(position);

            Glide.with(getContext()).load(card.getImageUrl()).into(holder.cardImage);
        }

        @Override
        public int getItemCount() {
            return mDeck.getCards().size();
        }
    }

    private class CustomViewHolder extends RecyclerView.ViewHolder{
        public ImageView cardImage;

        public CustomViewHolder(View itemView){
            super(itemView);

            cardImage = (ImageView) itemView.findViewById(R.id.cardImage);
        }
    }
}
