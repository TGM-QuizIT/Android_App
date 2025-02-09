package com.example.quizit_android_app.usecases.localdata.userstats

import com.example.quizit_android_app.model.ContentDataStore
import com.example.quizit_android_app.model.retrofit.Stats
import javax.inject.Inject

class SaveToLocalUserStatsUseCase @Inject constructor(
    val contentDataStore: ContentDataStore
) {
    suspend operator fun invoke(stats: Stats) {
        contentDataStore.saveStats(stats)
    }
}