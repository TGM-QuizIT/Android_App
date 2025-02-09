package com.example.quizit_android_app.workers

import android.content.Context
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
import javax.inject.Inject

class DataSyncWorker @Inject constructor(
    context: Context,
    workerParams: WorkerParameters,
    private val syncLocalOpenChallengesUseCase: SyncLocalOpenChallengesUseCase,
    private val syncLocalDoneChallengesUseCase: SyncLocalDoneChallengesUseCase,
    private val syncLocalFocusUseCase: SyncLocalFocusUseCase,
    private val syncLocalPendingFriendsUseCase: SyncLocalPendingFriendsUseCase,
    private val syncLocalAcceptedFriendsUseCase: SyncLocalAcceptedFriendsUseCase,
    private val syncLocalResultsUseCase: SyncLocalResultsUseCase,
    private val syncLocalSubjectsUseCase: SyncLocalSubjectsUseCase,
    private val syncLocalUserStatsUseCase: SyncLocalUserStatsUseCase
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
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
            Result.retry()
        }
    }
}