package org.bdaoust.project7capstone.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.bdaoust.project7capstone.R;
import org.bdaoust.project7capstone.data.Deck;

import io.magicthegathering.javasdk.resource.Card;

public class EditCardListAdapter extends RecyclerView.Adapter<EditCardListAdapter.EditCardItemViewHolder> {

    private Context mContext;
    private Deck mDeck;

    public EditCardListAdapter(Context context, Deck deck){
        mContext = context;
        mDeck = deck;
    }

    @Override
    public EditCardItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        EditCardItemViewHolder editCardItemViewHolder;
        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_edit_card, parent, false);
        editCardItemViewHolder = new EditCardItemViewHolder(view);

        return editCardItemViewHolder;
    }

    @Override
    public void onBindViewHolder(final EditCardItemViewHolder holder, int position) {
        final Card card;
        int numbCopies;

        card = mDeck.getCards().get(position);
        numbCopies = mDeck.getNumbCopies(card.getMultiverseid());
        Glide.with(holder.itemView.getContext()).load(card.getImageUrl()).into(holder.cardImage);
        holder.cardName.setText(card.getName());
        holder.cardNumbCopies.setText(String.valueOf(numbCopies));
        holder.setName.setText(String.valueOf(card.getSetName()));

        if(numbCopies == 1){
            holder.decrementButton.getDrawable().setTint(mContext.getResources().getColor(R.color.icon_button_disabled_tint_color));
        } else {
            holder.decrementButton.getDrawable().setTint(mContext.getResources().getColor(R.color.icon_button_tint_color));
        }

        if(numbCopies == 99){
            holder.incrementButton.getDrawable().setTint(mContext.getResources().getColor(R.color.icon_button_disabled_tint_color));
        } else {
            holder.incrementButton.getDrawable().setTint(mContext.getResources().getColor(R.color.icon_button_tint_color));
        }

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
                        holder.incrementButton.getDrawable().setTint(mContext.getResources().getColor(R.color.icon_button_disabled_tint_color));
                    }

                    if(numbCopies == 2){
                        holder.decrementButton.getDrawable().setTint(mContext.getResources().getColor(R.color.icon_button_tint_color));
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
                        holder.decrementButton.getDrawable().setTint(mContext.getResources().getColor(R.color.icon_button_disabled_tint_color));
                    }

                    if(numbCopies == 98){
                        holder.incrementButton.getDrawable().setTint(mContext.getResources().getColor(R.color.icon_button_tint_color));
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

    class EditCardItemViewHolder extends RecyclerView.ViewHolder {

        ImageView cardImage;
        TextView cardName;
        TextView cardNumbCopies;
        TextView setName;
        ImageButton decrementButton;
        ImageButton incrementButton;
        ImageButton deleteButton;

        EditCardItemViewHolder(View itemView) {
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


