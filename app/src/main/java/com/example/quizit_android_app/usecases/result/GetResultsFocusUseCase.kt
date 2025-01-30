package com.example.quizit_android_app.usecases.result

import com.example.quizit_android_app.model.DataRepo
import com.example.quizit_android_app.model.Result
import javax.inject.Inject

class GetResultsFocusUseCase @Inject constructor(
    val dataRepo: DataRepo
) {
    suspend operator fun invoke(focusId: Int, amount: Int? = null) : List<Result> {
        return dataRepo.fetchResultsOfFocus(focusId, amount)
    }
}