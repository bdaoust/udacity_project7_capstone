package org.bdaoust.project7capstone.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import org.bdaoust.project7capstone.MTGKeys;
import org.bdaoust.project7capstone.fragments.DeckDetailsFragment;
import org.bdaoust.project7capstone.fragments.DecksFragment;
import org.bdaoust.project7capstone.R;

public class BaseMainActivity extends AppCompatActivity implements DecksFragment.OnDeckSelectedListener , DecksFragment.OnFirstDeckAddedListener{

    private boolean mIsLargeLayout;
    private boolean mIsActivityInitialCreation;
    private final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toolbar toolbar;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIsLargeLayout = getResources().getBoolean(R.bool.large_layout);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mIsActivityInitialCreation = savedInstanceState == null;
    }

    private void loadDetailFragment(String firebaseKey){
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        DeckDetailsFragment deckDetailsFragment;
        Bundle bundle;

        bundle = new Bundle();
        bundle.putString(MTGKeys.FIREBASE_DECK_KEY, firebaseKey);
        deckDetailsFragment = new DeckDetailsFragment();
        deckDetailsFragment.setArguments(bundle);
        Log.d(TAG, "Loading Deck: " + firebaseKey);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.deckDetailsContainer, deckDetailsFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onDeckSelected(String firebaseKey, int position) {
        if(mIsLargeLayout){
            loadDetailFragment(firebaseKey);
        } else {
            Intent intent;

            intent = new Intent(this, DeckDetailsActivity.class);
            intent.putExtra(MTGKeys.FIREBASE_DECK_KEY, firebaseKey);
            Log.d(TAG, "Loading Deck: " + firebaseKey);
            startActivity(intent);
        }
    }

    @Override
    public void onFirstDeckAdded(String firebaseKey) {
        if(mIsLargeLayout && mIsActivityInitialCreation) {
            Log.d(TAG, "Initial Activity Creation... Loading First Deck");
            loadDetailFragment(firebaseKey);
        }
    }

    protected boolean isLargeLayout(){
        return mIsLargeLayout;
    }
}
