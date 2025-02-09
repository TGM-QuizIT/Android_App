package com.example.quizit_android_app.usecases.friendship

import com.example.quizit_android_app.model.retrofit.DataRepo
import javax.inject.Inject

class DeleteDeclineFriendshipUseCase @Inject constructor(
    val dataRepo: DataRepo
) {
    suspend operator fun invoke(friendshipId: Int) {
        dataRepo.deleteFriendship(friendshipId)
    }
}