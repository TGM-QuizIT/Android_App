package com.example.quizit_android_app.usecases.friendship

import com.example.quizit_android_app.model.retrofit.DataRepo
import com.example.quizit_android_app.model.retrofit.FriendshipResponse
import com.example.quizit_android_app.usecases.localdata.friendship.SyncLocalAcceptedFriendsUseCase
import com.example.quizit_android_app.usecases.localdata.friendship.SyncLocalPendingFriendsUseCase
import javax.inject.Inject

class AddFriendshipUseCase @Inject constructor(
    val dataRepo: DataRepo,
    val syncLocalAcceptedFriendsUseCase: SyncLocalAcceptedFriendsUseCase,
    val syncLocalPendingFriendsUseCase: SyncLocalPendingFriendsUseCase
) {
    suspend operator fun invoke(friendId: Int): FriendshipResponse {
        val response = dataRepo.addFriendship(friendId)
        if (response.status == "Success") {
            syncLocalPendingFriendsUseCase()
            syncLocalAcceptedFriendsUseCase()
        }
        return response
    }
}