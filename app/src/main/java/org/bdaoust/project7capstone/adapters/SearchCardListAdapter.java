package org.bdaoust.project7capstone.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.bdaoust.project7capstone.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.magicthegathering.javasdk.resource.Card;

public class SearchCardListAdapter extends RecyclerView.Adapter<SearchCardListAdapter.SearchCardItemViewHolder>{

    private Context mContext;
    private List<List<Card>> mCardsLists;
    private OnCardAddedListener mOnCardAddedListener;
    private HashMap<String, Integer> mSpinnerPositionCache;

    public SearchCardListAdapter(Context context, List<List<Card>> cardsLists){
        mContext = context;
        mCardsLists = cardsLists;
        mSpinnerPositionCache = new HashMap<>();
    }

    @Override
    public SearchCardItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SearchCardItemViewHolder searchCardItemViewHolder;
        View view;

        view = LayoutInflater.from(mContext).inflate(R.layout.list_item_search_card, parent, false);

        searchCardItemViewHolder = new SearchCardItemViewHolder(view);

        return searchCardItemViewHolder;
    }

    @Override
    public void onBindViewHolder(final SearchCardItemViewHolder holder, int position) {

        ArrayAdapter<CharSequence> adapter;
        ArrayList<CharSequence> setNames;
        final List<Card> cards;
        Card card;
        String cardName;
        int spinnerPosition;

        cards = mCardsLists.get(position);
        spinnerPosition = 0;

        //All cards will have the same name so we can get the name from the first element
        cardName = cards.get(0).getName();
        if(mSpinnerPositionCache.containsKey(cardName)) {
            spinnerPosition = mSpinnerPositionCache.get(cardName);
        }

        card = cards.get(spinnerPosition);
        Glide.with(mContext).load(card.getImageUrl()).into(holder.cardImage);
        holder.cardName.setText(cardName);

        setNames = new ArrayList<>();
        for(int i = 0; i < cards.size(); i++){
            setNames.add(cards.get(i).getSetName());
        }

        adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, setNames);

        holder.setNames.setAdapter(adapter);
        holder.setNames.setSelection(spinnerPosition);

        holder.setNames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int sPosition, long id) {
                Card card;

                card = cards.get(sPosition);
                Glide.with(mContext).load(card.getImageUrl()).into(holder.cardImage);
                mSpinnerPositionCache.put(card.getName(), sPosition);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.addCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnCardAddedListener != null) {
                    int position;

                    position = holder.setNames.getSelectedItemPosition();
                    mOnCardAddedListener.onCardAdded(cards.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCardsLists.size();
    }

    public void clearSpinnerPositionCache(){
        mSpinnerPositionCache.clear();
    }

    class SearchCardItemViewHolder extends RecyclerView.ViewHolder {
        ImageView cardImage;
        ImageButton addCardBtn;
        TextView cardName;
        Spinner setNames;

        SearchCardItemViewHolder(View itemView) {
            super(itemView);

            cardImage = (ImageView)itemView.findViewById(R.id.cardImage);
            cardName = (TextView) itemView.findViewById(R.id.cardName);
            setNames = (Spinner) itemView.findViewById(R.id.setNames);
            addCardBtn = (ImageButton) itemView.findViewById(R.id.actionAddCard);
        }
    }

    public void setOnCardAddedListener(OnCardAddedListener onCardAddedListener) {
        mOnCardAddedListener = onCardAddedListener;
    }

    public interface OnCardAddedListener {
        void onCardAdded(Card card);
    }
}


