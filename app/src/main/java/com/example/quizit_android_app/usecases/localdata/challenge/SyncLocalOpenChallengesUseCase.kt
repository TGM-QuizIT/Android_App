package com.example.quizit_android_app.usecases.localdata.challenge

import com.example.quizit_android_app.model.ContentDataStore
import com.example.quizit_android_app.model.retrofit.DataRepo
import javax.inject.Inject

class SyncLocalOpenChallengesUseCase @Inject constructor(
    val dataRepo: DataRepo,
    val contentDataStore: ContentDataStore
) {
    suspend operator fun invoke() {
        // TODO: alle offenen challenges eines users holen
        //val openChallenges = dataRepo.getOpenChallenges()
        //contentDataStore.saveOpenChallenges(openChallenges.openChallenges)
    }
}