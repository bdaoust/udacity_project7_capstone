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

public class BaseDeckDetailsActivity extends AppCompatActivity implements DecksFragment.OnDeckDeletedListener{

    private ActionBar mActionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar;
        FirebaseDatabase firebaseDatabase;
        DatabaseReference referenceRoot;
        DatabaseReference referenceDeckName;
        String firebaseDeckKey;

        setContentView(R.layout.activity_deck_details);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();

        if(mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }

        firebaseDeckKey = getIntent().getStringExtra(MTGKeys.FIREBASE_DECK_KEY);
        firebaseDatabase = FirebaseDatabase.getInstance();
        referenceRoot = firebaseDatabase.getReference();
        referenceDeckName = referenceRoot.child("decks").child(firebaseDeckKey).child("name");

        referenceDeckName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String deckName;

                deckName = dataSnapshot.getValue(String.class);

                if(mActionBar != null){
                    mActionBar.setTitle(deckName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(savedInstanceState == null){
            FragmentManager fragmentManager;
            FragmentTransaction fragmentTransaction;
            DeckDetailsFragment deckDetailsFragment;

            Bundle bundle;

            bundle = new Bundle();
            bundle.putString(MTGKeys.FIREBASE_DECK_KEY, firebaseDeckKey);
            deckDetailsFragment = new DeckDetailsFragment();
            deckDetailsFragment.setArguments(bundle);

            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.deckDetailsContainer, deckDetailsFragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onDeckDeleted(String firebaseKey) {
        finish();
    }
}
