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
import org.bdaoust.project7capstone.firebasemodels.MTGCardModel;
import org.bdaoust.project7capstone.firebasemodels.MTGDeckModel;
import org.bdaoust.project7capstone.network.InitSampleDeckService;
import org.bdaoust.project7capstone.tools.MTGTools;

import java.util.ArrayList;
import java.util.List;

public class DecksFragment extends Fragment{

    private RecyclerView mRecyclerView;
    private CreateDeckDialogFragment mCreateDeckDialogFragment;
    private View mEmptyDeckListView;
    private int mSelectedPosition = 0;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mReferenceUserRoot;
    private DatabaseReference mReferenceSampleDeckWasSaved;
    private DatabaseReference mReferenceDecks;
    private List<MTGDeckModel> mMTGDecks;
    private DeckListAdapter mDeckListAdapter;
    private ProgressBar mProgressBar;
    private View mRootView;
    private SampleDeckDownloadFailedBroadcastReceiver mSampleDeckDownloadFailedBroadcastReceiver;
    private boolean mDownloadSampleDeckWhenActivityCreated = false;
    private boolean mIsFirstDeckAdded;
    private static final String SELECTED_KEY = "selected_position";
    private static final String TAG = "DecksFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FloatingActionButton createDeckFAB;

        mRootView = inflater.inflate(R.layout.fragment_decks, container, false);
        mEmptyDeckListView = mRootView.findViewById(R.id.emptyDeckList);
        mProgressBar = (ProgressBar) mRootView.findViewById(R.id.progressBar);

        mMTGDecks = new ArrayList<>();
        mIsFirstDeckAdded = true;

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mReferenceUserRoot = MTGTools.createUserRootReference(mFirebaseDatabase, null);
        mReferenceSampleDeckWasSaved = MTGTools.createSampleDeckWasSavedReference(mReferenceUserRoot);
        mReferenceDecks = MTGTools.createDeckListReference(mReferenceUserRoot);

        mReferenceDecks.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MTGDeckModel mtgDeckModel;

                mtgDeckModel = dataSnapshot.getValue(MTGDeckModel.class);
                mtgDeckModel.setFirebaseKey(dataSnapshot.getKey());
                if(mtgDeckModel.getMTGCards() == null){
                    mtgDeckModel.setMTGCards(new ArrayList<MTGCardModel>());
                }
                mMTGDecks.add(mtgDeckModel);
                mDeckListAdapter.notifyDataSetChanged();

                if(mEmptyDeckListView.getVisibility() == View.VISIBLE){
                    mEmptyDeckListView.setVisibility(View.GONE);
                }

                if(mProgressBar.getVisibility() == View.VISIBLE){
                    mProgressBar.setVisibility(View.GONE);
                }

                if(mIsFirstDeckAdded) {
                    ((OnFirstDeckAddedListener)getActivity()).onFirstDeckAdded(dataSnapshot.getKey());
                    mIsFirstDeckAdded = false;
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
        });

        if(!isConnected()){
            mProgressBar.setVisibility(View.GONE);
            Toast.makeText(getContext(), R.string.no_network_connection, Toast.LENGTH_SHORT).show();
        }

        // Solution for keeping track of the selected position is based on
        // Project Sunshine (https://github.com/udacity/Sunshine-Version-2/blob/sunshine_master/app/src/main/java/com/example/android/sunshine/app/ForecastFragment.java)
        if(savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)){
            mSelectedPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.deckList);
        mDeckListAdapter = new DeckListAdapter(getContext(), mMTGDecks, mSelectedPosition);
        mDeckListAdapter.setOnDeckSelectedListener(new OnDeckSelectedListener() {
            @Override
            public void onDeckSelected(String firebaseKey, int position) {
                mSelectedPosition = position;
                ((OnDeckSelectedListener)getActivity()).onDeckSelected(firebaseKey, position);
            }
        });

        mRecyclerView.setAdapter(mDeckListAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mCreateDeckDialogFragment = new CreateDeckDialogFragment();

        createDeckFAB = (FloatingActionButton) mRootView.findViewById(R.id.createDeckFAB);
        createDeckFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCreateDeckDialogFragment.show(getFragmentManager(), "CreateDeck");
            }
        });

        return mRootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_KEY, mSelectedPosition);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSampleDeckDownloadFailedBroadcastReceiver = new SampleDeckDownloadFailedBroadcastReceiver();
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter intentFilter;

        intentFilter = new IntentFilter("org.bdaoust.project7capstone.NOTIFY_SAMPLE_DECK_DOWNLOAD_FAILED");
        getActivity().registerReceiver(mSampleDeckDownloadFailedBroadcastReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();

        getActivity().unregisterReceiver(mSampleDeckDownloadFailedBroadcastReceiver);
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
        void onDeckDeleted(String firstDeckFirebaseKey);
    }
}
