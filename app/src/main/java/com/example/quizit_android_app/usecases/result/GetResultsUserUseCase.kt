package com.example.quizit_android_app.usecases.result

import com.example.quizit_android_app.model.ContentDataStore
import com.example.quizit_android_app.model.retrofit.DataRepo
import com.example.quizit_android_app.model.retrofit.Result
import javax.inject.Inject

class GetResultsUserUseCase @Inject constructor(
    val dataRepo: DataRepo,
    val contentDataStore: ContentDataStore
){
    suspend operator fun invoke(amount: Int? = null) : List<Result> {
        val localData = contentDataStore.getResults().sortedBy { it.resultDateTime }
        return localData.ifEmpty {
            val remoteData = dataRepo.fetchResultsOfUser(amount)
            contentDataStore.saveResults(remoteData.sortedBy { it.resultDateTime })
            remoteData
        }
    }
}