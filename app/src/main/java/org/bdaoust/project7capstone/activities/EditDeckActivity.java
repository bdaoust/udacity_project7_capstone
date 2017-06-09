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

import org.bdaoust.project7capstone.tools.MTGKeys;
import org.bdaoust.project7capstone.adapters.EditCardListAdapter;
import org.bdaoust.project7capstone.R;
import org.bdaoust.project7capstone.adapters.SearchCardListAdapter;
import org.bdaoust.project7capstone.firebasemodels.MTGCardModel;
import org.bdaoust.project7capstone.firebasemodels.MTGDeckModel;
import org.bdaoust.project7capstone.fragments.SearchCardsDialogFragment;
import org.bdaoust.project7capstone.tools.MTGTools;

import java.util.ArrayList;
import java.util.List;

import io.magicthegathering.javasdk.resource.Card;

public class EditDeckActivity extends AppCompatActivity implements SearchCardListAdapter.OnCardAddedListener {

    private EditCardListAdapter mEditCardListAdapter;
    private MTGDeckModel mMTGTempDeck;
    private Context mContext;
    private DatabaseReference mReferenceDeck;
    private DatabaseReference mReferenceTempDeck;
    private DatabaseReference mReferenceTempDeckCards;
    private ValueEventListener mOnTempDeckValueEventListener;
    private ValueEventListener mOnDeckValueEventListener;
    private ChildEventListener mOnTempDeckCardsChildEventListener;
    private String mTempDeckFirebaseKey;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar;
        ActionBar actionBar;
        Resources resources;
        List<MTGCardModel> mtgTempCards;
        FirebaseDatabase firebaseDatabase;
        DatabaseReference referenceUserRoot;
        RecyclerView recyclerView;
        FloatingActionButton searchCardsFAB;
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
        mTempDeckFirebaseKey = "tempDeck" + editDeckFirebaseKey;

        mtgTempCards = new ArrayList<>();
        mMTGTempDeck = new MTGDeckModel();
        mMTGTempDeck.setFirebaseKey(mTempDeckFirebaseKey);
        mMTGTempDeck.setMTGCards(mtgTempCards);

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
        referenceUserRoot = MTGTools.createUserRootReference(firebaseDatabase, null);
        mReferenceDeck = MTGTools.createDeckReference(referenceUserRoot, editDeckFirebaseKey);
        mReferenceTempDeck = MTGTools.createTempDeckReference(referenceUserRoot, mTempDeckFirebaseKey);
        mReferenceTempDeckCards = MTGTools.createTempDeckCardsReference(referenceUserRoot, mTempDeckFirebaseKey);

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
        MTGCardModel newMTGCard;
        MTGCardModel mtgCard;

        newMTGCard = new MTGCardModel(card);

        if(mMTGTempDeck.findCardByMultiverseId(newMTGCard.getMultiverseId()) != null){
            int numbCopies;

            mtgCard = mMTGTempDeck.findCardByMultiverseId(newMTGCard.getMultiverseId());
            numbCopies = mtgCard.getNumbCopies();

            if(numbCopies < 99){
                numbCopies++;
                mEditCardListAdapter.updateNumbCardCopies(mTempDeckFirebaseKey, mtgCard.getFirebaseKey(), numbCopies);
            }
        } else {
            newMTGCard.setNumbCopies(1);
            mReferenceTempDeckCards.push().setValue(newMTGCard);
        }

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
                MTGDeckModel mtgDeck;

                mtgDeck = dataSnapshot.getValue(MTGDeckModel.class);
                mMTGTempDeck.setName(mtgDeck.getName());
                mReferenceTempDeck.setValue(mtgDeck);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        mOnTempDeckCardsChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MTGCardModel mtgCard;
                int position;

                mtgCard = dataSnapshot.getValue(MTGCardModel.class);
                mtgCard.setFirebaseKey(dataSnapshot.getKey());

                mMTGTempDeck.addCard(mtgCard);
                position = mMTGTempDeck.findCardPositionByMultiverseId(mtgCard.getMultiverseId());
                mEditCardListAdapter.notifyItemInserted(position);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                MTGCardModel updatedMTGCard;
                MTGCardModel mtgCard;
                int position;

                updatedMTGCard = dataSnapshot.getValue(MTGCardModel.class);
                updatedMTGCard.setFirebaseKey(dataSnapshot.getKey());

                mtgCard = mMTGTempDeck.findCardByMultiverseId(updatedMTGCard.getMultiverseId());
                mtgCard.setNumbCopies(updatedMTGCard.getNumbCopies());
                position = mMTGTempDeck.findCardPositionByMultiverseId(updatedMTGCard.getMultiverseId());
                mEditCardListAdapter.notifyItemChanged(position);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                MTGCardModel deletedMTGCard;
                int position;

                deletedMTGCard = dataSnapshot.getValue(MTGCardModel.class);
                position = mMTGTempDeck.findCardPositionByMultiverseId(deletedMTGCard.getMultiverseId());
                mMTGTempDeck.removeCard(deletedMTGCard.getMultiverseId());
                mEditCardListAdapter.notifyItemRemoved(position);
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