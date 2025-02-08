package com.example.quizit_android_app.usecases.localdata.result

import com.example.quizit_android_app.model.ContentDataStore
import com.example.quizit_android_app.model.retrofit.Result
import javax.inject.Inject

class SaveToLocalResultsUseCase @Inject constructor(
    val contentDataStore: ContentDataStore
) {
    suspend operator fun invoke(results: List<Result>) {
        contentDataStore.saveResults(results)
    }
}