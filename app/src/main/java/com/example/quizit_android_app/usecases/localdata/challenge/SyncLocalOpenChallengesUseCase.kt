package com.example.quizit_android_app.usecases.localdata.challenge

import android.util.Log
import com.example.quizit_android_app.model.ContentDataStore
import com.example.quizit_android_app.model.retrofit.DataRepo
import javax.inject.Inject

class SyncLocalOpenChallengesUseCase @Inject constructor(
    private val dataRepo: DataRepo,
    private val contentDataStore: ContentDataStore
) {
    suspend operator fun invoke() {
        val openChallenges = dataRepo.fetchAllOpenChallenges()
        if (openChallenges.status == "Success") {
            contentDataStore.saveOpenChallenges(openChallenges.openChallenges)
            Log.d("SyncLocalOpenChallengesUseCase", "Open Challenges synced")
        }
    }
}