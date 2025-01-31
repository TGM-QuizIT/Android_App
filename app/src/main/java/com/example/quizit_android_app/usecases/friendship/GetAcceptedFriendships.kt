package com.example.quizit_android_app.usecases.friendship

import com.example.quizit_android_app.model.AcceptedFriendships
import com.example.quizit_android_app.model.DataRepo
import javax.inject.Inject

class GetAcceptedFriendships @Inject constructor(
    val dataRepo: DataRepo
) {
    suspend operator fun invoke() : List<AcceptedFriendships> {
        val allFriendShip = dataRepo.fetchAllFriends()
        return allFriendShip?.acceptedFriendships ?: emptyList()
    }
}