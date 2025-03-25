package com.example.quizit_android_app

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.quizit_android_app.model.retrofit.OpenChallenges
import com.example.quizit_android_app.ui.login.LoginScreen
import com.example.quizit_android_app.usecases.localdata.challenge.SyncLocalDoneChallengesUseCase
import com.example.quizit_android_app.usecases.localdata.challenge.SyncLocalOpenChallengesUseCase
import com.example.quizit_android_app.usecases.localdata.focus.SyncLocalFocusUseCase
import com.example.quizit_android_app.usecases.localdata.friendship.SyncLocalAcceptedFriendsUseCase
import com.example.quizit_android_app.usecases.localdata.friendship.SyncLocalPendingFriendsUseCase
import com.example.quizit_android_app.usecases.localdata.result.SyncLocalResultsUseCase
import com.example.quizit_android_app.usecases.localdata.subjects.SyncLocalSubjectsUseCase
import com.example.quizit_android_app.usecases.localdata.userstats.SyncLocalUserStatsUseCase
import com.example.quizit_android_app.workers.DataSyncWorker
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class QuizITApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var syncLocalSubjectsUseCase: SyncLocalSubjectsUseCase

    @Inject
    lateinit var syncLocalOpenChallengesUseCase: SyncLocalOpenChallengesUseCase

    @Inject
    lateinit var syncLocalDoneChallengesUseCase: SyncLocalDoneChallengesUseCase

    @Inject
    lateinit var syncLocalFocusUseCase: SyncLocalFocusUseCase

    @Inject
    lateinit var syncLocalAcceptedFriendsUseCase: SyncLocalAcceptedFriendsUseCase

    @Inject
    lateinit var syncLocalPendingFriendsUseCase: SyncLocalPendingFriendsUseCase

    @Inject
    lateinit var syncLocalResultsUseCase: SyncLocalResultsUseCase

    @Inject
    lateinit var syncLocalUserStatsUseCase: SyncLocalUserStatsUseCase

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        WorkManager.initialize(this, workManagerConfiguration)
        callSyncMethods()
        setupPeriodicWork()
    }

    private fun callSyncMethods() {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("QuizITApplication", "on Create callSyncMethods()")
            syncLocalSubjectsUseCase()
            syncLocalOpenChallengesUseCase()
            syncLocalDoneChallengesUseCase()
            syncLocalFocusUseCase()
            syncLocalAcceptedFriendsUseCase()
            syncLocalPendingFriendsUseCase()
            syncLocalResultsUseCase()
            syncLocalUserStatsUseCase()
        }
    }


    private fun setupPeriodicWork() {
        Log.d("QuizITApplication", "setupPeriodicWork()")
        val syncWorkRequest = PeriodicWorkRequestBuilder<DataSyncWorker>(15, TimeUnit.MINUTES).build()
        WorkManager.getInstance(this).enqueue(syncWorkRequest)
    }
}