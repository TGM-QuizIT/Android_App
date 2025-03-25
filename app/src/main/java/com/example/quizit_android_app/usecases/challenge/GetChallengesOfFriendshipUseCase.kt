package com.example.quizit_android_app.usecases.challenge

import android.util.Log
import androidx.compose.ui.text.resolveDefaults
import com.example.quizit_android_app.model.retrofit.ChallengeResponse
import com.example.quizit_android_app.model.retrofit.DataRepo
import java.util.ArrayList
import javax.inject.Inject

class GetChallengesOfFriendshipUseCase @Inject constructor(
    val dataRepo: DataRepo
) {
    suspend operator fun invoke(friendshipId: Int): ChallengeResponse {
        val response = dataRepo.getChallengesOfFriendship(friendshipId)
        Log.d("GetChallengesOfFriendshipUseCase", "invoke $friendshipId: $response")
        val sortedOpen = response.openChallenges.sortedByDescending { it.challengeDateTime }
        val sortedDone = response.doneChallenges.sortedByDescending { it.challengeDateTime }
        return ChallengeResponse("Success", ArrayList(sortedOpen), ArrayList(sortedDone))
    }
}