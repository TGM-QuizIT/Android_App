package com.example.quizit_android_app.usecases.friendship

import com.example.quizit_android_app.model.DataRepo
import com.example.quizit_android_app.model.FriendshipResponse
import javax.inject.Inject

class AcceptFriendshipUseCase @Inject constructor(
    val dataRepo: DataRepo
) {
    suspend operator fun invoke(friendId: Int): FriendshipResponse {
        return dataRepo.acceptFriendship(friendId)
    }
}