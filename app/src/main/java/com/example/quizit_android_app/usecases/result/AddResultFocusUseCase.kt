package com.example.quizit_android_app.usecases.result

import com.example.quizit_android_app.model.retrofit.DataRepo
import com.example.quizit_android_app.model.retrofit.GetResultsResponse
import com.example.quizit_android_app.model.retrofit.GetSingleResultsResponse
import com.example.quizit_android_app.usecases.localdata.result.SyncLocalResultsUseCase
import javax.inject.Inject

class AddResultFocusUseCase @Inject constructor(
    val dataRepo: DataRepo,
    val syncLocalResultsUseCase: SyncLocalResultsUseCase
) {
    suspend operator fun invoke(focusId: Int, score: Double): GetSingleResultsResponse {
        val response = dataRepo.postResultOfFocus(focusId, score)
        if (response.status != null) {
            syncLocalResultsUseCase()
        }
        return response
    }
}