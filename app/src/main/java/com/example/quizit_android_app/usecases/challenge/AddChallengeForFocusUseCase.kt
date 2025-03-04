package com.example.quizit_android_app.usecases.challenge

import android.util.Log
import com.example.quizit_android_app.model.retrofit.AssignResultToChallengeResponse
import com.example.quizit_android_app.model.retrofit.DataRepo
import javax.inject.Inject

class AddChallengeForFocusUseCase @Inject constructor(
    val dataRepo: DataRepo
) {
    suspend operator fun invoke(friendshipId: Int, focusId: Int): AssignResultToChallengeResponse {
        Log.d("AddChallengeForFocusUseCase", "Adding challenge for focus $focusId to friendship $friendshipId")
        return dataRepo.addChallengeForFocus(friendshipId, focusId)
    }
}