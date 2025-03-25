package com.example.quizit_android_app.usecases.challenge

import com.example.quizit_android_app.model.retrofit.AssignResultToChallengeResponse
import com.example.quizit_android_app.model.retrofit.DataRepo
import javax.inject.Inject

class AddChallengeForSubjectUseCase @Inject constructor(
    val dataRepo: DataRepo
) {
    suspend operator fun invoke(friendshipId: Int, subjectId: Int): AssignResultToChallengeResponse {
        return dataRepo.addChallengeForSubject(friendshipId, subjectId)
    }
}