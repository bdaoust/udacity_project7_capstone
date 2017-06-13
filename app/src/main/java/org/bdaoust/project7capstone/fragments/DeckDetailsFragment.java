package org.bdaoust.project7capstone.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.bdaoust.project7capstone.tools.MTGKeys;
import org.bdaoust.project7capstone.adapters.CardListAdapter;
import org.bdaoust.project7capstone.R;
import org.bdaoust.project7capstone.activities.EditDeckActivity;
import org.bdaoust.project7capstone.firebasemodels.MTGCardModel;
import org.bdaoust.project7capstone.firebasemodels.MTGDeckModel;
import org.bdaoust.project7capstone.tools.MTGTools;

import java.util.ArrayList;
import java.util.List;

public class DeckDetailsFragment extends Fragment{

    private boolean mIsLargeLayout;
    private String mFirebaseDeckKey;
    private DatabaseReference mReferenceDeck;
    private DatabaseReference mReferenceCards;
    private DatabaseReference mReferenceDeckLastUpdated;
    private MTGDeckModel mMTGDeck;
    private CardListAdapter mCardListAdapter;
    private ChildEventListener mOnCardsChildEventListener;
    private ValueEventListener mOnLastUpdatedValueEventListener;
    private ValueEventListener mOnDeckValueEventListener;
    private long mLastUpdatedTimestamp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView;
        RecyclerView recyclerView;
        GridLayoutManager gridLayoutManager;
        List<MTGCardModel> mtgCards;
        FirebaseDatabase firebaseDatabase;
        DatabaseReference referenceUserRoot;
        int numbColumns;

        rootView = inflater.inflate(R.layout.fragment_deck_details, container, false);
        numbColumns = getResources().getInteger(R.integer.card_list_column_count);
        gridLayoutManager = new GridLayoutManager(getContext(), numbColumns);

        mIsLargeLayout = getResources().getBoolean(R.bool.large_layout);
        mLastUpdatedTimestamp = 0;
        setHasOptionsMenu(true);

        mtgCards = new ArrayList<>();
        mMTGDeck = new MTGDeckModel();
        mMTGDeck.setMTGCards(mtgCards);
        mCardListAdapter = new CardListAdapter(getContext(), mMTGDeck);

        mCardListAdapter.setOnCardClickedListener(new CardListAdapter.OnCardClickedListener() {
            @Override
            public void onCardClicked(String firebaseCardKey) {
                showCardDetails(firebaseCardKey);
            }
        });

        recyclerView = (RecyclerView) rootView.findViewById(R.id.cardsList);
        recyclerView.setAdapter(mCardListAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);

        firebaseDatabase = FirebaseDatabase.getInstance();
        referenceUserRoot = MTGTools.createUserRootReference(firebaseDatabase, null);
        mFirebaseDeckKey = getArguments().getString(MTGKeys.FIREBASE_DECK_KEY);
        mReferenceDeck = MTGTools.createDeckReference(referenceUserRoot, mFirebaseDeckKey);
        mReferenceCards = MTGTools.createCardListReference(referenceUserRoot, mFirebaseDeckKey);
        mReferenceDeckLastUpdated = MTGTools.createDeckLastUpdatedReference(referenceUserRoot, mFirebaseDeckKey);

        createListeners();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        addListeners();
    }

    @Override
    public void onPause() {
        super.onPause();

        // We clear the list of MTG cards, otherwise when we return to this Fragment, assuming it
        // hasn't been recreated, all of the MTG cards contained in the Deck will be added again
        // to the list, thus creating extra copies of MTG cards, which we don't want.
        mMTGDeck.getMTGCards().clear();
        mCardListAdapter.notifyDataSetChanged();
        mLastUpdatedTimestamp = 0;
        removeListeners();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_deck_details, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_edit:
                Intent intent;

                intent = new Intent(getContext(), EditDeckActivity.class);
                intent.putExtra(MTGKeys.FIREBASE_DECK_KEY, mFirebaseDeckKey);
                startActivity(intent);

                return true;
            case R.id.action_delete:
                mReferenceDeck.removeValue();
                ((DecksFragment.OnDeckDeletedListener)getActivity()).onDeckDeleted(mFirebaseDeckKey);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showCardDetails(String firebaseCardKey){
        CardDetailsDialogFragment cardDetailsDialogFragment;
        FragmentManager fragmentManager;
        Bundle bundle;

        bundle = new Bundle();
        bundle.putString(MTGKeys.FIREBASE_DECK_KEY, mFirebaseDeckKey);
        bundle.putString(MTGKeys.FIREBASE_CARD_KEY, firebaseCardKey);
        cardDetailsDialogFragment = new CardDetailsDialogFragment();
        cardDetailsDialogFragment.setArguments(bundle);

        fragmentManager = getFragmentManager();

        if(mIsLargeLayout){
            cardDetailsDialogFragment.show(fragmentManager, "CardDetails");
        } else {
            FragmentTransaction fragmentTransaction;

            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.add(android.R.id.content, cardDetailsDialogFragment)
                .addToBackStack(null)
                .commit();
        }

    }

    private void createListeners() {
        mOnCardsChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MTGCardModel mtgCard;

                mtgCard = dataSnapshot.getValue(MTGCardModel.class);
                mtgCard.setFirebaseKey(dataSnapshot.getKey());
                mMTGDeck.addCard(mtgCard);
                mCardListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        mOnLastUpdatedValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long lastUpdatedTimestamp;

                if(dataSnapshot.exists()) {
                    lastUpdatedTimestamp = dataSnapshot.getValue(Long.class);

                    // It is possible that a user could have the current Deck open for editing on a different device.
                    // If the current Deck does get updated on a different device then we can monitor that even by looking
                    // for a change in the lastUpdatedTimestamp. We ignore the case where mLastUpdatedTimestamp is 0 because
                    // that means either this Fragment is just being created or we are resuming back to this Fragment from
                    // the EditDeckActivity (i.e. mLastUpdatedTimestamp is also set to 0 in onPause). Once we know that the Deck
                    // was updated on a different device, we clear the list of MTGCards, remove and re-add the
                    // mOnCardsChildEventListener to repopulate the list and then notify the CardListAdapter.
                    if (mLastUpdatedTimestamp != 0 && lastUpdatedTimestamp > mLastUpdatedTimestamp) {
                        mMTGDeck.getMTGCards().clear();
                        mReferenceCards.removeEventListener(mOnCardsChildEventListener);
                        mReferenceCards.addChildEventListener(mOnCardsChildEventListener);
                        mCardListAdapter.notifyDataSetChanged();
                    }

                    mLastUpdatedTimestamp = lastUpdatedTimestamp;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        mOnDeckValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    ((DecksFragment.OnDeckDeletedListener) getActivity()).onDeckDeleted(mFirebaseDeckKey);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

    }

    private void addListeners(){
        mReferenceCards.addChildEventListener(mOnCardsChildEventListener);
        mReferenceDeckLastUpdated.addValueEventListener(mOnLastUpdatedValueEventListener);
        mReferenceDeck.addValueEventListener(mOnDeckValueEventListener);
    }

    private void removeListeners(){
        mReferenceCards.removeEventListener(mOnCardsChildEventListener);
        mReferenceDeckLastUpdated.removeEventListener(mOnLastUpdatedValueEventListener);
        mReferenceDeck.removeEventListener(mOnDeckValueEventListener);
    }
}