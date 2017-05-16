package org.bdaoust.project7capstone.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.bdaoust.project7capstone.R;
import org.bdaoust.project7capstone.data.Deck;
import org.bdaoust.project7capstone.ui.MTGDeckPieChart;

public class DeckListAdapter extends RecyclerView.Adapter<DeckListAdapter.DeckItemViewHolder> {

    private Context mContext;
    private Deck[] mDecks;
    private int mSelectedPosition;
    private OnDeckClickedListener mOnDeckClickedListener;

    public DeckListAdapter(Context context, Deck[] decks, int selectedPosition){
        mContext = context;
        mDecks = decks;
        mSelectedPosition = selectedPosition;
    }

    @Override
    public DeckItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DeckItemViewHolder deckItemViewHolder;
        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_deck, parent, false);
        deckItemViewHolder = new DeckItemViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position;

                position = (int)view.getTag();
                if(position != mSelectedPosition){
                    notifyItemChanged(mSelectedPosition);
                    mSelectedPosition = position;
                    view.setSelected(true);
                }

                if(mOnDeckClickedListener != null) {
                    mOnDeckClickedListener.onDeckClicked((int)view.getTag());
                }

            }
        });

        return deckItemViewHolder;
    }

    @Override
    public void onBindViewHolder(DeckItemViewHolder holder, int position) {
        Deck deck;
        String extraInfo;
        String lastUpdated;
        Deck.ColorPercentages colorPercentages;
        long lastUpdatedTimestamp;
        int numbCards;

        deck = mDecks[position];
        numbCards = deck.getNumbCards();
        lastUpdatedTimestamp = deck.getLastUpdatedTimestamp();
        lastUpdated = DateUtils.formatDateTime(mContext,lastUpdatedTimestamp, DateUtils.FORMAT_SHOW_YEAR);
        extraInfo = mContext.getResources().getString(R.string.deck_extra_info, numbCards, lastUpdated);

        holder.deckName.setText(deck.getDeckName() + " " + position);
        holder.deckExtraInfo.setText(extraInfo);
        holder.itemView.setTag(position);

        if(position == mSelectedPosition){
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

    class DeckItemViewHolder extends RecyclerView.ViewHolder {

        TextView deckName;
        TextView deckExtraInfo;
        MTGDeckPieChart mtgDeckPieChart;

        DeckItemViewHolder(View itemView) {
            super(itemView);

            deckName = (TextView) itemView.findViewById(R.id.deckName);
            deckExtraInfo = (TextView) itemView.findViewById(R.id.deckExtraInfo);
            mtgDeckPieChart = (MTGDeckPieChart) itemView.findViewById(R.id.deckPieChart);
        }
    }

    public void setOnDeckClickedListener(OnDeckClickedListener onDeckClickedListener) {
        mOnDeckClickedListener = onDeckClickedListener;
    }

    public interface OnDeckClickedListener {
        void onDeckClicked(int deckId);
    }
}