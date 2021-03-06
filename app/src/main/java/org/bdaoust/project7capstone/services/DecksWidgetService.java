package org.bdaoust.project7capstone.services;


import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.bdaoust.project7capstone.DecksAppWidgetProvider;
import org.bdaoust.project7capstone.R;
import org.bdaoust.project7capstone.firebasemodels.MTGDeckModel;
import org.bdaoust.project7capstone.tools.MTGTools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DecksWidgetService extends RemoteViewsService {

    private final static String TAG = "DecksWidgetService";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new DeckRemoteViewsFactory(this.getApplicationContext());
    }

    private class DeckRemoteViewsFactory implements RemoteViewsFactory {

        private Context mContext;
        private FirebaseAuth mFirebaseAuth;
        private FirebaseAuth.AuthStateListener mAuthStateListener;
        private FirebaseDatabase mFirebaseDatabase;
        private DatabaseReference mReferenceUserRoot;
        private DatabaseReference mReferenceDecks;
        private ChildEventListener mOnDecksChildEventListener;
        private List<MTGDeckModel> mMTGDecks;
        private String mFirebaseUserId;
        private boolean mOnSignedInInitializeCalled;

        DeckRemoteViewsFactory(Context context) {
            mMTGDecks = new ArrayList<>();
            mContext = context;

            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mFirebaseAuth = FirebaseAuth.getInstance();
            mOnSignedInInitializeCalled = false;

            createFirebaseAuthListener();
            createFirebaseDBListeners();
        }

        @Override
        public void onCreate() {
            mFirebaseAuth.addAuthStateListener(mAuthStateListener);
            Log.d(TAG, "RemoteViewsFactory CREATED");
        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
            if (mReferenceDecks != null) {
                mReferenceDecks.removeEventListener(mOnDecksChildEventListener);
            }
            Log.d(TAG, "RemoteViewsFactory DESTROYED");
        }

        @Override
        public int getCount() {
            return mMTGDecks.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews remoteViews;
            MTGDeckModel mtgDeck;
            MTGDeckModel.ColorPercentages colorPercentages;
            String extraInfo;
            String lastUpdated;
            Bundle extras;
            Intent fillIntent;
            long lastUpdatedTimestamp;
            int numbCards;
            float colorless;

            mtgDeck = mMTGDecks.get(position);
            numbCards = mtgDeck.getNumbCards();
            lastUpdatedTimestamp = mtgDeck.getLastUpdatedTimestamp();
            lastUpdated = DateUtils.formatDateTime(mContext, lastUpdatedTimestamp, DateUtils.FORMAT_SHOW_YEAR);
            extraInfo = mContext.getResources().
                    getQuantityString(R.plurals.deck_extra_info, numbCards, numbCards, lastUpdated);

            remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.app_widget_list_item_deck);
            remoteViews.setTextViewText(R.id.deckName, mMTGDecks.get(position).getName());
            remoteViews.setTextViewText(R.id.deckExtraInfo, extraInfo);

            colorPercentages = mtgDeck.getColorPercentages();

            if (colorPercentages.black <= 0) {
                remoteViews.setViewVisibility(R.id.mtgColorBlackItem, View.GONE);
            } else {
                remoteViews.setViewVisibility(R.id.mtgColorBlackItem, View.VISIBLE);
                remoteViews.setTextViewText(R.id.mtgColorBlack, (int) colorPercentages.black + "%");
            }

            if (colorPercentages.blue <= 0) {
                remoteViews.setViewVisibility(R.id.mtgColorBlueItem, View.GONE);
            } else {
                remoteViews.setViewVisibility(R.id.mtgColorBlueItem, View.VISIBLE);
                remoteViews.setTextViewText(R.id.mtgColorBlue, (int) colorPercentages.blue + "%");
            }

            if (colorPercentages.green <= 0) {
                remoteViews.setViewVisibility(R.id.mtgColorGreenItem, View.GONE);
            } else {
                remoteViews.setViewVisibility(R.id.mtgColorGreenItem, View.VISIBLE);
                remoteViews.setTextViewText(R.id.mtgColorGreen, (int) colorPercentages.green + "%");
            }

            if (colorPercentages.red <= 0) {
                remoteViews.setViewVisibility(R.id.mtgColorRedItem, View.GONE);
            } else {
                remoteViews.setViewVisibility(R.id.mtgColorRedItem, View.VISIBLE);
                remoteViews.setTextViewText(R.id.mtgColorRed, (int) colorPercentages.red + "%");
            }

            if (colorPercentages.white <= 0) {
                remoteViews.setViewVisibility(R.id.mtgColorWhiteItem, View.GONE);
            } else {
                remoteViews.setViewVisibility(R.id.mtgColorWhiteItem, View.VISIBLE);
                remoteViews.setTextViewText(R.id.mtgColorWhite, (int) colorPercentages.white + "%");
            }

            colorless = 100f - colorPercentages.black - colorPercentages.blue - colorPercentages.green
                    - colorPercentages.red - colorPercentages.white;

            if (colorless <= 0) {
                remoteViews.setViewVisibility(R.id.mtgColorlessItem, View.GONE);
            } else {
                remoteViews.setViewVisibility(R.id.mtgColorlessItem, View.VISIBLE);
                remoteViews.setTextViewText(R.id.mtgColorless, (int) colorless + "%");
            }


            extras = new Bundle();
            extras.putString(DecksAppWidgetProvider.EXTRA_FIREBASE_USER_ID, mFirebaseUserId);
            extras.putString(DecksAppWidgetProvider.EXTRA_DECK_FIREBASE_KEY, mtgDeck.getFirebaseKey());
            fillIntent = new Intent();
            fillIntent.putExtras(extras);

            remoteViews.setOnClickFillInIntent(R.id.deckListItem, fillIntent);

            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        private void onSignedInInitialize() {
            mReferenceUserRoot = MTGTools.createUserRootReference(mFirebaseDatabase, mFirebaseUserId);
            mReferenceDecks = MTGTools.createDeckListReference(mReferenceUserRoot);

            mReferenceDecks.addChildEventListener(mOnDecksChildEventListener);

            mOnSignedInInitializeCalled = true;
        }

        private void onSignedOutCleanup() {
            if (mReferenceDecks != null) {
                mReferenceDecks.removeEventListener(mOnDecksChildEventListener);
            }
            mMTGDecks.clear();

            updateAllWidgets();

            mOnSignedInInitializeCalled = true;
        }

        private void createFirebaseAuthListener() {
            mAuthStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser firebaseUser;

                    firebaseUser = firebaseAuth.getCurrentUser();
                    if (firebaseUser != null) {
                        mFirebaseUserId = firebaseUser.getUid();
                        // Even though only one AuthStateListener gets registered, the onAuthStateChanged event
                        // can still gets triggered more than once which has the effect of creating duplicate
                        // Decks in the list. To avoid this, we use a boolean to keep track of whether
                        // onSignedInInitialize() was called or not. This is based on the solution
                        // from YYY: https://stackoverflow.com/questions/37673616/firebase-android-onauthstatechanged-called-twice
                        if (!mOnSignedInInitializeCalled) {
                            Log.d(TAG, "About to call onSignedInInitialize()");
                            onSignedInInitialize();
                        } else {
                            Log.d(TAG, "User already signed in... not calling onSignedInInitialize()");
                        }
                    } else {
                        onSignedOutCleanup();
                    }
                }
            };
        }

        private void createFirebaseDBListeners() {
            mOnDecksChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    MTGDeckModel mtgDeck;

                    mtgDeck = dataSnapshot.getValue(MTGDeckModel.class);
                    mtgDeck.setFirebaseKey(dataSnapshot.getKey());
                    mMTGDecks.add(mtgDeck);

                    Collections.sort(mMTGDecks, new Comparator<MTGDeckModel>() {
                        @Override
                        public int compare(MTGDeckModel deck1, MTGDeckModel deck2) {
                            return deck1.getName().compareTo(deck2.getName());
                        }
                    });

                    updateAllWidgets();
                    Log.d(TAG, "Widget MTG Deck added: " + mtgDeck.getFirebaseKey());
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    MTGDeckModel updatedMTGDeck;
                    int position = -1;

                    updatedMTGDeck = dataSnapshot.getValue(MTGDeckModel.class);
                    updatedMTGDeck.setFirebaseKey(dataSnapshot.getKey());

                    for (int i = 0; i < mMTGDecks.size(); i++) {
                        MTGDeckModel mtgDeck;

                        mtgDeck = mMTGDecks.get(i);
                        if (mtgDeck.getFirebaseKey().equals(updatedMTGDeck.getFirebaseKey())) {
                            position = i;
                        }
                    }

                    if (position >= 0) {
                        mMTGDecks.set(position, updatedMTGDeck);
                    }

                    updateAllWidgets();
                    Log.d(TAG, "Widget MTG Deck updated: " + updatedMTGDeck.getFirebaseKey());
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    MTGDeckModel removedMTGDeck;
                    int position = -1;

                    removedMTGDeck = dataSnapshot.getValue(MTGDeckModel.class);
                    removedMTGDeck.setFirebaseKey(dataSnapshot.getKey());

                    for (int i = 0; i < mMTGDecks.size(); i++) {
                        MTGDeckModel mtgDeck;

                        mtgDeck = mMTGDecks.get(i);
                        if (mtgDeck.getFirebaseKey().equals(removedMTGDeck.getFirebaseKey())) {
                            position = i;
                        }
                    }

                    if (position >= 0) {
                        mMTGDecks.remove(position);
                    }

                    updateAllWidgets();
                    Log.d(TAG, "Widget MTG Deck removed: " + removedMTGDeck.getFirebaseKey());
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
        }

        private void updateAllWidgets() {
            ComponentName componentName;
            AppWidgetManager appWidgetManager;
            int[] appWidgetIds;

            appWidgetManager = AppWidgetManager.getInstance(mContext);
            componentName = new ComponentName(mContext, DecksAppWidgetProvider.class);
            appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);

            for (int appWidgetId : appWidgetIds) {
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.decks);
            }

        }
    }
}
