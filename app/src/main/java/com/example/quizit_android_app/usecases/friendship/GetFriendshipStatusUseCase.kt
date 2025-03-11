package com.example.quizit_android_app.usecases.friendship

import android.util.Log
import com.example.quizit_android_app.model.retrofit.DataRepo
import javax.inject.Inject

class GetFriendshipStatusUseCase @Inject constructor(
    val dataRepo: DataRepo
) {
    suspend operator fun invoke(friendshipId: Int): FriendshipStatus {
        Log.d("GetFriendshipStatusUseCase", "FriendshipId: "+friendshipId)
        val friendships = dataRepo.fetchAllFriends()
        val acceptedFriends = friendships?.acceptedFriendships
        Log.d("GetFriendshipStatusUseCase", "AcceptedFriends: "+acceptedFriends)

        val pendingFriends = friendships?.pendingFriendships
        Log.d("GetFriendshipStatusUseCase", "PendingFriends: "+pendingFriends)

        acceptedFriends?.find { it.friendshipId == friendshipId }?.let {
            return FriendshipStatus.FRIENDS
        }

        pendingFriends?.find { it.friendshipId == friendshipId }?.let {
            return if (it.actionReq == true) {
                FriendshipStatus.PENDING_ACTIONREQ
            } else {
                FriendshipStatus.PENDING
            }
        }
        return FriendshipStatus.NONE
    }
}

enum class FriendshipStatus {
    FRIENDS,
    PENDING,
    PENDING_ACTIONREQ,
    NONE
}