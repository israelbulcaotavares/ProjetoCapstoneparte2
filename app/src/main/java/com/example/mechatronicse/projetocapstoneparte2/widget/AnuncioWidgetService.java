package com.example.mechatronicse.projetocapstoneparte2.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.example.mechatronicse.projetocapstoneparte2.R;

@SuppressWarnings("WeakerAccess")
public class AnuncioWidgetService extends IntentService {

    @SuppressWarnings("WeakerAccess")
    public static final String UPDATE_WIDGET_ACTION = "com.example.mechatronicse.projetocapstoneparte2.widget_update_widget_action";

    public AnuncioWidgetService() {
        super("AnuncioWidgetService");
    }

    public static void startActionUpdateWidget(Context context) {
        Intent intent = new Intent(context, AnuncioWidgetService.class);
        intent.setAction(UPDATE_WIDGET_ACTION);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (UPDATE_WIDGET_ACTION.equals(action)) {
                handleUpdateWidgetAction();
            }
        }
    }

    private void handleUpdateWidgetAction() {
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(this);
        int widgetsIds[] = widgetManager.getAppWidgetIds(new ComponentName(this, AnuncioAppWidget.class));
        widgetManager.notifyAppWidgetViewDataChanged(widgetsIds, R.id.widget_grid_view);
        AnuncioAppWidget.updateAnunciosAppWidget(this, widgetManager, widgetsIds);
    }
}
