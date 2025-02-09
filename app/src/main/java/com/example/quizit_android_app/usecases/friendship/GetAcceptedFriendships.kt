package com.example.quizit_android_app.usecases.friendship

import com.example.quizit_android_app.model.retrofit.AcceptedFriendships
import com.example.quizit_android_app.model.retrofit.DataRepo
import javax.inject.Inject

class GetAcceptedFriendships @Inject constructor(
    val dataRepo: DataRepo
) {
    suspend operator fun invoke() : List<AcceptedFriendships> {
        val allFriendShip = dataRepo.fetchAllFriends()
        return allFriendShip?.acceptedFriendships ?: emptyList()
    }
}