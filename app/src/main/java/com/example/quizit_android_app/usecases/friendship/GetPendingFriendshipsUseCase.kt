package com.example.quizit_android_app.usecases.friendship

import com.example.quizit_android_app.model.retrofit.DataRepo
import com.example.quizit_android_app.model.retrofit.PendingFriendships
import javax.inject.Inject

class GetPendingFriendshipsUseCase @Inject constructor(
    val dataRepo: DataRepo
) {
    suspend operator fun invoke() : List<PendingFriendships> {
        val allFriendShip = dataRepo.fetchAllFriends()
        return allFriendShip?.pendingFriendships ?: emptyList()
    }
}