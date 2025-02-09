package com.example.quizit_android_app.usecases.challenge

import com.example.quizit_android_app.model.ContentDataStore
import com.example.quizit_android_app.model.retrofit.DataRepo
import com.example.quizit_android_app.model.retrofit.DoneChallengesResponse
import java.util.ArrayList
import javax.inject.Inject

class GetDoneChallengesUseCase @Inject constructor(
    val dataRepo: DataRepo,
    val contentDataStore: ContentDataStore
) {
    suspend operator fun invoke(): DoneChallengesResponse {
        val localData = contentDataStore.getDoneChallenges()
        return if (localData.isNotEmpty()) {
            DoneChallengesResponse("success", ArrayList(localData))
        } else {
            val remoteData = dataRepo.getDoneChallenges()
            contentDataStore.saveDoneChallenges(remoteData.doneChallenges)
            remoteData
        }
    }
}