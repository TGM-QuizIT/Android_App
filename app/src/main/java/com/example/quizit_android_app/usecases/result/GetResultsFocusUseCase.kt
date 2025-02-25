package com.example.quizit_android_app.usecases.result

import com.example.quizit_android_app.model.retrofit.DataRepo
import com.example.quizit_android_app.model.retrofit.Result
import javax.inject.Inject

class GetResultsFocusUseCase @Inject constructor(
    val dataRepo: DataRepo
) {
    suspend operator fun invoke(focusId: Int, amount: Int? = null) : List<Result> {
        val response = dataRepo.fetchResultsOfFocus(focusId, amount)
        val sorted = response.sortedWith(compareByDescending<Result> { it.resultScore}
            .thenByDescending { it.resultDateTime })
        return sorted
    }
}