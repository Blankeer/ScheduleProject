package com.blanke.scheduleproject

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import kotlin.concurrent.thread

class TestJobService : JobService() {

    override fun onStartJob(params: JobParameters?): Boolean {
        thread {
            Log.d("TestJobService", "开始做事")
            Thread.sleep(5000)
            Log.d("TestJobService", "做完了")
            jobFinished(params, false)
        }.run()
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d("TestJobService", "onStopJob")
        return false
    }
}
