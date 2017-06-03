package org.bdaoust.project7capstone.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.bdaoust.project7capstone.fragments.DeckDetailsFragment;
import org.bdaoust.project7capstone.R;

public class BaseDeckDetailsActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar;
        ActionBar actionBar;

        setContentView(R.layout.activity_deck_details);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        if(actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("[Deck Name]");
        }

        if(savedInstanceState == null){
            FragmentManager fragmentManager;
            FragmentTransaction fragmentTransaction;
            DeckDetailsFragment deckDetailsFragment;

            deckDetailsFragment = new DeckDetailsFragment();

            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.deckDetailsContainer, deckDetailsFragment);
            fragmentTransaction.commit();
        }
    }

}
