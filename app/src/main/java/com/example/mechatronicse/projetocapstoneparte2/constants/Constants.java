package com.example.mechatronicse.projetocapstoneparte2.constants;

import java.util.concurrent.TimeUnit;

public class Constants {

    //Animate Views
    public static final int ZERO = 0;
    public static final float Alpha = 0.85f;
    public static final float Y = 0f;
    public static final float alphaNumber = 1f;
    public static final long DURATION = 1000L;
    public static final float NUMBER_OFFSET = 1.5f;

    //Versao SKD PERMISSION
    public static final int VERSION_SDK_PERSMISSION = 23;

    //requestCodes
    public static final int request_UM = 1;
    public static final int request_DOIS = 2;
    public static final int request_TRES = 3;

    //Post Reminder Intent Service
    public static final String POSTREMINDER = "PostReminderIntentService";

    //todo:usuarios cadastrados
    public static final int REMINDER_INTERVAL_MINUTES = 60; // 60 min
    public static final int REMINDER_INTERVAL_SECONDS = (int) (TimeUnit.MINUTES.toSeconds(REMINDER_INTERVAL_MINUTES));
    public static final int SYNC_FLEXTIME_SECONDS = REMINDER_INTERVAL_SECONDS;
    public static final String REMINDER_JOB_TAG = "reminder_tag";
    public static boolean sInitialized;

    //todo:usuarios não-cadastrados
    public static final int REMINDER_INTERVAL_MINUTES_FOR_NO_USERS = 15;  // 15 min
    public static final int REMINDER_INTERVAL_SECONDS_FOR_NO_USERS = (int) (TimeUnit.MINUTES.toSeconds(REMINDER_INTERVAL_MINUTES_FOR_NO_USERS));
    public static final int SYNC_FLEXTIME_SECONDS_NO_USERS = REMINDER_INTERVAL_SECONDS_FOR_NO_USERS;
    public static final String REMINDER_JOB_FOR_NO__USERS_TAG = "reminder_for_no_users_tag";
    public static boolean sInitializedNoUsers;


    //todo: Notifications
    public static final int REMINDER_NOTIFICATION_ID = 1138;
    public static final int REMINDER_PENDING_INTENT_ID = 3417;
    public static final String REMINDER_NOTIFICATION_CHANNEL_ID = "reminder_notification_channel";
    public static final int ACTION_IGNORE_PENDING_INTENT_ID = 14;


}
