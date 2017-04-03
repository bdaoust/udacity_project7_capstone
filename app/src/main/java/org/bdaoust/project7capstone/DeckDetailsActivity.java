package org.bdaoust.project7capstone;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class DeckDetailsActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_deck_details);

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
