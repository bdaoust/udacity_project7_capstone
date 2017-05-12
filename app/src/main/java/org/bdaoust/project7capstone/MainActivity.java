package org.bdaoust.project7capstone;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements DecksFragment.OnDeckSelectedListener{

    private boolean mIsLargeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toolbar toolbar;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIsLargeLayout = getResources().getBoolean(R.bool.large_layout);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(mIsLargeLayout) {
            if (savedInstanceState == null) {
                loadDetailFragment(0);
            }
        }
    }

    private void loadDetailFragment(int deckId){
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        DeckDetailsFragment deckDetailsFragment;

        deckDetailsFragment = new DeckDetailsFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.deckDetailsContainer, deckDetailsFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onDeckSelected(int deckId) {
        if(mIsLargeLayout){
            loadDetailFragment(deckId);
        } else {
            Intent intent;

            intent = new Intent(this, DeckDetailsActivity.class);
            startActivity(intent);
        }
    }
}
