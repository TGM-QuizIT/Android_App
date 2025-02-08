package com.example.quizit_android_app.usecases.localdata.userstats

import com.example.quizit_android_app.model.ContentDataStore
import javax.inject.Inject

class GetLocalUserStatsUseCase @Inject constructor(
    val contentDataStore: ContentDataStore
) {
    suspend operator fun invoke() = contentDataStore.getStats()
}