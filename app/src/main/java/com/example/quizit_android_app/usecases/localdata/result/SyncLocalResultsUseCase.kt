package com.example.quizit_android_app.usecases.localdata.result

import com.example.quizit_android_app.model.ContentDataStore
import com.example.quizit_android_app.model.retrofit.DataRepo
import javax.inject.Inject

class SyncLocalResultsUseCase @Inject constructor(
    val dataRepo: DataRepo,
    val contentDataStore: ContentDataStore
){
    suspend operator fun invoke(){
        val results = dataRepo.fetchResultsOfUser()
        contentDataStore.saveResults(results)
    }
}