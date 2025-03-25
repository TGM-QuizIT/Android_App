package com.example.quizit_android_app.usecases.challenge

import android.util.Log
import com.example.quizit_android_app.model.ContentDataStore
import com.example.quizit_android_app.model.retrofit.DataRepo
import com.example.quizit_android_app.model.retrofit.OpenChallengesResponse
import javax.inject.Inject

class GetChallengesOfUserUseCase @Inject constructor(
    val dataRepo: DataRepo,
    val contentDataStore: ContentDataStore
) {
    suspend operator fun invoke(): OpenChallengesResponse {
        val localData = contentDataStore.getOpenChallenges()
        return if (localData.isNotEmpty()) {
            Log.d("GetChallengesOfUser - local data", localData.toString())
            OpenChallengesResponse("success", ArrayList(localData.sortedByDescending { it.challengeDateTime }))
        } else {
            val remoteData = dataRepo.fetchAllOpenChallenges()
            val sortedRemoteData = remoteData.openChallenges.sortedByDescending { it.challengeDateTime }
            contentDataStore.saveOpenChallenges(sortedRemoteData)
            Log.d("GetChallengesOfUser", "Remote data")
            OpenChallengesResponse("Success", ArrayList(sortedRemoteData))
        }
    }
}