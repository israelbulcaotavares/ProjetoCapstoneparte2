package com.example.mechatronicse.projetocapstoneparte2.notifications.sync.service_schedule;

import android.app.IntentService;
import android.content.Intent;
import com.example.mechatronicse.projetocapstoneparte2.notifications.sync.ReminderTasks;

public class PostReminderIntentServiceForNoUsers extends IntentService {
    public static final String POSTREMINDER = "PostReminderIntentServiceForNoUsers";
    public PostReminderIntentServiceForNoUsers() {
        super(POSTREMINDER);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        ReminderTasks.executeTaskFor_NoUsers(this, action);

    }

}