package com.example.quizit_android_app.usecases.challenge

import com.example.quizit_android_app.model.retrofit.AssignResultToChallengeResponse
import com.example.quizit_android_app.model.retrofit.DataRepo
import com.example.quizit_android_app.usecases.localdata.challenge.SyncLocalDoneChallengesUseCase
import com.example.quizit_android_app.usecases.localdata.challenge.SyncLocalOpenChallengesUseCase
import javax.inject.Inject

class AssignResultToChallengeUseCase @Inject constructor(
    val dataRepo: DataRepo,
    val syncLocalDoneChallengesUseCase: SyncLocalDoneChallengesUseCase,
    val syncLocalOpenChallengesUseCase: SyncLocalOpenChallengesUseCase
) {
    suspend operator fun invoke(challengeId: Int, result: Int): AssignResultToChallengeResponse {
        val response = dataRepo.assignResultToChallenge(challengeId, result)
        if (response.status != null) {
            syncLocalOpenChallengesUseCase()
            syncLocalDoneChallengesUseCase()
        }
        return response

    }
}