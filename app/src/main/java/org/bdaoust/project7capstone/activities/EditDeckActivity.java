package org.bdaoust.project7capstone.activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import org.bdaoust.project7capstone.adapters.EditCardListAdapter;
import org.bdaoust.project7capstone.R;
import org.bdaoust.project7capstone.data.SampleDeck;
import org.bdaoust.project7capstone.fragments.SearchCardsDialogFragment;

public class EditDeckActivity extends AppCompatActivity{

    private RecyclerView mRecyclerView;
    private FloatingActionButton mSearchCardsFAB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar;
        ActionBar actionBar;
        Resources resources;

        setContentView(R.layout.activity_edit_deck);

        toolbar = (Toolbar)findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if(actionBar !=null){
            resources = getResources();
            getSupportActionBar().setTitle(resources.getText(R.string.activity_name_edit_deck));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mRecyclerView = (RecyclerView)findViewById(R.id.editCardsList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(new EditCardListAdapter(this, new SampleDeck()));

        mSearchCardsFAB = (FloatingActionButton) findViewById(R.id.searchCardsFAB);

        mSearchCardsFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchCardsDialogFragment searchCardsDialogFragment;

                searchCardsDialogFragment = new SearchCardsDialogFragment();
                searchCardsDialogFragment.show(getSupportFragmentManager(), "SEARCH_CARDS");
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_deck, menu);

        return true;
    }

}
