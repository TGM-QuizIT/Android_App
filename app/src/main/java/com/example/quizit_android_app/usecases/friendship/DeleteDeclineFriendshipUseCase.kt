package com.example.quizit_android_app.usecases.friendship

import com.example.quizit_android_app.model.retrofit.DataRepo
import com.example.quizit_android_app.model.retrofit.StatusResponse
import com.example.quizit_android_app.usecases.localdata.challenge.SyncLocalDoneChallengesUseCase
import com.example.quizit_android_app.usecases.localdata.challenge.SyncLocalOpenChallengesUseCase
import com.example.quizit_android_app.usecases.localdata.friendship.SyncLocalAcceptedFriendsUseCase
import com.example.quizit_android_app.usecases.localdata.friendship.SyncLocalPendingFriendsUseCase
import javax.inject.Inject

class DeleteDeclineFriendshipUseCase @Inject constructor(
    val dataRepo: DataRepo,
    val syncLocalPendingFriendsUseCase: SyncLocalPendingFriendsUseCase,
    val syncLocalAcceptedFriendsUseCase: SyncLocalAcceptedFriendsUseCase,
    val syncLocalDoneChallengesUseCase: SyncLocalDoneChallengesUseCase,
    val syncLocalOpenChallengesUseCase: SyncLocalOpenChallengesUseCase
) {
    suspend operator fun invoke(friendshipId: Int): StatusResponse {
        val response = dataRepo.deleteFriendship(friendshipId)
        if (response.status == "Success") {
            syncLocalPendingFriendsUseCase()
            syncLocalAcceptedFriendsUseCase()
            syncLocalDoneChallengesUseCase()
            syncLocalOpenChallengesUseCase()
        }
        return response
    }
}