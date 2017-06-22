package org.bdaoust.project7capstone.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.bdaoust.project7capstone.fragments.DecksFragment;
import org.bdaoust.project7capstone.tools.MTGKeys;
import org.bdaoust.project7capstone.fragments.DeckDetailsFragment;
import org.bdaoust.project7capstone.R;
import org.bdaoust.project7capstone.tools.MTGTools;

public class BaseDeckDetailsActivity extends AppCompatActivity implements DecksFragment.OnDeckDeletedListener{

    private ActionBar mActionBar;
    private DatabaseReference mReferenceDeckName;
    private ValueEventListener mOnDeckNameValueEventListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar;
        FirebaseDatabase firebaseDatabase;
        DatabaseReference referenceUserRoot;
        String firebaseUserId;
        String firebaseDeckKey;

        setContentView(R.layout.activity_deck_details);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();

        if(mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }

        firebaseUserId = getIntent().getStringExtra(MTGKeys.FIREBASE_USER_ID);
        firebaseDeckKey = getIntent().getStringExtra(MTGKeys.FIREBASE_DECK_KEY);
        firebaseDatabase = FirebaseDatabase.getInstance();
        referenceUserRoot = MTGTools.createUserRootReference(firebaseDatabase, firebaseUserId);
        mReferenceDeckName = MTGTools.createDeckNameReference(referenceUserRoot, firebaseDeckKey);

        createListeners();

        if(savedInstanceState == null){
            FragmentManager fragmentManager;
            FragmentTransaction fragmentTransaction;
            DeckDetailsFragment deckDetailsFragment;

            Bundle bundle;

            bundle = new Bundle();
            bundle.putString(MTGKeys.FIREBASE_USER_ID, firebaseUserId);
            bundle.putString(MTGKeys.FIREBASE_DECK_KEY, firebaseDeckKey);
            deckDetailsFragment = new DeckDetailsFragment();
            deckDetailsFragment.setArguments(bundle);

            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.deckDetailsContainer, deckDetailsFragment);
            fragmentTransaction.commit();
        }
    }

    private void createListeners(){
        mOnDeckNameValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String deckName;

                if(dataSnapshot.exists()) {
                    deckName = dataSnapshot.getValue(String.class);

                    if (mActionBar != null) {
                        mActionBar.setTitle(deckName);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();

        addListeners();
    }

    @Override
    protected void onPause() {
        super.onPause();

        removeListeners();
    }

    private void addListeners(){
        mReferenceDeckName.addValueEventListener(mOnDeckNameValueEventListener);
    }

    private void removeListeners(){
        mReferenceDeckName.removeEventListener(mOnDeckNameValueEventListener);
    }

    @Override
    public void onDeckDeleted(String firebaseUserId, String firebaseKey) {
        finish();
    }
}
