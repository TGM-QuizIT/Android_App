package com.example.quizit_android_app.usecases.result

import com.example.quizit_android_app.model.retrofit.DataRepo
import com.example.quizit_android_app.model.retrofit.GetResultsResponse
import javax.inject.Inject

class AddResultFocusUseCase @Inject constructor(
    val dataRepo: DataRepo
) {
    suspend operator fun invoke(focusId: Int, score: Double): GetResultsResponse {
        return dataRepo.postResultOfFocus(focusId, score)
    }
}