package com.example.quizit_android_app.usecases.friendship

import com.example.quizit_android_app.model.DataRepo
import javax.inject.Inject

class AddFriendshipUseCase @Inject constructor(
    val dataRepo: DataRepo
) {
    suspend operator fun invoke(friendId: Int) {
        dataRepo.addFriendship(friendId)
    }
}