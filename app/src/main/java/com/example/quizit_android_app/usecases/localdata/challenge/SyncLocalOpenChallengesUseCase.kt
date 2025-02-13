package com.example.quizit_android_app.usecases.localdata.challenge

import com.example.quizit_android_app.model.ContentDataStore
import com.example.quizit_android_app.model.retrofit.DataRepo
import javax.inject.Inject

class SyncLocalOpenChallengesUseCase @Inject constructor(
    val dataRepo: DataRepo,
    val contentDataStore: ContentDataStore
) {
    suspend operator fun invoke() {
        val openChallenges = dataRepo.fetchAllOpenChallenges()
        if (openChallenges.status == "Success") {
            contentDataStore.saveOpenChallenges(openChallenges.openChallenges)
        }
    }
}