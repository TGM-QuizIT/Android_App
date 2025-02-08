package com.example.quizit_android_app.usecases.localdata.challenge

import com.example.quizit_android_app.model.ContentDataStore
import com.example.quizit_android_app.model.retrofit.DataRepo
import javax.inject.Inject

class SyncLocalDoneChallengesUseCase @Inject constructor(
    private val dataRepo: DataRepo,
    private val contentDataStore: ContentDataStore
) {
    suspend operator fun invoke() {
        val doneChallenges = dataRepo.getDoneChallenges()
        contentDataStore.saveDoneChallenges(doneChallenges.doneChallenges)
    }
}