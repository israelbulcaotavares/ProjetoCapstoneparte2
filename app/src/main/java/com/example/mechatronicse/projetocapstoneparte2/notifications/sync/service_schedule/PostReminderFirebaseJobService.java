/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.mechatronicse.projetocapstoneparte2.notifications.sync.service_schedule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import com.example.mechatronicse.projetocapstoneparte2.data.FirebaseConfig;
import com.example.mechatronicse.projetocapstoneparte2.notifications.sync.ReminderTasks;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
 import com.google.firebase.auth.FirebaseAuth;

public class PostReminderFirebaseJobService extends JobService {
    public AsyncTask mBackgroundTask;
    private FirebaseAuth auth;

    @SuppressLint("StaticFieldLeak")
    @Override
    public boolean onStartJob(final JobParameters jobParameters) {

        mBackgroundTask = new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] params) {

                    auth  = FirebaseConfig.getFirebaseAutenticacao();

                    if (auth.getCurrentUser() != null){
                        Context context = PostReminderFirebaseJobService.this;
                        ReminderTasks.executeTask(context, ReminderTasks.ACTION_POST_REMINDER);
                    }else{
                        Context context2 = PostReminderFirebaseJobService.this;
                        ReminderTasks.executeTaskFor_NoUsers(context2, ReminderTasks.ACTION_POST_REMINDER);
                    }


                 return null;
            }

            @Override
            public void onPostExecute(Object o) {

                jobFinished(jobParameters, false);
            }
        };

        mBackgroundTask.execute();
        return true;
    }


    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if (mBackgroundTask != null) mBackgroundTask.cancel(true);
        return true;
    }
}