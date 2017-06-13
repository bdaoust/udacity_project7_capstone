package org.bdaoust.project7capstone.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import org.bdaoust.project7capstone.tools.MTGTools;

import java.util.ArrayList;
import java.util.List;

public class DecksFragment extends Fragment{

    private CreateDeckDialogFragment mCreateDeckDialogFragment;
    private View mEmptyDeckListView;
    private int mSelectedPosition = 0;
    private DatabaseReference mReferenceSampleDeckWasSaved;
    private DatabaseReference mReferenceDecks;
    private ChildEventListener mOnDecksChildEventListener;
    private ValueEventListener mOnSampleDeckWasSavedValueEventListener;
    private List<MTGDeckModel> mMTGDecks;
    private DeckListAdapter mDeckListAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private View mRootView;
    private SampleDeckDownloadFailedBroadcastReceiver mSampleDeckDownloadFailedBroadcastReceiver;
    private String mNewlyCreatedDeckFirebaseKey;
    private boolean mDownloadSampleDeckWhenActivityCreated = false;
    private boolean mIsFirstDeckAdded;
    private static final String SELECTED_KEY = "selected_position";
    private static final String TAG = "DecksFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FloatingActionButton createDeckFAB;
        FirebaseDatabase firebaseDatabase;
        DatabaseReference referenceUserRoot;

