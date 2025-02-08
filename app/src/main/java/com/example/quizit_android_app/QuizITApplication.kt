package com.example.quizit_android_app

import android.app.Application
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.quizit_android_app.workers.DataSyncWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class QuizITApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        setupPeriodicWork()
    }

    private fun setupPeriodicWork() {
        val syncWorkRequest = PeriodicWorkRequestBuilder<DataSyncWorker>(15, TimeUnit.MINUTES).build()
        WorkManager.getInstance(this).enqueue(syncWorkRequest)
    }
}