package org.bdaoust.project7capstone.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.bdaoust.project7capstone.R;
import org.bdaoust.project7capstone.firebasemodels.MTGCardModel;
import org.bdaoust.project7capstone.firebasemodels.MTGDeckModel;
import org.bdaoust.project7capstone.tools.MTGTools;

public class EditCardListAdapter extends RecyclerView.Adapter<EditCardListAdapter.EditCardItemViewHolder> {

    private Context mContext;
    private MTGDeckModel mMTGDeck;
    private DatabaseReference mUserRootReference;

    public EditCardListAdapter(Context context, MTGDeckModel mtgDeck){
        FirebaseDatabase firebaseDatabase;

        mContext = context;
        mMTGDeck = mtgDeck;

        firebaseDatabase = FirebaseDatabase.getInstance();
        mUserRootReference = MTGTools.createUserRootReference(firebaseDatabase, null);
    }

    @Override
    public EditCardItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        EditCardItemViewHolder editCardItemViewHolder;
        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_edit_card, parent, false);
        editCardItemViewHolder = new EditCardItemViewHolder(view);

        return editCardItemViewHolder;
    }

    @Override
    public void onBindViewHolder(EditCardItemViewHolder holder, int position) {
        final MTGCardModel mtgCard;
        int numbCopies;

        mtgCard = mMTGDeck.getMTGCards().get(position);
        numbCopies = mtgCard.getNumbCopies();
        Glide.with(holder.itemView.getContext()).load(mtgCard.getImageUrl()).into(holder.cardImage);
        holder.cardName.setText(mtgCard.getName());
        holder.cardNumbCopies.setText(String.valueOf(numbCopies));
        holder.setName.setText(String.valueOf(mtgCard.getSetName()));
        holder.itemView.setTag(mtgCard.getFirebaseKey());

        if(numbCopies == 1){
            disableButton(holder.decrementButton);
        } else {
            enableButton(holder.decrementButton);
        }

        if(numbCopies == 99){
            disableButton(holder.incrementButton);
        } else {
            enableButton(holder.incrementButton);
        }

        holder.incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numbCopies;

                numbCopies = mtgCard.getNumbCopies();
                if(numbCopies < 99){
                    numbCopies++;
                    updateNumbCardCopies(mMTGDeck.getFirebaseKey(), mtgCard.getFirebaseKey(), numbCopies);
                }
            }
        });


        holder.decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numbCopies;

                numbCopies = mtgCard.getNumbCopies();
                if(numbCopies > 1){
                    numbCopies--;
                    updateNumbCardCopies(mMTGDeck.getFirebaseKey(), mtgCard.getFirebaseKey(), numbCopies);
                }
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference cardReference;

                cardReference = MTGTools.createTempDeckCardReference(mUserRootReference, mMTGDeck.getFirebaseKey(), mtgCard.getFirebaseKey());
                cardReference.removeValue();
            }
        });

    }


    @Override
    public int getItemCount() {
        return mMTGDeck.getMTGCards().size();
    }

    public void updateNumbCardCopies(String firebaseDeckKey, String firebaseCardKey, int numbCopies){
        DatabaseReference cardNumbCopiesReference;

        cardNumbCopiesReference = MTGTools.createTempDeckCardNumbCopiesReference(mUserRootReference, firebaseDeckKey, firebaseCardKey);
        cardNumbCopiesReference.setValue(numbCopies);
    }

    private void disableButton(ImageButton button){
        setDrawableTint(button.getDrawable(), R.color.icon_button_disabled_tint_color);
    }

    private void enableButton(ImageButton button){
        setDrawableTint(button.getDrawable(), R.color.icon_button_tint_color);
    }

    private void setDrawableTint(Drawable drawable, int tintColorResource){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            drawable.setTint(mContext.getResources().getColor(tintColorResource, mContext.getTheme()));
        } else {
            //noinspection deprecation
            drawable.setTint(mContext.getResources().getColor(tintColorResource));
        }
    }

    class EditCardItemViewHolder extends RecyclerView.ViewHolder {

        ImageView cardImage;
        TextView cardName;
        TextView cardNumbCopies;
        TextView setName;
        ImageButton decrementButton;
        ImageButton incrementButton;
        ImageButton deleteButton;

        EditCardItemViewHolder(View itemView) {
            super(itemView);

            cardImage = (ImageView) itemView.findViewById(R.id.cardImage);
            cardName = (TextView) itemView.findViewById(R.id.cardName);
            cardNumbCopies = (TextView) itemView.findViewById(R.id.cardNumbCopies);
            setName = (TextView) itemView.findViewById(R.id.setName);
            decrementButton = (ImageButton) itemView.findViewById(R.id.actionDecrementCardCopies);
            incrementButton = (ImageButton) itemView.findViewById(R.id.actionIncrementCardCopies);
            deleteButton = (ImageButton) itemView.findViewById(R.id.actionDeleteCardCopies);
        }
    }
}


