package com.example.quizit_android_app.usecases.challenge

import com.example.quizit_android_app.model.ContentDataStore
import com.example.quizit_android_app.model.retrofit.DataRepo
import com.example.quizit_android_app.model.retrofit.OpenChallengesResponse
import javax.inject.Inject

class GetChallengesOfUserUseCase @Inject constructor(
    val dataRepo: DataRepo,
    val contentDataStore: ContentDataStore
) {
    suspend operator fun invoke(): OpenChallengesResponse{
        val localData = contentDataStore.getOpenChallenges()
        return if (localData.isNotEmpty()) {
            OpenChallengesResponse("success", ArrayList(localData))
        } else {
            val remoteData = dataRepo.fetchAllOpenChallenges()
            contentDataStore.saveOpenChallenges(remoteData.openChallenges)
            remoteData
        }
    }
}