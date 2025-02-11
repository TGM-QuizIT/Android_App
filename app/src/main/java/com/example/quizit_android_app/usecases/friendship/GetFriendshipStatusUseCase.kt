package com.example.quizit_android_app.usecases.friendship

import com.example.quizit_android_app.model.retrofit.DataRepo
import javax.inject.Inject

class GetFriendshipStatusUseCase @Inject constructor(
    val dataRepo: DataRepo
) {
    suspend operator fun invoke(friendshipId: Int): FriendshipStatus {
        val friendships = dataRepo.fetchAllFriends()
        val acceptedFriends = friendships?.acceptedFriendships
        val pendingFriends = friendships?.pendingFriendships

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
        throw IllegalArgumentException("Friendship ID not found")
    }
}

enum class FriendshipStatus {
    FRIENDS,
    PENDING,
    PENDING_ACTIONREQ
}