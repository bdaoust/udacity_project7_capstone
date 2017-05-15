package org.bdaoust.project7capstone;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class SearchCardsDialogFragment extends DialogFragment{

    private RecyclerView mRecyclerView;
    private EditText mInputCardName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView;

        rootView = inflater.inflate(R.layout.fragment_search_cards_dialog, container, false);

        mInputCardName = (EditText) rootView.findViewById(R.id.inputCardName);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.searchCardsList);
        mRecyclerView.setAdapter(new CustomAdapter());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        return rootView;
    }

    private class CustomAdapter extends RecyclerView.Adapter<CustomViewHolder>{

        private Deck mSampleDeck;

        public CustomAdapter(){
            mSampleDeck = new SampleDeck();
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            CustomViewHolder customViewHolder;
            View view;

            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_search_card, parent, false);

            customViewHolder = new CustomViewHolder(view);

            return customViewHolder;
        }

        @Override
        public void onBindViewHolder(CustomViewHolder holder, int position) {

            Glide.with(getContext()).load(mSampleDeck.getCards().get(0).getImageUrl()).into(holder.cardImage);
            holder.cardName.setText(mSampleDeck.getCards().get(0).getName());

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.sample_set_names, android.R.layout.simple_spinner_item);

            holder.setNames.setAdapter(adapter);
        }

        @Override
        public int getItemCount() {
            return 12;
        }
    }

    private class CustomViewHolder extends RecyclerView.ViewHolder{
        ImageView cardImage;
        TextView cardName;
        Spinner setNames;

        public CustomViewHolder(View itemView) {
            super(itemView);

            cardImage = (ImageView)itemView.findViewById(R.id.cardImage);
            cardName = (TextView) itemView.findViewById(R.id.cardName);
            setNames = (Spinner) itemView.findViewById(R.id.setNames);
        }
    }

}
