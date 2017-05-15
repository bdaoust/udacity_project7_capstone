package org.bdaoust.project7capstone;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

        Toolbar toolbar;
        ActionBar actionBar;
        Resources resources;

        setContentView(R.layout.activity_edit_deck);

        toolbar = (Toolbar)findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if(actionBar !=null){
            resources = getResources();
            getSupportActionBar().setTitle(resources.getText(R.string.activity_name_edit_deck));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_deck, menu);

        return true;
    }


    private class CustomAdapter extends RecyclerView.Adapter<CustomViewHolder> {
        private Deck mDeck;

        public CustomAdapter(Deck deck){
            mDeck = deck;
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            CustomViewHolder customViewHolder;
            View view;

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_edit_card, parent, false);
            customViewHolder = new CustomViewHolder(view);

            return customViewHolder;
        }

        @Override
        public void onBindViewHolder(final CustomViewHolder holder, int position) {
            final Card card;

            card = mDeck.getCards().get(position);
            Glide.with(holder.itemView.getContext()).load(card.getImageUrl()).into(holder.cardImage);
            holder.cardName.setText(card.getName());
            holder.cardNumbCopies.setText(String.valueOf(mDeck.getNumbCopies(card.getMultiverseid())));
            holder.setName.setText(String.valueOf(card.getSetName()));

            holder.incrementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int numbCopies;

                    numbCopies = mDeck.getNumbCopies(card.getMultiverseid());
                    if(numbCopies < 99){
                        mDeck.addCardCopies(card, 1);
                        numbCopies++;
                        holder.cardNumbCopies.setText(String.valueOf(numbCopies));

                        if(numbCopies == 99){
                            holder.incrementButton.getDrawable().setTint(getResources().getColor(R.color.icon_button_disabled_tint_color));
                        }

                        if(numbCopies == 2){
                            holder.decrementButton.getDrawable().setTint(getResources().getColor(R.color.icon_button_tint_color));
                        }
                    }
                }
            });


            holder.decrementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int numbCopies;

                    numbCopies = mDeck.getNumbCopies(card.getMultiverseid());
                    if(numbCopies > 1){
                        numbCopies--;
                        mDeck.setCardCopies(card, numbCopies);
                        holder.cardNumbCopies.setText(String.valueOf(numbCopies));

                        if(numbCopies == 1){
                            holder.decrementButton.getDrawable().setTint(getResources().getColor(R.color.icon_button_disabled_tint_color));
                        }

                        if(numbCopies == 98){
                            holder.incrementButton.getDrawable().setTint(getResources().getColor(R.color.icon_button_tint_color));
                        }
                    }
                }
            });

            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDeck.removeCardCopies(card.getMultiverseid());
                    notifyDataSetChanged();
                }
            });

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
        public ImageButton decrementButton;
        public ImageButton incrementButton;
        public ImageButton deleteButton;

        public CustomViewHolder(View itemView) {
            super(itemView);

            cardImage = (ImageView) itemView.findViewById(R.id.cardImage);
            cardName = (TextView) itemView.findViewById(R.id.cardName);
            cardNumbCopies = (TextView) itemView.findViewById(R.id.cardNumbCopies);
            setName = (TextView) itemView.findViewById(R.id.setName);
            decrementButton = (ImageButton) itemView.findViewById(R.id.actionDecrementCardCopies);
            incrementButton = (ImageButton) itemView.findViewById(R.id.actionIncrementCardCopies);
            deleteButton = (ImageButton) itemView.findViewById(R.id.actionDeleteCardCopies);
        }
    }
}
