package com.example.quizit_android_app.usecases.challenge

import com.example.quizit_android_app.model.retrofit.ChallengeResponse
import com.example.quizit_android_app.model.retrofit.DataRepo
import javax.inject.Inject

class GetChallengesOfFriendshipUseCase @Inject constructor(
    val dataRepo: DataRepo
) {
    suspend operator fun invoke(friendshipId: Int): ChallengeResponse {
        return dataRepo.getChallengesOfFriendship(friendshipId)
    }
}