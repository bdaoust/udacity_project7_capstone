package org.bdaoust.project7capstone;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class DecksAppWidgetProvider extends AppWidgetProvider{

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId: appWidgetIds) {
            RemoteViews remoteViews;
            Intent intent;

            intent = new Intent(context, DecksWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

            remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget_decks);
            remoteViews.setRemoteAdapter(R.id.decks, intent);
            remoteViews.setEmptyView(R.id.decks, R.id.empty_view);

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

}
