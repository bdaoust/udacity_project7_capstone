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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.bdaoust.project7capstone.MTGKeys;
import org.bdaoust.project7capstone.R;
import org.bdaoust.project7capstone.firebasemodels.MTGCardModel;

public class CardDetailsDialogFragment extends DialogFragment{

    private Toolbar mToolbar;
    private ImageView mCardImage;
    private TextView mCardName;
    private TextView mCardQuantity;
    private TextView mCardCMC;
    private TextView mCardTypes;
    private TextView mCardOracleText;
    private TextView mCardFlavorText;
    private TextView mCardPowerToughness;
    private TextView mCardLoyalty;
    private TextView mCardSet;
    private TextView mCardArtist;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView;
        FirebaseDatabase firebaseDatabase;
        DatabaseReference referenceCard;
        String firebaseDeckKey;
        String firebaseCardKey;

        rootView = inflater.inflate(R.layout.fragment_card_details_dialog, container, false);
        mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);

        mCardImage = (ImageView) rootView.findViewById(R.id.cardImage);
        mCardName = (TextView) rootView.findViewById(R.id.cardName);
        mCardQuantity = (TextView) rootView.findViewById(R.id.cardQuantity);
        mCardCMC = (TextView) rootView.findViewById(R.id.cardCMC);
        mCardTypes = (TextView) rootView.findViewById(R.id.cardTypes);
        mCardOracleText = (TextView) rootView.findViewById(R.id.cardOracleText);
        mCardFlavorText = (TextView) rootView.findViewById(R.id.cardFlavorText);
        mCardPowerToughness = (TextView) rootView.findViewById(R.id.cardPowerToughness);
        mCardLoyalty = (TextView) rootView.findViewById(R.id.cardLoyalty);
        mCardSet = (TextView) rootView.findViewById(R.id.cardSet);
        mCardArtist = (TextView) rootView.findViewById(R.id.cardArtist);

        firebaseDeckKey = getArguments().getString(MTGKeys.FIREBASE_DECK_KEY);
        firebaseCardKey = getArguments().getString(MTGKeys.FIREBASE_CARD_KEY);

        if(firebaseDeckKey != null && firebaseCardKey != null) {
            firebaseDatabase = FirebaseDatabase.getInstance();
            referenceCard = firebaseDatabase.getReference().child("decks").
                    child(firebaseDeckKey).child("mtgcardModels").child(firebaseCardKey);

            referenceCard.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    MTGCardModel mtgCard;

                    mtgCard = dataSnapshot.getValue(MTGCardModel.class);


                    mToolbar.setTitle(mtgCard.getName());

                    Glide.with(getContext()).load(mtgCard.getImageUrl()).into(mCardImage);
                    mCardName.setText(mtgCard.getName());
                    mCardQuantity.setText(String.valueOf(mtgCard.getNumbCopies()));
                    mCardCMC.setText(String.valueOf(mtgCard.getCmc()));
                    mCardTypes.setText(mtgCard.getType());
                    mCardOracleText.setText(mtgCard.getText());
                    mCardFlavorText.setText(mtgCard.getFlavorText());
                    mCardPowerToughness.setText(mtgCard.getPower() + "/" + mtgCard.getToughness());
                    mCardLoyalty.setText(String.valueOf(mtgCard.getLoyalty()));
                    mCardSet.setText(mtgCard.getSetName());
                    mCardArtist.setText(mtgCard.getArtist());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


        if(mToolbar != null){
            mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
            mToolbar.setNavigationContentDescription(R.string.action_up);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            // Hide the deckList from the DeckDetailsFragment (on phones) since it can be otherwise
            // still be "seen" when navigating the app with TalkBack.
            View deckList;

            deckList = getActivity().findViewById(R.id.cardsList);
            if(deckList != null){
                deckList.setVisibility(View.INVISIBLE);
            }

        }

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        View deckList;

        // The CardDetailsDialogFragment is no longer visible, so we can show the deckList view
        deckList = getActivity().findViewById(R.id.cardsList);
        if(deckList !=null && (deckList.getVisibility() == View.INVISIBLE)) {
            deckList.setVisibility(View.VISIBLE);
        }
    }
}
