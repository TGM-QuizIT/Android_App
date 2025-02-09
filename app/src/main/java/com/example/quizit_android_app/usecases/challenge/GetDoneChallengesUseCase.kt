package com.example.quizit_android_app.usecases.challenge

import com.example.quizit_android_app.model.retrofit.DataRepo
import com.example.quizit_android_app.model.retrofit.DoneChallengesResponse
import javax.inject.Inject

class GetDoneChallengesUseCase @Inject constructor(
    val dataRepo: DataRepo
) {
    suspend operator fun invoke(): DoneChallengesResponse {
        return dataRepo.getDoneChallenges()
    }
}