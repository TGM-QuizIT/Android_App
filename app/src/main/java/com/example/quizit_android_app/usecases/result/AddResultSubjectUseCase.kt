package com.example.quizit_android_app.usecases.result

import com.example.quizit_android_app.model.DataRepo
import com.example.quizit_android_app.model.GetResultsResponse
import javax.inject.Inject

class AddResultSubjectUseCase @Inject constructor(
    val dataRepo: DataRepo
) {
    suspend operator fun invoke(subjectId: Int, score: Double): GetResultsResponse {
        return dataRepo.postResultOfSubject(subjectId, score)
    }
}