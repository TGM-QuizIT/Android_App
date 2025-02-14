package com.example.quizit_android_app.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.quizit_android_app.usecases.localdata.challenge.SyncLocalDoneChallengesUseCase
import com.example.quizit_android_app.usecases.localdata.challenge.SyncLocalOpenChallengesUseCase
import com.example.quizit_android_app.usecases.localdata.focus.SyncLocalFocusUseCase
import com.example.quizit_android_app.usecases.localdata.friendship.SyncLocalAcceptedFriendsUseCase
import com.example.quizit_android_app.usecases.localdata.friendship.SyncLocalPendingFriendsUseCase
import com.example.quizit_android_app.usecases.localdata.result.SyncLocalResultsUseCase
import com.example.quizit_android_app.usecases.localdata.subjects.SyncLocalSubjectsUseCase
import com.example.quizit_android_app.usecases.localdata.userstats.SyncLocalUserStatsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import javax.inject.Inject

@HiltWorker
class DataSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val syncLocalSubjectsUseCase: SyncLocalSubjectsUseCase,
    private val syncLocalOpenChallengesUseCase: SyncLocalOpenChallengesUseCase,
    private val syncLocalDoneChallengesUseCase: SyncLocalDoneChallengesUseCase,
    private val syncLocalFocusUseCase: SyncLocalFocusUseCase,
    private val syncLocalAcceptedFriendsUseCase: SyncLocalAcceptedFriendsUseCase,
    private val syncLocalPendingFriendsUseCase: SyncLocalPendingFriendsUseCase,
    private val syncLocalResultsUseCase: SyncLocalResultsUseCase,
    private val syncLocalUserStatsUseCase: SyncLocalUserStatsUseCase
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            Log.d("DataSyncWorker", "doWork")
            syncLocalOpenChallengesUseCase()
            syncLocalDoneChallengesUseCase()
            syncLocalFocusUseCase()
            syncLocalPendingFriendsUseCase()
            syncLocalAcceptedFriendsUseCase()
            syncLocalResultsUseCase()
            syncLocalSubjectsUseCase()
            syncLocalUserStatsUseCase()

            Result.success()
        } catch (e: Exception) {
            Log.e("DataSyncWorker", "Error: ${e.message}")
            Result.retry()
        }
    }
}