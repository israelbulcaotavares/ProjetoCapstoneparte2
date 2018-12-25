package com.example.mechatronicse.projetocapstoneparte2.notifications.utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Action;
import android.support.v4.content.ContextCompat;
import com.example.mechatronicse.projetocapstoneparte2.R;
import com.example.mechatronicse.projetocapstoneparte2.activity.CadastroActivity;
import com.example.mechatronicse.projetocapstoneparte2.notifications.sync.ReminderTasks;
import com.example.mechatronicse.projetocapstoneparte2.notifications.sync.service_schedule.PostReminderIntentService;
import static com.example.mechatronicse.projetocapstoneparte2.constants.Constants.ACTION_IGNORE_PENDING_INTENT_ID;
import static com.example.mechatronicse.projetocapstoneparte2.constants.Constants.REMINDER_NOTIFICATION_CHANNEL_ID;
import static com.example.mechatronicse.projetocapstoneparte2.constants.Constants.REMINDER_NOTIFICATION_ID;
import static com.example.mechatronicse.projetocapstoneparte2.constants.Constants.REMINDER_PENDING_INTENT_ID;

public class NotificationNoUsers {

    public static void clearAllNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager)  context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static void createNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    REMINDER_NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
            }
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, REMINDER_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.pets_notification)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(R.string.cadastre_noticiations))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                 .addAction(ignoreReminderAction(context))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        notificationManager.notify(REMINDER_NOTIFICATION_ID, notificationBuilder.build());
    }

    private static Action ignoreReminderAction(Context context) {
        Intent ignoreReminderIntent = new Intent(context, PostReminderIntentService.class);
        ignoreReminderIntent.setAction(ReminderTasks.ACTION_DISMISS_NOTIFICATION);
        PendingIntent ignoreReminderPendingIntent = PendingIntent.getService(
                context,
                ACTION_IGNORE_PENDING_INTENT_ID,
                ignoreReminderIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Action ignoreReminderAction = new Action(R.drawable.ic_cancel_black_24px,
                context.getString(R.string.nao_obrigado)  ,
                ignoreReminderPendingIntent);
        return ignoreReminderAction;
    }


    private static PendingIntent contentIntent(Context context) {
        Intent startActivityIntent = new Intent(context, CadastroActivity.class);
        return PendingIntent.getActivity(
                context,
                REMINDER_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static Bitmap largeIcon(Context context) {
        Resources res = context.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.pets_notification);
        return largeIcon;
    }
}