package com.example.quizit_android_app.usecases.challenge

import android.util.Log
import com.example.quizit_android_app.model.retrofit.ChallengeResponse
import com.example.quizit_android_app.model.retrofit.DataRepo
import javax.inject.Inject

class GetChallengesOfFriendshipUseCase @Inject constructor(
    val dataRepo: DataRepo
) {
    suspend operator fun invoke(friendshipId: Int): ChallengeResponse {
        val response = dataRepo.getChallengesOfFriendship(friendshipId)
        Log.d("GetChallengesOfFriendshipUseCase", "invoke $friendshipId: $response")
        return response
    }
}