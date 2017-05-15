package org.bdaoust.project7capstone.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.bdaoust.project7capstone.data.Deck;
import org.bdaoust.project7capstone.R;
import org.bdaoust.project7capstone.data.SampleDeck;

import java.util.ArrayList;

import io.magicthegathering.javasdk.resource.Card;

public class CardDetailsDialogFragment extends DialogFragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView;
        Deck deck;
        ArrayList<Card> cards;
        Card card;
        Toolbar toolbar;

        ImageView cardImage;
        TextView cardName;
        TextView cardQuantity;
        TextView cardCMC;
        TextView cardTypes;
        TextView cardOracleText;
        TextView cardPowerToughness;
        TextView cardLoyalty;
        TextView cardExpansion;
        TextView cardArtist;

        rootView = inflater.inflate(R.layout.fragment_card_details_dialog, container, false);

        cardImage = (ImageView) rootView.findViewById(R.id.cardImage);
        cardName = (TextView) rootView.findViewById(R.id.cardName);
        cardQuantity = (TextView) rootView.findViewById(R.id.cardQuantity);
        cardCMC = (TextView) rootView.findViewById(R.id.cardCMC);
        cardTypes = (TextView) rootView.findViewById(R.id.cardTypes);
        cardOracleText = (TextView) rootView.findViewById(R.id.cardOracleText);
        cardPowerToughness = (TextView) rootView.findViewById(R.id.cardPowerToughness);
        cardLoyalty = (TextView) rootView.findViewById(R.id.cardLoyalty);
        cardExpansion = (TextView) rootView.findViewById(R.id.cardExpansion);
        cardArtist = (TextView) rootView.findViewById(R.id.cardArtist);

        deck = new SampleDeck();
        cards = deck.getCards();
        card = cards.get(0);

        Glide.with(getContext()).load(card.getImageUrl()).into(cardImage);
        cardName.setText(card.getName());
        cardQuantity.setText(String.valueOf(deck.getNumbCopies(card.getMultiverseid())));
        cardCMC.setText(String.valueOf(card.getCmc()));
        cardTypes.setText(card.getType());
        cardOracleText.setText(card.getText());
        cardPowerToughness.setText(card.getPower() + "/" + card.getToughness());
        cardLoyalty.setText("This will be Null???");
        cardExpansion.setText(card.getSetName());
        cardArtist.setText(card.getArtist());

        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        if(toolbar != null){
            toolbar.setTitle(card.getName());
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }

        return rootView;
    }
}
