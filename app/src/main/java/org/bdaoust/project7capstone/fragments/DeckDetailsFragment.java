package org.bdaoust.project7capstone.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import org.bdaoust.project7capstone.MTGKeys;
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
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mReferenceUserRoot;
    private DatabaseReference mReferenceCards;
    private MTGDeckModel mMTGDeck;
    private List<MTGCardModel> mMTGCards;
    private CardListAdapter mCardListAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView;
        RecyclerView recyclerView;
        GridLayoutManager gridLayoutManager;
        int numbColumns;

        rootView = inflater.inflate(R.layout.fragment_deck_details, container, false);
        numbColumns = getResources().getInteger(R.integer.card_list_column_count);
        gridLayoutManager = new GridLayoutManager(getContext(), numbColumns);

        mMTGCards = new ArrayList<>();
        mMTGDeck = new MTGDeckModel();
        mMTGDeck.setMTGCards(mMTGCards);
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

        mIsLargeLayout = getResources().getBoolean(R.bool.large_layout);

        mFirebaseDeckKey = getArguments().getString(MTGKeys.FIREBASE_DECK_KEY);
        Log.d("DeckDetailsFragment", "----------- The Deck to load is " + mFirebaseDeckKey + " ---------------");

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mReferenceUserRoot = MTGTools.createUserRootReference(mFirebaseDatabase, null);
        mReferenceCards = MTGTools.createCardListReference(mReferenceUserRoot, mFirebaseDeckKey);

        mReferenceCards.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MTGCardModel mtgCard;

                mtgCard = dataSnapshot.getValue(MTGCardModel.class);
                mtgCard.setFirebaseKey(dataSnapshot.getKey());
                mMTGCards.add(mtgCard);
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
        });

        setHasOptionsMenu(true);

        return rootView;
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
}
