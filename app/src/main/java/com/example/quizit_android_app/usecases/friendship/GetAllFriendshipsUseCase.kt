package com.example.quizit_android_app.usecases.friendship

import com.example.quizit_android_app.model.AllFriendshipResponse
import com.example.quizit_android_app.model.DataRepo
import javax.inject.Inject

class GetAllFriendshipsUseCase @Inject constructor(
    val dataRepo: DataRepo
) {
    suspend operator fun invoke() : AllFriendshipResponse? {
        val allFriendShip = dataRepo.fetchAllFriends()
        return allFriendShip
    }
}