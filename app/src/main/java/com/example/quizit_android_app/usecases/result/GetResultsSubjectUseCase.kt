package com.example.quizit_android_app.usecases.result

import com.example.quizit_android_app.model.ContentDataStore
import com.example.quizit_android_app.model.retrofit.DataRepo
import com.example.quizit_android_app.model.retrofit.Result
import javax.inject.Inject

class GetResultsSubjectUseCase @Inject constructor(
    val dataRepo: DataRepo,
    val contentDataStore: ContentDataStore
) {
    suspend operator fun invoke(subjectId: Int, amount: Int? = null): List<Result> {
        val localData = contentDataStore.getResults().filter { it.subject?.subjectId == subjectId }
            .sortedWith(compareByDescending<Result> { it.resultScore }.thenByDescending { it.resultDateTime })
        return localData.ifEmpty {
            val remoteData = dataRepo.fetchResultsOfUser()
            contentDataStore.saveResults(remoteData)
            remoteData.filter { it.subject?.subjectId == subjectId }
                .sortedWith(compareByDescending<Result> { it.resultScore }.thenByDescending { it.resultDateTime })
        }
    }
}