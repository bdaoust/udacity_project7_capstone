package org.bdaoust.project7capstone.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.bdaoust.project7capstone.R;
import org.bdaoust.project7capstone.firebasemodels.MTGDeckModel;
import org.bdaoust.project7capstone.fragments.DecksFragment;
import org.bdaoust.project7capstone.ui.MTGDeckPieChart;

import java.util.List;

public class DeckListAdapter extends RecyclerView.Adapter<DeckListAdapter.DeckItemViewHolder> {

    private Context mContext;
    private List<MTGDeckModel> mMTGDecks;
    private int mSelectedPosition;
    private DecksFragment.OnDeckSelectedListener mOnDeckSelectedListener;

    public DeckListAdapter(Context context, List<MTGDeckModel> mtgDecks, int selectedPosition) {
        mContext = context;
        mMTGDecks = mtgDecks;
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
                String firebaseKey;

                position = (int) view.getTag();
                if (position != mSelectedPosition) {
                    notifyItemChanged(mSelectedPosition);
                    mSelectedPosition = position;
                    view.setSelected(true);
                }

                firebaseKey = mMTGDecks.get(position).getFirebaseKey();
                if (mOnDeckSelectedListener != null) {
                    mOnDeckSelectedListener.onDeckSelected(firebaseKey, position);
                }

            }
        });

        return deckItemViewHolder;
    }

    @Override
    public void onBindViewHolder(DeckItemViewHolder holder, int position) {
        MTGDeckModel mtgDeck;
        String extraInfo;
        String lastUpdated;
        MTGDeckModel.ColorPercentages colorPercentages;
        long lastUpdatedTimestamp;
        int numbCards;

        mtgDeck = mMTGDecks.get(position);
        numbCards = mtgDeck.getNumbCards();
        lastUpdatedTimestamp = mtgDeck.getLastUpdatedTimestamp();
        lastUpdated = DateUtils.formatDateTime(mContext, lastUpdatedTimestamp, DateUtils.FORMAT_SHOW_YEAR);
        extraInfo = mContext.getResources().getQuantityString(R.plurals.deck_extra_info, numbCards, numbCards, lastUpdated);

        holder.deckName.setText(mtgDeck.getName());
        holder.deckExtraInfo.setText(extraInfo);
        holder.itemView.setTag(position);

        if (position == mSelectedPosition) {
            holder.itemView.setSelected(true);
        } else {
            holder.itemView.setSelected(false);
        }

        colorPercentages = mtgDeck.getColorPercentages();
        holder.mtgDeckPieChart.setBlackPercentage(colorPercentages.black);
        holder.mtgDeckPieChart.setBluePercentage(colorPercentages.blue);
        holder.mtgDeckPieChart.setGreenPercentage(colorPercentages.green);
        holder.mtgDeckPieChart.setRedPercentage(colorPercentages.red);
        holder.mtgDeckPieChart.setWhitePercentage(colorPercentages.white);
    }

    @Override
    public int getItemCount() {
        return mMTGDecks.size();
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

    public void setOnDeckSelectedListener(DecksFragment.OnDeckSelectedListener onDeckSelectedListener) {
        mOnDeckSelectedListener = onDeckSelectedListener;
    }

}