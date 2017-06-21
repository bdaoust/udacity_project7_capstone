package org.bdaoust.project7capstone;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import org.bdaoust.project7capstone.activities.DeckDetailsActivity;
import org.bdaoust.project7capstone.activities.MainActivity;
import org.bdaoust.project7capstone.services.DecksWidgetService;
import org.bdaoust.project7capstone.tools.MTGKeys;

public class DecksAppWidgetProvider extends AppWidgetProvider{

    public static final String SHOW_DECK_DETAILS_ACTION = "org.bdaoust.project7capstone.SHOW_DECK_DETAILS";
    public static final String EXTRA_DECK_FIREBASE_KEY = "org.bdaoust.project7capstone.EXTRA_DECK_FIREBASE_KEY";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SHOW_DECK_DETAILS_ACTION)) {
            Intent startActivityIntent;
            String firebaseDeckKey;
            boolean isLargeLayout;

            firebaseDeckKey = intent.getStringExtra(EXTRA_DECK_FIREBASE_KEY);
            isLargeLayout = context.getResources().getBoolean(R.bool.large_layout);

            if(isLargeLayout){
                startActivityIntent = new Intent(context, MainActivity.class);
                startActivityIntent.putExtra(MTGKeys.FIREBASE_DECK_KEY, firebaseDeckKey);
                // Adding the FLAG_ACTIVITY_CLEAR_TASK to make sure that the MainActivity is recreated
                // each time when starting it from the Widget. This allows us to wait for onActivityCreated()
                // in the DecksFragment in order to grab the Firebase Key of the Deck to be loaded.
                startActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(startActivityIntent);
            } else {
                TaskStackBuilder taskStackBuilder;

                startActivityIntent = new Intent(context, DeckDetailsActivity.class);
                startActivityIntent.putExtra(MTGKeys.FIREBASE_DECK_KEY, firebaseDeckKey);

                taskStackBuilder = TaskStackBuilder.create(context);
                taskStackBuilder.addNextIntentWithParentStack(startActivityIntent);
                taskStackBuilder.startActivities();
            }

        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId: appWidgetIds) {
            RemoteViews remoteViews;
            Intent decksWidgetServiceIntent;
            Intent showDeckDetailsIntent;
            PendingIntent showDeckDetailsPendingIntent;

            showDeckDetailsIntent = new Intent(context, DecksAppWidgetProvider.class);
            showDeckDetailsIntent.setAction(SHOW_DECK_DETAILS_ACTION);
            showDeckDetailsPendingIntent = PendingIntent.
                    getBroadcast(context, 0, showDeckDetailsIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            decksWidgetServiceIntent = new Intent(context, DecksWidgetService.class);
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget_decks);
            remoteViews.setRemoteAdapter(R.id.decks, decksWidgetServiceIntent);
            remoteViews.setEmptyView(R.id.decks, R.id.empty_view);
            remoteViews.setPendingIntentTemplate(R.id.decks, showDeckDetailsPendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

}
