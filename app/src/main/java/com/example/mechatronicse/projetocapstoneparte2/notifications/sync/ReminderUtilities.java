package com.example.mechatronicse.projetocapstoneparte2.notifications.sync;

import android.content.Context;
import com.example.mechatronicse.projetocapstoneparte2.notifications.sync.service_schedule.PostReminderFirebaseJobService;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import static com.example.mechatronicse.projetocapstoneparte2.constants.Constants.REMINDER_INTERVAL_SECONDS;
import static com.example.mechatronicse.projetocapstoneparte2.constants.Constants.REMINDER_JOB_FOR_NO__USERS_TAG;
import static com.example.mechatronicse.projetocapstoneparte2.constants.Constants.REMINDER_JOB_TAG;
import static com.example.mechatronicse.projetocapstoneparte2.constants.Constants.SYNC_FLEXTIME_SECONDS;
import static com.example.mechatronicse.projetocapstoneparte2.constants.Constants.SYNC_FLEXTIME_SECONDS_NO_USERS;
import static com.example.mechatronicse.projetocapstoneparte2.constants.Constants.sInitialized;
import static com.example.mechatronicse.projetocapstoneparte2.constants.Constants.sInitializedNoUsers;

public class ReminderUtilities {

    synchronized public static void scheduleReminderPosts(final Context context) {

        if (sInitialized) return;
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job constraintReminderJob = dispatcher.newJobBuilder()
                .setService(PostReminderFirebaseJobService.class)
                .setTag(REMINDER_JOB_TAG)               // enviar notificação
                .setConstraints(Constraint.ON_ANY_NETWORK)  // e com qlqr rede
                .setLifetime(Lifetime.FOREVER) // aparecer a proxima notificação só no proximo boot
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        REMINDER_INTERVAL_SECONDS,
                        REMINDER_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();

        dispatcher.mustSchedule(constraintReminderJob);
        sInitialized = true;
    }


    synchronized public static void scheduleReminderPostsFor_NoUsers(final Context context) {
        if (sInitializedNoUsers) return;
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job constraintReminderJob = dispatcher.newJobBuilder()
                .setService(PostReminderFirebaseJobService.class)
                .setTag(REMINDER_JOB_FOR_NO__USERS_TAG)               // enviar notificação
                .setConstraints(Constraint.ON_ANY_NETWORK)  // e com qlqr rede
                .setLifetime(Lifetime.FOREVER) // aparecer a proxima notificação só no proximo boot
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        REMINDER_INTERVAL_SECONDS,
                        REMINDER_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS_NO_USERS))
                .setReplaceCurrent(true)
                .build();

        dispatcher.mustSchedule(constraintReminderJob);
        sInitializedNoUsers = true;
    }



}
