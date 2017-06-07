package org.bdaoust.project7capstone.activities;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.bdaoust.project7capstone.MTGKeys;
import org.bdaoust.project7capstone.adapters.EditCardListAdapter;
import org.bdaoust.project7capstone.R;
import org.bdaoust.project7capstone.adapters.SearchCardListAdapter;
import org.bdaoust.project7capstone.firebasemodels.MTGCardModel;
import org.bdaoust.project7capstone.firebasemodels.MTGDeckModel;
import org.bdaoust.project7capstone.fragments.SearchCardsDialogFragment;

import java.util.ArrayList;
import java.util.List;

import io.magicthegathering.javasdk.resource.Card;

public class EditDeckActivity extends AppCompatActivity implements SearchCardListAdapter.OnCardAddedListener {

    private EditCardListAdapter mEditCardListAdapter;
    private MTGDeckModel mMTGTempDeck;
    private List<MTGCardModel> mMTGTempCards;
    private Context mContext;
    private DatabaseReference mReferenceDeck;
    private DatabaseReference mReferenceTempDeck;
    private DatabaseReference mReferenceTempDeckCards;
    private ValueEventListener mOnTempDeckValueEventListener;
    private ValueEventListener mOnDeckValueEventListener;
    private ChildEventListener mOnTempDeckCardsChildEventListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar;
        ActionBar actionBar;
        Resources resources;
        FirebaseDatabase firebaseDatabase;
        DatabaseReference referenceRoot;
        RecyclerView recyclerView;
        FloatingActionButton searchCardsFAB;
        String tempDeckName;
        String editDeckFirebaseKey;

        mContext = this;
        setContentView(R.layout.activity_edit_deck);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            resources = getResources();
            getSupportActionBar().setTitle(resources.getText(R.string.activity_name_edit_deck));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeActionContentDescription(R.string.action_cancel);
        }

        editDeckFirebaseKey = getIntent().getStringExtra(MTGKeys.FIREBASE_DECK_KEY);
        tempDeckName = "tempDeck" + editDeckFirebaseKey;

        mMTGTempCards = new ArrayList<>();
        mMTGTempDeck = new MTGDeckModel();
        mMTGTempDeck.setMTGCardModels(mMTGTempCards);

        mEditCardListAdapter = new EditCardListAdapter(this, mMTGTempDeck);
        recyclerView = (RecyclerView) findViewById(R.id.editCardsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mEditCardListAdapter);

        searchCardsFAB = (FloatingActionButton) findViewById(R.id.searchCardsFAB);

        searchCardsFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager connectivityManager;
                NetworkInfo activeNetwork;
                boolean isConnected;

                connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                activeNetwork = connectivityManager.getActiveNetworkInfo();
                isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                SearchCardsDialogFragment searchCardsDialogFragment;

                if (isConnected) {
                    searchCardsDialogFragment = new SearchCardsDialogFragment();
                    searchCardsDialogFragment.show(getSupportFragmentManager(), "SEARCH_CARDS");
                } else {
                    Toast.makeText(mContext, R.string.no_network_connection, Toast.LENGTH_SHORT).show();
                }
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        referenceRoot = firebaseDatabase.getReference();
        mReferenceDeck = referenceRoot.child("decks").child(editDeckFirebaseKey);
        mReferenceTempDeck = referenceRoot.child(tempDeckName);
        mReferenceTempDeckCards = mReferenceTempDeck.child("mtgcardModels");

        createListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_deck, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                deleteTempDeck();
                finish();
                return true;
            case R.id.action_save:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCardAdded(Card card) {
        MTGCardModel mtgCard;

        Log.d("EditDeckActivity", "----------- onCardAdded ------------");
        mtgCard = new MTGCardModel(card);
        mtgCard.setNumbCopies(1);
        mMTGTempCards.add(mtgCard);

        mEditCardListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mReferenceTempDeck.addListenerForSingleValueEvent(mOnTempDeckValueEventListener);
        mReferenceTempDeckCards.addChildEventListener(mOnTempDeckCardsChildEventListener);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mReferenceTempDeck.removeEventListener(mOnTempDeckCardsChildEventListener);

        mReferenceTempDeck.removeEventListener(mOnTempDeckValueEventListener);
        mReferenceDeck.removeEventListener(mOnDeckValueEventListener);
    }

    private void createListeners() {
        mOnTempDeckValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    Log.v("AAA", "The Temp Deck doesn't exist... Create It");
                    mReferenceDeck.addListenerForSingleValueEvent(mOnDeckValueEventListener);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        mOnDeckValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MTGDeckModel mtgDeckModel;

                mtgDeckModel = dataSnapshot.getValue(MTGDeckModel.class);
                mMTGTempDeck.setName(mtgDeckModel.getName());
                mReferenceTempDeck.setValue(mtgDeckModel);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        mOnTempDeckCardsChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MTGCardModel mtgCardModel;

                mtgCardModel = dataSnapshot.getValue(MTGCardModel.class);

                mMTGTempCards.add(mtgCardModel);
                mEditCardListAdapter.notifyDataSetChanged();
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
        };
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        deleteTempDeck();
    }

    private void deleteTempDeck() {
        mReferenceTempDeck.removeValue();
    }
}