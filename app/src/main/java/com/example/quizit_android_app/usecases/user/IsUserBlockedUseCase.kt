package com.example.quizit_android_app.usecases.user

import com.example.quizit_android_app.model.retrofit.DataRepo
import com.example.quizit_android_app.model.retrofit.UserBlockedResponse
import javax.inject.Inject

class IsUserBlockedUseCase @Inject constructor(
    val dataRepo: DataRepo
) {
    suspend operator fun invoke(): UserBlockedResponse {
        return dataRepo.isUserBlocked()
    }
}