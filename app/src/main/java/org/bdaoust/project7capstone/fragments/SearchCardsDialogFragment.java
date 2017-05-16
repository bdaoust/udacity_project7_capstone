package org.bdaoust.project7capstone.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.bdaoust.project7capstone.adapters.SearchCardListAdapter;
import org.bdaoust.project7capstone.R;

public class SearchCardsDialogFragment extends DialogFragment {

    private RecyclerView mRecyclerView;
    private EditText mInputCardName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView;

        rootView = inflater.inflate(R.layout.fragment_search_cards_dialog, container, false);

        mInputCardName = (EditText) rootView.findViewById(R.id.inputCardName);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.searchCardsList);
        mRecyclerView.setAdapter(new SearchCardListAdapter(getContext()));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        return rootView;
    }

}
