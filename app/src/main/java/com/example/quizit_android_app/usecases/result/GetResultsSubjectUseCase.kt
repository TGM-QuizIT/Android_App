package com.example.quizit_android_app.usecases.result

import com.example.quizit_android_app.model.retrofit.DataRepo
import com.example.quizit_android_app.model.retrofit.Result
import javax.inject.Inject

class GetResultsSubjectUseCase @Inject constructor(
    val dataRepo: DataRepo
) {
    suspend operator fun invoke(subjectId: Int, amount: Int? = null): List<Result> {
        return dataRepo.fetchResultsOfSubject(subjectId, amount)
    }
}