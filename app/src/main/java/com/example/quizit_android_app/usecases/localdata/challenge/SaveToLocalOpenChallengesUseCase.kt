package com.example.quizit_android_app.usecases.localdata.challenge

import com.example.quizit_android_app.model.ContentDataStore
import com.example.quizit_android_app.model.retrofit.OpenChallenges
import javax.inject.Inject

class SaveToLocalOpenChallengesUseCase @Inject constructor(
    val contentDataStore: ContentDataStore
) {
    suspend operator fun invoke(newOpenChallenges: List<OpenChallenges>) {
        val existingOpenChallenges = contentDataStore.getOpenChallenges().toMutableList()
        newOpenChallenges.forEach { newOpenChallenge ->
            if (existingOpenChallenges.none { it.challengeId == newOpenChallenge.challengeId }) {
                existingOpenChallenges.add(newOpenChallenge)
            }
        }
        contentDataStore.saveOpenChallenges(existingOpenChallenges)
    }
}