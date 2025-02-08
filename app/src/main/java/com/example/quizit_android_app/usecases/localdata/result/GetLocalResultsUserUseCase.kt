package com.example.quizit_android_app.usecases.localdata.result

import com.example.quizit_android_app.model.ContentDataStore
import javax.inject.Inject

class GetLocalResultsUserUseCase @Inject constructor(
    val contentDataStore: ContentDataStore
) {
    suspend operator fun invoke() = contentDataStore.getResults()
}