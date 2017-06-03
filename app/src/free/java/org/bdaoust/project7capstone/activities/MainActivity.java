package org.bdaoust.project7capstone.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.bdaoust.project7capstone.fragments.DeckDetailsFragment;
import org.bdaoust.project7capstone.fragments.DecksFragment;
import org.bdaoust.project7capstone.R;

public class MainActivity extends AppCompatActivity implements DecksFragment.OnDeckSelectedListener , DecksFragment.OnFirstDeckAddedListener{

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

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

        if(mIsLargeLayout) {
            AdView adView;
            AdRequest adRequest;

            adView = (AdView) findViewById(R.id.adView);
            if(adView != null){
                adRequest = new AdRequest.Builder().build();
                adView.loadAd(adRequest);
            }
        }
    }

    private void loadDetailFragment(String firebaseKey){
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        DeckDetailsFragment deckDetailsFragment;

        Log.d(TAG, "Loading Deck: " + firebaseKey);
        deckDetailsFragment = new DeckDetailsFragment();
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
            intent.putExtra("FIREBASE_DECK_KEY", firebaseKey);
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
}
