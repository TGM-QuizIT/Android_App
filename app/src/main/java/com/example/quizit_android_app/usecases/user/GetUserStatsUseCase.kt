package com.example.quizit_android_app.usecases.user

import android.util.Log
import com.example.quizit_android_app.model.DataRepo
import com.example.quizit_android_app.model.UserStatsResponse
import javax.inject.Inject

class GetUserStatsUseCase @Inject constructor(
    val dataRepo: DataRepo
) {
    suspend operator fun invoke(): UserStatsResponse? {
        val stats = dataRepo.fetchUserStats()
        Log.d("GetUserStatsUseCase", "invoke: $stats")
        return stats
    }
}