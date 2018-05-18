package com.blanke.scheduleproject

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class TestReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("TestReceiver", "onReceive ts=${System.currentTimeMillis()}")
    }
}