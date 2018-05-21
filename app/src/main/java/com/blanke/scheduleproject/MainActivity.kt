package com.blanke.scheduleproject

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("main", "onCreated")
        bu_alarm?.setOnClickListener {
            alarm()
        }
        bu_job?.setOnClickListener {
            jobService()
        }
    }

    private fun jobService() {
        val builder = JobInfo.Builder(100, ComponentName(this, TestJobService::class.java))
        builder.setMinimumLatency(1000)
        builder.setOverrideDeadline(3000)
//        builder.setPeriodic(3000)
        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val result = jobScheduler.schedule(builder.build())
        Log.d("main", "jobScheduler.schedule()=$result")
    }

    private fun alarm() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        Log.d("main", "time=${System.currentTimeMillis()}")
        // 一次性闹钟
        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 10000, getTestActivityIntent())
        Toast.makeText(this, "10s 后会重启 MainActivity", Toast.LENGTH_SHORT).show()
//        finish()

        // >19 精确
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, getTestReceiverIntent())

        // > 23 低耗电模式精确
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, getTestReceiverIntent())
        }

        // 重复闹钟
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 3000, 5000, getTestReceiverIntent())
        // 更省电，会合并闹钟时间，间隔时间可能没那么精准
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 3000, 5000, getTestReceiverIntent())
    }

    private fun getTestActivityIntent(): PendingIntent {
        val intent = Intent(applicationContext, MainActivity::class.java)
        return PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun getTestReceiverIntent(): PendingIntent {
        val intent = Intent(applicationContext, TestReceiver::class.java)
        return PendingIntent.getBroadcast(applicationContext, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}
