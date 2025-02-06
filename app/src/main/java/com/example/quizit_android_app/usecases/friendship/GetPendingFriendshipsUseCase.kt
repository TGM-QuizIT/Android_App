package com.example.quizit_android_app.usecases.friendship

import com.example.quizit_android_app.model.DataRepo
import com.example.quizit_android_app.model.PendingFriendships
import javax.inject.Inject

class GetPendingFriendshipsUseCase @Inject constructor(
    val dataRepo: DataRepo
) {
    suspend operator fun invoke() : List<PendingFriendships> {
        val allFriendShip = dataRepo.fetchAllFriends()
        return allFriendShip?.pendingFriendships ?: emptyList()
    }
}