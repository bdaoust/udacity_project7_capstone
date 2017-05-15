package org.bdaoust.project7capstone.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.bdaoust.project7capstone.R;
import org.bdaoust.project7capstone.data.Deck;

import io.magicthegathering.javasdk.resource.Card;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.CardItemViewHolder>{

    private Context mContext;
    private Deck mDeck;
    private OnCardClickedListener mOnCardClickedListener;

    public CardListAdapter(Context context, Deck deck){
        mContext = context;
        mDeck = deck;
    }

    @Override
    public CardItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardItemViewHolder cardItemViewHolder;
        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_card, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnCardClickedListener != null){
                    mOnCardClickedListener.onCardClicked();
                }
            }
        });

        cardItemViewHolder = new CardItemViewHolder(view);

        return cardItemViewHolder;
    }

    @Override
    public void onBindViewHolder(CardItemViewHolder holder, int position) {
        Card card = mDeck.getCards().get(position);

        Glide.with(mContext).load(card.getImageUrl()).into(holder.cardImage);
        holder.cardNumbCopies.setText(String.valueOf(mDeck.getNumbCopies(card.getMultiverseid())));
    }

    @Override
    public int getItemCount() {
        return mDeck.getCards().size();
    }

    public void setOnCardClickedListener (OnCardClickedListener onCardClickedListener) {
        mOnCardClickedListener = onCardClickedListener;
    }

    public interface OnCardClickedListener {
        void onCardClicked();
    }

    class CardItemViewHolder extends RecyclerView.ViewHolder{
        ImageView cardImage;
        TextView cardNumbCopies;

        CardItemViewHolder(View itemView){
            super(itemView);

            cardImage = (ImageView) itemView.findViewById(R.id.cardImage);
            cardNumbCopies = (TextView) itemView.findViewById(R.id.cardNumbCopies);
        }
    }
}