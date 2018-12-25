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

import android.app.IntentService;
import android.content.Intent;
import com.example.mechatronicse.projetocapstoneparte2.notifications.sync.ReminderTasks;

import static com.example.mechatronicse.projetocapstoneparte2.constants.Constants.POSTREMINDER;

public class PostReminderIntentService extends IntentService {

    public PostReminderIntentService() {
        super(POSTREMINDER);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        ReminderTasks.executeTask(this, action);

    }

}