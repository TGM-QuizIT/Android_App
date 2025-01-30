package com.example.quizit_android_app.usecases.result

import com.example.quizit_android_app.model.DataRepo
import javax.inject.Inject

class AddResultSubjectUseCase @Inject constructor(
    val dataRepo: DataRepo
) {
    suspend operator fun invoke(subjectId: Int, score: Double) {
        dataRepo.postResultOfSubject(subjectId, score)
    }
}