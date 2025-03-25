package com.example.quizit_android_app.usecases.result

import com.example.quizit_android_app.model.ContentDataStore
import com.example.quizit_android_app.model.retrofit.DataRepo
import com.example.quizit_android_app.model.retrofit.Result
import javax.inject.Inject

class GetResultsFocusUseCase @Inject constructor(
    val dataRepo: DataRepo,
    val contentDataStore: ContentDataStore
) {
    suspend operator fun invoke(focusId: Int, amount: Int? = null): List<Result> {
        val localData = contentDataStore.getResults().filter { it.focus?.focusId == focusId }
            .sortedWith(compareByDescending<Result> { it.resultScore }.thenByDescending { it.resultDateTime })
        return localData.ifEmpty {
            val remoteData = dataRepo.fetchResultsOfUser()
            // Save the results to the local storage only when remoteData is not empty
            if (remoteData.isNotEmpty()) {
                contentDataStore.saveResults(remoteData)
            }
            remoteData.filter { it.focus?.focusId == focusId }
                .sortedWith(compareByDescending<Result> { it.resultScore }.thenByDescending { it.resultDateTime })
        }
    }
}