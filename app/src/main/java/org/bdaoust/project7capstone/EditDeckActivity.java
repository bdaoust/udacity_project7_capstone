package org.bdaoust.project7capstone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import io.magicthegathering.javasdk.resource.Card;

public class EditDeckActivity extends AppCompatActivity{

    private RecyclerView mRecyclerView;
    private FloatingActionButton mSearchCardsFAB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_deck);

        mRecyclerView = (RecyclerView)findViewById(R.id.editCardsList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(new CustomAdapter(new SampleDeck()));

        mSearchCardsFAB = (FloatingActionButton) findViewById(R.id.searchCardsFAB);

        mSearchCardsFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchCardsDialogFragment searchCardsDialogFragment;

                searchCardsDialogFragment = new SearchCardsDialogFragment();
                searchCardsDialogFragment.show(getSupportFragmentManager(), "SEARCH_CARDS");
            }
        });
    }

    private class CustomAdapter extends RecyclerView.Adapter<CustomViewHolder>{
        private Deck mDeck;

        public CustomAdapter(Deck deck){
            mDeck = deck;
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            CustomViewHolder customViewHolder;
            View view;

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_card_list_item, parent, false);
            customViewHolder = new CustomViewHolder(view);

            return customViewHolder;
        }

        @Override
        public void onBindViewHolder(CustomViewHolder holder, int position) {
            Card card;

            card = mDeck.getCards().get(position);
            Glide.with(holder.itemView.getContext()).load(card.getImageUrl()).into(holder.cardImage);
            holder.cardName.setText(card.getName());
            holder.cardNumbCopies.setText(String.valueOf(mDeck.getNumbCopies(card.getMultiverseid())));
            holder.setName.setText(String.valueOf(card.getSetName()));
        }

        @Override
        public int getItemCount() {
            return mDeck.getCards().size();
        }
    }

    private class CustomViewHolder extends RecyclerView.ViewHolder{

        public ImageView cardImage;
        public TextView cardName;
        public TextView cardNumbCopies;
        public TextView setName;

        public CustomViewHolder(View itemView) {
            super(itemView);

            cardImage = (ImageView) itemView.findViewById(R.id.cardImage);
            cardName = (TextView) itemView.findViewById(R.id.cardName);
            cardNumbCopies = (TextView) itemView.findViewById(R.id.cardNumbCopies);
            setName = (TextView) itemView.findViewById(R.id.setName);
        }
    }
}
