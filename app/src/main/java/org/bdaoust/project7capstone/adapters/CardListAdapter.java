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
import org.bdaoust.project7capstone.firebasemodels.MTGCardModel;
import org.bdaoust.project7capstone.firebasemodels.MTGDeckModel;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.CardItemViewHolder>{

    private Context mContext;
    private MTGDeckModel mMTGDeckModel;
    private OnCardClickedListener mOnCardClickedListener;

    public CardListAdapter(Context context, MTGDeckModel mtgDeckModel){
        mContext = context;
        mMTGDeckModel = mtgDeckModel;
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
        MTGCardModel mtgCardModel;
        int numbCopies;

        mtgCardModel = mMTGDeckModel.getMTGCardModels().get(position);
        numbCopies = mtgCardModel.getNumbCopies();

        Glide.with(mContext).load(mtgCardModel.getImageUrl()).into(holder.cardImage);
        holder.cardNumbCopies.setText(String.valueOf(numbCopies));
        // Setting the contentDescription on the cardNumbCopies TextView instead of the cardImage
        // ImageView, otherwise TalkBack will read the name of the card followed by the number
        // of copies as opposed to the number of copies followed by the name of the card.
        holder.cardNumbCopies.setContentDescription(numbCopies + mtgCardModel.getName());
    }

    @Override
    public int getItemCount() {
        return mMTGDeckModel.getMTGCardModels().size();
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