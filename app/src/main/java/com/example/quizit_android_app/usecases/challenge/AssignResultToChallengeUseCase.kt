package com.example.quizit_android_app.usecases.challenge

import com.example.quizit_android_app.model.retrofit.AssignResultToChallengeResponse
import com.example.quizit_android_app.model.retrofit.DataRepo
import javax.inject.Inject

class AssignResultToChallengeUseCase @Inject constructor(
    val dataRepo: DataRepo
) {
    suspend operator fun invoke(challengeId: Int, result: Int): AssignResultToChallengeResponse {
        return dataRepo.assignResultToChallenge(challengeId, result)
    }
}