package com.example.quizit_android_app.usecases.challenge

import com.example.quizit_android_app.model.DataRepo
import javax.inject.Inject

class GetChallengesForSubjectUseCase @Inject constructor(
    val dataRepo: DataRepo
) {
    suspend operator fun invoke(subjectId: Int) {
        dataRepo.getChallengesForSubject(subjectId)
    }
}