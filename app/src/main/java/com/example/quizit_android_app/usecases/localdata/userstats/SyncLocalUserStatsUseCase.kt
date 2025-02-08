package com.example.quizit_android_app.usecases.localdata.userstats

import com.example.quizit_android_app.model.ContentDataStore
import com.example.quizit_android_app.model.retrofit.DataRepo
import javax.inject.Inject

class SyncLocalUserStatsUseCase @Inject constructor(
    val dataRepo: DataRepo,
    val contentDataStore: ContentDataStore
) {
    suspend operator fun invoke() {
        val stats = dataRepo.fetchUserStats()
        contentDataStore.saveStats(stats.stats)
    }
}