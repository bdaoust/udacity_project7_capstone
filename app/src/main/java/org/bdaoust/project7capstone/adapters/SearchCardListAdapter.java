package org.bdaoust.project7capstone.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.bdaoust.project7capstone.R;
import org.bdaoust.project7capstone.data.Deck;
import org.bdaoust.project7capstone.data.SampleDeck;


public class SearchCardListAdapter extends RecyclerView.Adapter<SearchCardListAdapter.SearchCardItemViewHolder>{

    private Context mContext;
    private Deck mSampleDeck;

    public SearchCardListAdapter(Context context){
        mContext = context;
        mSampleDeck = new SampleDeck();
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
    public void onBindViewHolder(SearchCardItemViewHolder holder, int position) {

        Glide.with(mContext).load(mSampleDeck.getCards().get(0).getImageUrl()).into(holder.cardImage);
        holder.cardName.setText(mSampleDeck.getCards().get(0).getName());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext,
                R.array.sample_set_names, android.R.layout.simple_spinner_item);

        holder.setNames.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return 12;
    }

    class SearchCardItemViewHolder extends RecyclerView.ViewHolder {
        ImageView cardImage;
        TextView cardName;
        Spinner setNames;

        SearchCardItemViewHolder(View itemView) {
            super(itemView);

            cardImage = (ImageView)itemView.findViewById(R.id.cardImage);
            cardName = (TextView) itemView.findViewById(R.id.cardName);
            setNames = (Spinner) itemView.findViewById(R.id.setNames);
        }
    }
}


