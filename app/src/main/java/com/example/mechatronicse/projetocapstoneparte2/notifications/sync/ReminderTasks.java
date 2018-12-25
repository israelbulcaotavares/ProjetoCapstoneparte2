package com.example.mechatronicse.projetocapstoneparte2.notifications.sync;

import android.content.Context;
import com.example.mechatronicse.projetocapstoneparte2.notifications.utilities.NotificationNoUsers;
import com.example.mechatronicse.projetocapstoneparte2.notifications.utilities.NotificationUtils;

public class ReminderTasks {
    public static final String ACTION_DISMISS_NOTIFICATION = "dismiss-notification";
    public static final String ACTION_POST_REMINDER = "post-reminder";

    public static void executeTask(Context context, String action) {
        if (ACTION_DISMISS_NOTIFICATION.equals(action)) {
             NotificationUtils.clearAllNotifications(context);
        } else if (ACTION_POST_REMINDER.equals(action)) {
            issuePostReminder(context);

        }
    }

    public static void executeTaskFor_NoUsers(Context context, String action) {
        if (ACTION_DISMISS_NOTIFICATION.equals(action)) {
            NotificationNoUsers.clearAllNotifications(context);
        } else if (ACTION_POST_REMINDER.equals(action)) {
            issuePostReminderNoUsers(context);

        }
    }

    private static void issuePostReminder(Context context) {
         NotificationUtils.createNotification(context);
    }

    public static void issuePostReminderNoUsers(Context context) {
        NotificationNoUsers.createNotification(context);
    }

}