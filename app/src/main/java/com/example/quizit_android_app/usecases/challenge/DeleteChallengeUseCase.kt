package com.example.quizit_android_app.usecases.challenge

import com.example.quizit_android_app.model.retrofit.DataRepo
import javax.inject.Inject

class DeleteChallengeUseCase @Inject constructor(
    val dataRepo: DataRepo
) {
    suspend operator fun invoke(challengeId: Int) {
        dataRepo.deleteChallenge(challengeId)
    }
}