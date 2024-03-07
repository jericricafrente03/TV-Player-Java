package com.ph.bittelasia.libvlc.control;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

public class MonitorService extends IntentService {

    private static final String TAG = "MonitorService";

    public MonitorService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String date = intent.getStringExtra("date");
        Log.e(TAG, "onHandleIntent: "+date);
    }


}
