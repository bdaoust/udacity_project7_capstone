package org.bdaoust.project7capstone.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.SurfaceView;

import org.bdaoust.project7capstone.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MTGDeckPieChart extends SurfaceView {

    private Paint mColorlessPaint;
    private Paint mBorderPaint;

    private float mCenterX;
    private float mCenterY;
    private float mRadius;
    private float mLeft;
    private float mTop;
    private float mRight;
    private float mBottom;
    private float mPadding;
    private float mBorderWidth;

    private final static String KEY_WHITE = "white";
    private final static String KEY_BLUE = "blue";
    private final static String KEY_BLACK = "black";
    private final static String KEY_RED = "red";
    private final static String KEY_GREEN = "green";

    private HashMap<String, DeckColor> mDeckColors;

    public MTGDeckPieChart(Context context, AttributeSet attributeSet){
        super(context, attributeSet);

        Paint paint;
        TypedArray typedArray;
        float whitePercentage;
        float bluePercentage;
        float blackPercentage;
        float redPercentage;
        float greenPercentage;

        mDeckColors = new HashMap<>();

        typedArray = context.getTheme().obtainStyledAttributes(
                attributeSet,
                R.styleable.MTGDeckPieChart,
                0, 0);

        try {
            whitePercentage = typedArray.getFloat(R.styleable.MTGDeckPieChart_whitePercentage, 0);
            bluePercentage = typedArray.getFloat(R.styleable.MTGDeckPieChart_bluePercentage, 0);
            blackPercentage = typedArray.getFloat(R.styleable.MTGDeckPieChart_blackPercentage, 0);
            redPercentage = typedArray.getFloat(R.styleable.MTGDeckPieChart_redPercentage, 0);
            greenPercentage = typedArray.getFloat(R.styleable.MTGDeckPieChart_greenPercentage, 0);
        } finally {
            typedArray.recycle();
        }

        //White Paint
        paint = new Paint();
        paint.setARGB(255, 255, 255, 255); //Material White
        paint.setAntiAlias(true);
        mDeckColors.put(KEY_WHITE, new DeckColor(paint, whitePercentage));

        //Blue Paint
        paint = new Paint();
        paint.setARGB(255, 33, 150, 243); //Material Blue 500
        paint.setAntiAlias(true);
        mDeckColors.put(KEY_BLUE, new DeckColor(paint, bluePercentage));

        //Black Paint
        paint = new Paint();
        paint.setARGB(255, 0, 0, 0); //Material Black
        paint.setAntiAlias(true);
        mDeckColors.put(KEY_BLACK, new DeckColor(paint, blackPercentage));

        //Red Paint
        paint = new Paint();
        paint.setARGB(255, 244, 67, 54); //Material Red 500
        paint.setAntiAlias(true);
        mDeckColors.put(KEY_RED, new DeckColor(paint, redPercentage));

        //Green Paint
        paint = new Paint();
        paint.setARGB(255, 76, 175, 80); //Material Green 500
        paint.setAntiAlias(true);
        mDeckColors.put(KEY_GREEN, new DeckColor(paint, greenPercentage));

        //Colorless Paint
        mColorlessPaint = new Paint();
        mColorlessPaint.setARGB(255, 158, 158, 158); //Material Grey 500
        mColorlessPaint.setAntiAlias(true);

        //Border Paint
        mBorderPaint = new Paint();
        mBorderPaint.setARGB(128, 189, 189, 189); //Material Grey 400 (semi transparent)
        mBorderPaint.setAntiAlias(true);

        mBorderWidth = 1;
        mPadding = 4;

        //Added a call to setZOrderOnTop(true) in order to make the part of the SurfaceView that is not
        //drawn transparent. Based on a solution provided by
        // Bajji (http://stackoverflow.com/questions/31599167/surfaceview-transparency-not-working-properly)
        setZOrderOnTop(true);

        updateContentDescription();
    }

    public float getWhitePercentage(){
        return mDeckColors.get(KEY_WHITE).mPercentage;
    }

    public void setWhitePercentage(float percentage){
        setColorPercentage(KEY_WHITE, percentage);
    }

    public float getBluePercentage(){
        return mDeckColors.get(KEY_BLUE).mPercentage;
    }

    public void setBluePercentage(float percentage){
        setColorPercentage(KEY_BLUE, percentage);
    }

    public float getBlackPercentage(){
        return mDeckColors.get(KEY_BLACK).mPercentage;
    }

    public void setBlackPercentage(float percentage){
        setColorPercentage(KEY_BLACK, percentage);
    }

    public float getRedPercentage(){
        return mDeckColors.get(KEY_RED).mPercentage;
    }

    public void setRedPercentage(float percentage){
        setColorPercentage(KEY_RED, percentage);
    }

    public float getGreenPercentage(){
        return mDeckColors.get(KEY_GREEN).mPercentage;
    }

    public void setGreenPercentage(float percentage){
        setColorPercentage(KEY_GREEN, percentage);
    }

    private void setColorPercentage(String key, float percentage){
        setPercentage(key, percentage);
        updateContentDescription();
    }

    private void setPercentage(String key, float percentage){
        DeckColor deckColor;

        if(percentage < 0){
            percentage = 0;
        }
        if(percentage > 100){
            percentage = 100;
        }

        deckColor = mDeckColors.get(key);
        deckColor.mPercentage = percentage;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mCenterX = w/2.0f;
        mCenterY = h/2.0f;
        mRadius = w/2.0f - mPadding;

        //Adjusting the left, top, right, bottom positions (which will be used to draw the
        // pie chart), to account for the border width and padding.
        mLeft = 0 + mBorderWidth + mPadding;
        mTop = 0 + mBorderWidth + mPadding;
        mRight = w - mBorderWidth - mPadding;
        mBottom = h - mBorderWidth - mPadding;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        ArrayList<DeckColor> deckColorsList;
        float currentAngle;
        float degreesCovered;
        float degreesLeft;

        deckColorsList = new ArrayList<>(mDeckColors.values());
        Collections.sort(deckColorsList);
        Collections.reverse(deckColorsList);

        currentAngle = -90;
        degreesCovered = 0;

        canvas.drawCircle(mCenterX, mCenterY, mRadius+mBorderWidth, mBorderPaint);

        for(int i = 0; i < deckColorsList.size(); i++){
            DeckColor deckColor;
            float sweepAngle;

            deckColor = deckColorsList.get(i);
            sweepAngle = calcSweepAngle(deckColor.mPercentage);
            canvas.drawArc(mLeft, mTop, mRight, mBottom, currentAngle, sweepAngle, true, deckColor.mPaint);
            currentAngle += sweepAngle;
            degreesCovered += sweepAngle;
        }

        if(degreesCovered < 360) {
            degreesLeft = 360 - degreesCovered;
            canvas.drawArc(mLeft, mTop, mRight, mBottom, currentAngle, degreesLeft, true, mColorlessPaint);
        }
    }

    private class DeckColor implements Comparable<DeckColor>{
        Paint mPaint;
        Float mPercentage;

        DeckColor(Paint paint, Float percentage) {
            mPaint = paint;
            mPercentage = percentage;
        }

        @Override
        public int compareTo(@NonNull DeckColor deckColor) {
            return mPercentage.compareTo(deckColor.mPercentage);
        }
    }

    private float calcSweepAngle(float percentage){
        return (percentage/100f)*360f;
    }

    private void updateContentDescription(){
        String deckColorBreakdown;
        int colorlessPercentage;

        colorlessPercentage = (int)(100 - getBlackPercentage() - getBluePercentage() - getGreenPercentage()
                - getRedPercentage() - getWhitePercentage());

        deckColorBreakdown = getResources().getString(R.string.deck_color_breakdown,
                (int)getBlackPercentage(), (int)getBluePercentage(), (int)getGreenPercentage(),
                (int)getRedPercentage(), (int)getWhitePercentage(), colorlessPercentage);

        setContentDescription(deckColorBreakdown);
    }
}
