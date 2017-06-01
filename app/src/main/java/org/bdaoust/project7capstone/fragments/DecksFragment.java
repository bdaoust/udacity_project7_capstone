package org.bdaoust.project7capstone.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.bdaoust.project7capstone.adapters.DeckListAdapter;
import org.bdaoust.project7capstone.R;
import org.bdaoust.project7capstone.firebasemodels.MTGDeckModel;
import org.bdaoust.project7capstone.network.InitSampleDeckService;

import java.util.ArrayList;
import java.util.List;

public class DecksFragment extends Fragment{

    private RecyclerView mRecyclerView;
    private CreateDeckDialogFragment mCreateDeckDialogFragment;
    private View mEmptyDeckListView;
    private int mSelectedPosition = 0;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mReferenceRoot;
    private DatabaseReference mReferenceSampleDeckWasSaved;
    private DatabaseReference mReferenceDecks;
    private List<MTGDeckModel> mMTGDecks;
    private DeckListAdapter mDeckListAdapter;
    private ProgressBar mProgressBar;

    private static final String SELECTED_KEY = "selected_position";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView;
        FloatingActionButton createDeckFAB;

        rootView = inflater.inflate(R.layout.fragment_decks, container, false);
        mEmptyDeckListView = rootView.findViewById(R.id.emptyDeckList);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        mMTGDecks = new ArrayList<>();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mReferenceRoot = mFirebaseDatabase.getReference();
        mReferenceSampleDeckWasSaved = mReferenceRoot.child("sampleDeckWasSaved");
        mReferenceDecks = mReferenceRoot.child("decks");

        mReferenceDecks.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MTGDeckModel mtgDeckModel;

                mtgDeckModel = dataSnapshot.getValue(MTGDeckModel.class);
                mtgDeckModel.setFirebaseKey(dataSnapshot.getKey());
                mMTGDecks.add(mtgDeckModel);
                mDeckListAdapter.notifyDataSetChanged();

                if(mEmptyDeckListView.getVisibility() == View.VISIBLE){
                    mEmptyDeckListView.setVisibility(View.GONE);
                }

                if(mProgressBar.getVisibility() == View.VISIBLE){
                    mProgressBar.setVisibility(View.GONE);
                }
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

        mReferenceSampleDeckWasSaved.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Log.d("DecksFragment","Sample Deck was already saved!");

                    if(mMTGDecks.size() == 0){
                        mEmptyDeckListView.setVisibility(View.VISIBLE);
                    }
                } else {
                    Intent initSampleDeck;

                    initSampleDeck = new Intent(getActivity(), InitSampleDeckService.class);
                    getActivity().startService(initSampleDeck);

                    mProgressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        // Solution for keeping track of the selected position is based on
        // Project Sunshine (https://github.com/udacity/Sunshine-Version-2/blob/sunshine_master/app/src/main/java/com/example/android/sunshine/app/ForecastFragment.java)
        if(savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)){
            mSelectedPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.deckList);
        mDeckListAdapter = new DeckListAdapter(getContext(), mMTGDecks, mSelectedPosition);
        mDeckListAdapter.setOnDeckClickedListener(new DeckListAdapter.OnDeckClickedListener() {
            @Override
            public void onDeckClicked(int deckId) {
                ((DecksFragment.OnDeckSelectedListener)getActivity()).onDeckSelected(deckId);
            }
        });

        mRecyclerView.setAdapter(mDeckListAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mCreateDeckDialogFragment = new CreateDeckDialogFragment();

        createDeckFAB = (FloatingActionButton) rootView.findViewById(R.id.createDeckFAB);
        createDeckFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCreateDeckDialogFragment.show(getFragmentManager(), "CreateDeck");
            }
        });

        return rootView;
    }

    public interface OnDeckSelectedListener {
        void onDeckSelected(int someDeckId);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_KEY, mSelectedPosition);

        super.onSaveInstanceState(outState);
    }
}
