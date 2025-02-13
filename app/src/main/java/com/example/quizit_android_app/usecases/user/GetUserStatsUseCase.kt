package com.example.quizit_android_app.usecases.user

import android.util.Log
import com.example.quizit_android_app.model.ContentDataStore
import com.example.quizit_android_app.model.retrofit.DataRepo
import com.example.quizit_android_app.model.retrofit.UserStatsResponse
import javax.inject.Inject

class GetUserStatsUseCase @Inject constructor(
    val dataRepo: DataRepo,
    val contentDataStore: ContentDataStore
) {
    suspend operator fun invoke(userId: Int? = null): UserStatsResponse {
        val localData = contentDataStore.getStats()
        val allNonNull = listOf(localData.ranking, localData.avgPoints, localData.winRate).all { it != null }

        return if (allNonNull && userId == null) {
            Log.d("GetUserStatsUseCase", "invoke: using local data")
            UserStatsResponse("success", localData)
        } else {
            Log.d("GetUserStatsUseCase", "invoke: fetching remote data")
            val remoteData = dataRepo.fetchUserStats(userId)
            if (userId == null) {
                contentDataStore.saveStats(remoteData.stats)
            }
            remoteData
        }

    }
}