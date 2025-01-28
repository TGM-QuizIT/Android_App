package com.example.quizit_android_app.usecases.Result

import com.example.quizit_android_app.model.DataRepo
import com.example.quizit_android_app.model.Result
import javax.inject.Inject

class GetResultsSubjectUseCase @Inject constructor(
    val dataRepo: DataRepo
) {
    suspend operator fun invoke(subjectId: Int, amount: Int? = null): List<Result> {
        return dataRepo.fetchResultsOfSubject(subjectId, amount)
    }
}