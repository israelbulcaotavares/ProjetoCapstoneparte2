package com.example.mechatronicse.projetocapstoneparte2.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import com.example.mechatronicse.projetocapstoneparte2.R;
import com.example.mechatronicse.projetocapstoneparte2.activity.DetalhesActivity;

@SuppressWarnings("WeakerAccess")
public class AnuncioAppWidget extends AppWidgetProvider {
    @SuppressWarnings("WeakerAccess")
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = getAnunciosView(context);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static RemoteViews getAnunciosView(Context context) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_list_view);
        Intent gridViewServiceIntent = new Intent(context, AnuncioRemoteViewsService.class);
        remoteViews.setRemoteAdapter(R.id.widget_grid_view, gridViewServiceIntent);

        Intent appIntent = new Intent(context, DetalhesActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,appIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViews.setPendingIntentTemplate(R.id.widget_grid_view, pendingIntent);
        remoteViews.setEmptyView(R.id.widget_grid_view, R.id.progressBar);
        return remoteViews;

    }

    public static void updateAnunciosAppWidget(Context context, AppWidgetManager manager, int[] widgetsIds) {
        for (int appWidgetId : widgetsIds) {
            updateAppWidget(context, manager, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        AnuncioWidgetService.startActionUpdateWidget(context);
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}

