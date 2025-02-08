package com.example.quizit_android_app.usecases.localdata.challenge

import com.example.quizit_android_app.model.ContentDataStore
import com.example.quizit_android_app.model.retrofit.DoneChallenges
import javax.inject.Inject

class SaveToLocalDoneChallengesUseCase @Inject constructor(
    val contentDataStore: ContentDataStore
) {
    suspend operator fun invoke(doneChallenges: List<DoneChallenges>) {
        contentDataStore.saveDoneChallenges(doneChallenges)
    }
}