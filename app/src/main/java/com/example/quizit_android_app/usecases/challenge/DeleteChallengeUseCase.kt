package com.example.quizit_android_app.usecases.challenge

import com.example.quizit_android_app.model.retrofit.DataRepo
import com.example.quizit_android_app.usecases.localdata.challenge.SyncLocalOpenChallengesUseCase
import javax.inject.Inject

class DeleteChallengeUseCase @Inject constructor(
    val dataRepo: DataRepo,
    val syncLocalOpenChallengesUseCase: SyncLocalOpenChallengesUseCase
) {
    suspend operator fun invoke(challengeId: Int) {
        val statusResponse = dataRepo.deleteChallenge(challengeId)
        if (statusResponse.status != null) {
            syncLocalOpenChallengesUseCase()
        }
    }
}