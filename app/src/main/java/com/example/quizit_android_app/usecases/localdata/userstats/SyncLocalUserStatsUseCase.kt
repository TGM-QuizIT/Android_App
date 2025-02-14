package com.example.quizit_android_app.usecases.localdata.userstats

import android.util.Log
import com.example.quizit_android_app.model.ContentDataStore
import com.example.quizit_android_app.model.retrofit.DataRepo
import javax.inject.Inject

class SyncLocalUserStatsUseCase @Inject constructor(
    val dataRepo: DataRepo,
    val contentDataStore: ContentDataStore
) {
    suspend operator fun invoke() {
        val stats = dataRepo.fetchUserStats()
        if (stats.status == "Success") {
            contentDataStore.saveStats(stats.stats)
            Log.d("SyncLocalUserStatsUseCase", "user stats synced")
        }
    }
}