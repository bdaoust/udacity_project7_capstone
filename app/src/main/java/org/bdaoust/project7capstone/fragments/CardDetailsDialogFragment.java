package org.bdaoust.project7capstone.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
    private TextView mCardManaCostLabel;
    private TextView mCardManaCost;
    private TextView mCardCMC;
    private TextView mCardTypes;
    private TextView mCardOracleTextLabel;
    private TextView mCardOracleText;
    private TextView mCardFlavorTextLabel;
    private TextView mCardFlavorText;
    private TextView mCardPowerToughnessLabel;
    private TextView mCardPowerToughness;
    private TextView mCardLoyaltyLabel;
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
        mCardManaCostLabel = (TextView) rootView.findViewById(R.id.cardManaCostLabel);
        mCardManaCost = (TextView) rootView.findViewById(R.id.cardManaCost);
        mCardCMC = (TextView) rootView.findViewById(R.id.cardCMC);
        mCardTypes = (TextView) rootView.findViewById(R.id.cardTypes);
        mCardOracleTextLabel = (TextView) rootView.findViewById(R.id.cardOracleTextLabel);
        mCardOracleText = (TextView) rootView.findViewById(R.id.cardOracleText);
        mCardFlavorTextLabel = (TextView) rootView.findViewById(R.id.cardFlavorTextLabel);
        mCardFlavorText = (TextView) rootView.findViewById(R.id.cardFlavorText);
        mCardPowerToughnessLabel = (TextView) rootView.findViewById(R.id.cardPowerToughnessLabel);
        mCardPowerToughness = (TextView) rootView.findViewById(R.id.cardPowerToughness);
        mCardLoyaltyLabel = (TextView) rootView.findViewById(R.id.cardLoyaltyLabel);
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


                    if(mToolbar != null) {
                        mToolbar.setTitle(mtgCard.getName());
                    }

                    /* The card fields "CardImage", "Name", "Quantity", "CMC", "Types", "Set",
                     * and "Artist" should always be defined so we simply set the values. However,
                     * the fields "Mana Cost", "Oracle Text", "Flavor Text", "PowerToughness",
                     * and  "Loyalty" might be null so we only display them if they aren't null.
                     */
                    Glide.with(getContext()).load(mtgCard.getImageUrl()).into(mCardImage);
                    mCardName.setText(mtgCard.getName());
                    mCardQuantity.setText(String.valueOf(mtgCard.getNumbCopies()));
                    mCardCMC.setText(getPrettyCMC(mtgCard.getCmc()));
                    mCardTypes.setText(mtgCard.getType());
                    mCardSet.setText(mtgCard.getSetName());
                    mCardArtist.setText(mtgCard.getArtist());

                    if(mtgCard.getManaCost() != null){
                        mCardManaCost.setText(mtgCard.getManaCost());
                    } else {
                        mCardManaCostLabel.setVisibility(View.GONE);
                        mCardManaCost.setVisibility(View.GONE);
                    }

                    if(mtgCard.getText() != null) {
                        mCardOracleText.setText(mtgCard.getText());
                    } else {
                        mCardOracleTextLabel.setVisibility(View.GONE);
                        mCardOracleText.setVisibility(View.GONE);
                    }

                    if(mtgCard.getFlavorText() != null){
                        mCardFlavorText.setText(mtgCard.getFlavorText());
                    } else {
                        mCardFlavorTextLabel.setVisibility(View.GONE);
                        mCardFlavorText.setVisibility(View.GONE);
                    }

                    if(mtgCard.getPower() != null && mtgCard.getToughness() != null){
                        mCardPowerToughness.setText(mtgCard.getPower() + "/" + mtgCard.getToughness());
                    } else {
                        mCardPowerToughnessLabel.setVisibility(View.GONE);
                        mCardPowerToughness.setVisibility(View.GONE);
                    }

                    if(mtgCard.getLoyalty() > 0){
                        mCardLoyalty.setText(String.valueOf(mtgCard.getLoyalty()));
                    } else {
                        mCardLoyaltyLabel.setVisibility(View.GONE);
                        mCardLoyalty.setVisibility(View.GONE);
                    }
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
                    // Calling onBackPressed() instead of dismiss() because otherwise the state
                    // of the stack becomes inconsistent. For example, if a user is on a phone and
                    // clicks on the Up arrow to return to the Deck Details, followed by clicking on a
                    // card to get to the Card Details, followed by clicking on the Up arrow to return
                    // to the Deck Details once more, it will take more than one click on the Back
                    // Arrow (bottom of screen) to return to the Deck list, which is not what we want.
                    getActivity().onBackPressed();
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

        Log.v("AAA", "----------- On Create Called -------------");

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

        Log.v("AAA", "----------- On Destroy Called -------------");
    }

    private String getPrettyCMC(double cmc){
        double delta = 0.01;
        String prettyCMC;

        // Most values of CMC are integer values and we want to display them as integers, however we
        // also want to make sure that we account for special cases like the card
        // "Little Girl" (http://gatherer.wizards.com/Pages/Card/Details.aspx?multiverseid=74257)
        // which has a CMC of 0.5
        if(cmc - Math.floor(cmc) > delta){
            prettyCMC = String.valueOf(cmc);
        } else {
            prettyCMC = String.valueOf((int)cmc);
        }

        return prettyCMC;
    }
}
