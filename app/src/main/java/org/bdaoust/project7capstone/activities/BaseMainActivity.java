package org.bdaoust.project7capstone.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;

import org.bdaoust.project7capstone.tools.MTGKeys;
import org.bdaoust.project7capstone.fragments.DeckDetailsFragment;
import org.bdaoust.project7capstone.fragments.DecksFragment;
import org.bdaoust.project7capstone.R;

public class BaseMainActivity extends AppCompatActivity implements DecksFragment.OnDeckSelectedListener,
        DecksFragment.OnFirstDeckAddedListener, DecksFragment.OnDeckDeletedListener{

    private boolean mIsLargeLayout;
    private boolean mIsActivityInitialCreation;
    private final static String TAG = "MainActivity";
    private DeckDetailsFragment mDeckDetailsFragment;
    private String mLoadedDeckFirebaseKey;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign_out:
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadDetailFragment(String firebaseUserId, String firebaseKey){
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        Bundle bundle;

        bundle = new Bundle();
        bundle.putString(MTGKeys.FIREBASE_USER_ID, firebaseUserId);
        bundle.putString(MTGKeys.FIREBASE_DECK_KEY, firebaseKey);
        mDeckDetailsFragment = new DeckDetailsFragment();
        mDeckDetailsFragment.setArguments(bundle);
        mLoadedDeckFirebaseKey = firebaseKey;
        Log.d(TAG, "Loading Deck: " + firebaseKey);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.deckDetailsContainer, mDeckDetailsFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onDeckSelected(String firebaseUserId, String firebaseKey, int position) {
        if(mIsLargeLayout){
            loadDetailFragment(firebaseUserId, firebaseKey);
        } else {
            Intent intent;

            intent = new Intent(this, DeckDetailsActivity.class);
            intent.putExtra(MTGKeys.FIREBASE_USER_ID, firebaseUserId);
            intent.putExtra(MTGKeys.FIREBASE_DECK_KEY, firebaseKey);

            Log.d(TAG, "Loading Deck: " + firebaseKey);
            startActivity(intent);
        }
    }

    @Override
    public void onFirstDeckAdded(String firebaseUserId, String firebaseKey) {
        if(mIsLargeLayout && mIsActivityInitialCreation) {
            Log.d(TAG, "Initial Activity Creation... Loading First Deck");
            loadDetailFragment(firebaseUserId, firebaseKey);
        }
    }

    @SuppressWarnings("unused")
    protected boolean isLargeLayout(){
        return mIsLargeLayout;
    }

    @Override
    public void onDeckDeleted(String firebaseUserId, String firebaseKey) {
        if(mIsLargeLayout && firebaseKey.equals(mLoadedDeckFirebaseKey)){
            FragmentManager fragmentManager;
            FragmentTransaction fragmentTransaction;

            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(mDeckDetailsFragment);
            fragmentTransaction.commit();
        }

    }
}
