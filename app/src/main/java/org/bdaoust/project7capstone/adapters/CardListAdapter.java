package org.bdaoust.project7capstone.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.bdaoust.project7capstone.R;
import org.bdaoust.project7capstone.firebasemodels.MTGCardModel;
import org.bdaoust.project7capstone.firebasemodels.MTGDeckModel;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.CardItemViewHolder> {

    private Context mContext;
    private MTGDeckModel mMTGDeck;
    private OnCardClickedListener mOnCardClickedListener;

    public CardListAdapter(Context context, MTGDeckModel mtgDeck) {
        mContext = context;
        mMTGDeck = mtgDeck;
    }

    @Override
    public CardItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardItemViewHolder cardItemViewHolder;
        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_card, parent, false);
        cardItemViewHolder = new CardItemViewHolder(view);

        return cardItemViewHolder;
    }

    @Override
    public void onBindViewHolder(CardItemViewHolder holder, int position) {
        final MTGCardModel mtgCard;
        int numbCopies;

        mtgCard = mMTGDeck.getMTGCards().get(position);
        numbCopies = mtgCard.getNumbCopies();

        Glide.with(mContext)
                .load(mtgCard.getImageUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.cardImage);
        holder.cardNumbCopies.setText(String.valueOf(numbCopies));
        // Setting the contentDescription on the cardNumbCopies TextView instead of the cardImage
        // ImageView, otherwise TalkBack will read the name of the card followed by the number
        // of copies as opposed to the number of copies followed by the name of the card.
        holder.cardNumbCopies.setContentDescription(numbCopies + mtgCard.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firebaseCardKey;

                firebaseCardKey = mtgCard.getFirebaseKey();
                if (mOnCardClickedListener != null) {
                    mOnCardClickedListener.onCardClicked(firebaseCardKey);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMTGDeck.getMTGCards().size();
    }

    public void setOnCardClickedListener(OnCardClickedListener onCardClickedListener) {
        mOnCardClickedListener = onCardClickedListener;
    }

    public interface OnCardClickedListener {
        void onCardClicked(String firebaseKey);
    }

    class CardItemViewHolder extends RecyclerView.ViewHolder {
        ImageView cardImage;
        TextView cardNumbCopies;

        CardItemViewHolder(View itemView) {
            super(itemView);

            cardImage = (ImageView) itemView.findViewById(R.id.cardImage);
            cardNumbCopies = (TextView) itemView.findViewById(R.id.cardNumbCopies);
        }
    }
}