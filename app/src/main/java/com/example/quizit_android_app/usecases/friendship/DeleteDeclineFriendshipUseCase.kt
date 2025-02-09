package com.example.quizit_android_app.usecases.friendship

import com.example.quizit_android_app.model.retrofit.DataRepo
import com.example.quizit_android_app.model.retrofit.StatusResponse
import com.example.quizit_android_app.usecases.localdata.friendship.SyncLocalPendingFriendsUseCase
import javax.inject.Inject

class DeleteDeclineFriendshipUseCase @Inject constructor(
    val dataRepo: DataRepo,
    val syncLocalPendingFriendsUseCase: SyncLocalPendingFriendsUseCase
) {
    suspend operator fun invoke(friendshipId: Int): StatusResponse {
        val response = dataRepo.deleteFriendship(friendshipId)
        if (response.status != null) {
            syncLocalPendingFriendsUseCase()
        }
        return response
    }
}