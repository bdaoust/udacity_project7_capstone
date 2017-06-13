package org.bdaoust.project7capstone.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.bdaoust.project7capstone.R;
import org.bdaoust.project7capstone.firebasemodels.MTGDeckModel;
import org.bdaoust.project7capstone.tools.MTGTools;

import java.util.HashSet;
import java.util.Set;

public class CreateDeckDialogFragment extends DialogFragment{

    private DatabaseReference mReferenceDecks;
    private ChildEventListener mOnDecksChildEventListener;
    private Set<String> mDeckNames;
    private EditText mEditDeckName;
    private DecksFragment.OnDeckCreatedListener mOnDeckCreatedListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView;
        AppCompatButton cancelButton;
        AppCompatButton saveButton;
        FirebaseDatabase firebaseDatabase;
        DatabaseReference referenceUserRoot;

        rootView = inflater.inflate(R.layout.fragment_create_deck_dialog, container, false);

        cancelButton = (AppCompatButton) rootView.findViewById(R.id.actionCancel);
        saveButton = (AppCompatButton) rootView.findViewById(R.id.actionSave);
        mEditDeckName = (EditText) rootView.findViewById(R.id.inputDeckName);

        mDeckNames = new HashSet<>();

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputDeckName;

                inputDeckName = mEditDeckName.getText().toString();
                inputDeckName = inputDeckName.trim();

                if(mDeckNames.contains(inputDeckName.toLowerCase())){
                    Toast.makeText(getContext(), R.string.deck_name_already_exists, Toast.LENGTH_SHORT).show();
                } else if (inputDeckName.equals("")){
                    Toast.makeText(getContext(), R.string.deck_name_cant_be_null, Toast.LENGTH_SHORT).show();
                } else {
                    MTGDeckModel mtgDeck;
                    DatabaseReference newDeckReference;
                    String firebaseKey;

                    mtgDeck = new MTGDeckModel();
                    mtgDeck.setName(inputDeckName);
                    mtgDeck.setLastUpdatedTimestamp(System.currentTimeMillis());
                    newDeckReference = mReferenceDecks.push();
                    newDeckReference.setValue(mtgDeck);
                    firebaseKey = newDeckReference.getKey();

                    if(mOnDeckCreatedListener != null){
                        mOnDeckCreatedListener.onDeckCreated(firebaseKey);
                    }

                    mEditDeckName.setText("");
                    dismiss();
                }
            }
        });


        firebaseDatabase = FirebaseDatabase.getInstance();
        referenceUserRoot = MTGTools.createUserRootReference(firebaseDatabase, null);
        mReferenceDecks = MTGTools.createDeckListReference(referenceUserRoot);

        createListeners();

        return rootView;
    }

    private void createListeners() {

        mOnDecksChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MTGDeckModel mtgDeck;

                mtgDeck = dataSnapshot.getValue(MTGDeckModel.class);
                mDeckNames.add(mtgDeck.getName().toLowerCase());
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
    public void onResume() {
        super.onResume();

        addListeners();
    }

    @Override
    public void onPause() {
        super.onPause();

        removeListeners();
        mDeckNames.clear();
    }

    public void setOnDeckCreatedListener(DecksFragment.OnDeckCreatedListener onDeckCreatedListener){
        mOnDeckCreatedListener = onDeckCreatedListener;
    }

    private void addListeners(){
        mReferenceDecks.addChildEventListener(mOnDecksChildEventListener);
    }

    private void removeListeners(){
        mReferenceDecks.removeEventListener(mOnDecksChildEventListener);
    }
}