        mRootView = inflater.inflate(R.layout.fragment_decks, container, false);
        mEmptyDeckListView = mRootView.findViewById(R.id.emptyDeckList);
        mProgressBar = (ProgressBar) mRootView.findViewById(R.id.progressBar);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.deckList);

        mIsFirstDeckAdded = true;
        mSampleDeckDownloadFailedBroadcastReceiver = new SampleDeckDownloadFailedBroadcastReceiver();

        // Solution for keeping track of the selected position is based on
        // Project Sunshine (https://github.com/udacity/Sunshine-Version-2/blob/sunshine_master/app/src/main/java/com/example/android/sunshine/app/ForecastFragment.java)
        if(savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)){
            mSelectedPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        mMTGDecks = new ArrayList<>();
        mDeckListAdapter = new DeckListAdapter(getContext(), mMTGDecks, mSelectedPosition);
        mDeckListAdapter.setOnDeckSelectedListener(new OnDeckSelectedListener() {
            @Override
            public void onDeckSelected(String firebaseKey, int position) {
                mSelectedPosition = position;
                ((OnDeckSelectedListener)getActivity()).onDeckSelected(firebaseKey, position);
                mRecyclerView.scrollToPosition(position);
            }
        });
        mRecyclerView.setAdapter(mDeckListAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mCreateDeckDialogFragment = new CreateDeckDialogFragment();
        mCreateDeckDialogFragment.setOnDeckCreatedListener(new OnDeckCreatedListener() {
            @Override
            public void onDeckCreated(String firebaseKey) {
                mNewlyCreatedDeckFirebaseKey = firebaseKey;
            }
        });

        createDeckFAB = (FloatingActionButton) mRootView.findViewById(R.id.createDeckFAB);
        createDeckFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCreateDeckDialogFragment.show(getFragmentManager(), "CreateDeck");
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        referenceUserRoot = MTGTools.createUserRootReference(firebaseDatabase, null);
        mReferenceSampleDeckWasSaved = MTGTools.createSampleDeckWasSavedReference(referenceUserRoot);
        mReferenceDecks = MTGTools.createDeckListReference(referenceUserRoot);

        createListeners();

        return mRootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_KEY, mSelectedPosition);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter intentFilter;

        intentFilter = new IntentFilter("org.bdaoust.project7capstone.NOTIFY_SAMPLE_DECK_DOWNLOAD_FAILED");
        getActivity().registerReceiver(mSampleDeckDownloadFailedBroadcastReceiver, intentFilter);

        if(!isConnected()){
            mProgressBar.setVisibility(View.GONE);
            Toast.makeText(getContext(), R.string.no_network_connection, Toast.LENGTH_SHORT).show();
        }

        addListeners();
    }

    @Override
    public void onPause() {
        super.onPause();

        getActivity().unregisterReceiver(mSampleDeckDownloadFailedBroadcastReceiver);

        mMTGDecks.clear();
        mDeckListAdapter.notifyDataSetChanged();

        removeListeners();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(mDownloadSampleDeckWhenActivityCreated){
            downloadSampleDeck();
            mDownloadSampleDeckWhenActivityCreated = false;
        }
    }

    private class SampleDeckDownloadFailedBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Snackbar snackbar;

            mProgressBar.setVisibility(View.GONE);
            if(mMTGDecks.size() == 0) {
                mEmptyDeckListView.setVisibility(View.VISIBLE);
            }

            snackbar = Snackbar.make(mRootView, R.string.failed_to_download_sample_deck, Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(R.string.action_retry, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEmptyDeckListView.setVisibility(View.GONE);
                    downloadSampleDeck();
                }
            });
            snackbar.show();
        }
    }

    private void downloadSampleDeck(){
        Intent initSampleDeck;

        if(getContext() != null) {
            initSampleDeck = new Intent(getContext(), InitSampleDeckService.class);
            getActivity().startService(initSampleDeck);
        } else {
            mDownloadSampleDeckWhenActivityCreated = true;
        }

        mProgressBar.setVisibility(View.VISIBLE);
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager;
        NetworkInfo activeNetwork;

        connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = connectivityManager.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void createListeners() {
        mOnDecksChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MTGDeckModel mtgDeck;
                int position;

                mtgDeck = dataSnapshot.getValue(MTGDeckModel.class);
                mtgDeck.setFirebaseKey(dataSnapshot.getKey());

                mMTGDecks.add(mtgDeck);
                position = mDeckListAdapter.findMTGDeckPositionByFirebaseKey(mtgDeck.getFirebaseKey());
                mDeckListAdapter.notifyItemInserted(position);

                if(mtgDeck.getFirebaseKey().equals(mNewlyCreatedDeckFirebaseKey)){
                    if(position != -1) {
                        mDeckListAdapter.selectDeck(position);
                        mNewlyCreatedDeckFirebaseKey = "";
                    }
                }

                mEmptyDeckListView.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.GONE);

                if(mIsFirstDeckAdded) {
                    ((OnFirstDeckAddedListener)getActivity()).onFirstDeckAdded(dataSnapshot.getKey());
                    mIsFirstDeckAdded = false;
                }

                if(position == mSelectedPosition){
                    mRecyclerView.scrollToPosition(position);
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                MTGDeckModel updatedMTGDeck;
                int position;

                updatedMTGDeck = dataSnapshot.getValue(MTGDeckModel.class);
                updatedMTGDeck.setFirebaseKey(dataSnapshot.getKey());
                position = mDeckListAdapter.findMTGDeckPositionByFirebaseKey(updatedMTGDeck.getFirebaseKey());

                if(position != -1){
                    mMTGDecks.set(position, updatedMTGDeck);
                    mDeckListAdapter.notifyItemChanged(position);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                MTGDeckModel deletedMTGDeck;
                int position;

                deletedMTGDeck = dataSnapshot.getValue(MTGDeckModel.class);
                deletedMTGDeck.setFirebaseKey(dataSnapshot.getKey());
                position = mDeckListAdapter.findMTGDeckPositionByFirebaseKey(deletedMTGDeck.getFirebaseKey());

                if(position != -1){
                    mMTGDecks.remove(position);
                    mDeckListAdapter.notifyItemRemoved(position);
                }

                ((DecksFragment.OnDeckDeletedListener)getActivity()).onDeckDeleted(deletedMTGDeck.getFirebaseKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        mOnSampleDeckWasSavedValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Log.d(TAG,"Sample Deck was already saved!");

                    if(mMTGDecks.size() == 0){
                        mEmptyDeckListView.setVisibility(View.VISIBLE);
                    }
                } else if(isConnected()){
                    downloadSampleDeck();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
    }

    private void addListeners(){
        mReferenceDecks.addChildEventListener(mOnDecksChildEventListener);
        mReferenceSampleDeckWasSaved.addListenerForSingleValueEvent(mOnSampleDeckWasSavedValueEventListener);
    }

    private void removeListeners(){
        mReferenceDecks.removeEventListener(mOnDecksChildEventListener);
        mReferenceSampleDeckWasSaved.removeEventListener(mOnSampleDeckWasSavedValueEventListener);
    }

    public interface OnFirstDeckAddedListener {
        void onFirstDeckAdded(String firebaseKey);
    }

    public interface OnDeckCreatedListener {
        void onDeckCreated(String firebaseKey);
    }

    public interface OnDeckSelectedListener {
        void onDeckSelected(String firebaseKey, int position);
    }

    public interface OnDeckDeletedListener {
        void onDeckDeleted(String firebaseKey);
    }
}
