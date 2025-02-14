package com.example.quizit_android_app.usecases.localdata.friendship

import android.util.Log
import com.example.quizit_android_app.model.ContentDataStore
import com.example.quizit_android_app.model.retrofit.DataRepo
import javax.inject.Inject

class SyncLocalAcceptedFriendsUseCase @Inject constructor(
    val dataRepo: DataRepo,
    val contentDataStore: ContentDataStore
) {
    suspend operator fun invoke() {
        val friends = dataRepo.fetchAllFriends()
        Log.d("SyncLocalAcceptedFriendsUseCase", "invoke: ${friends?.acceptedFriendships}")
        if (friends?.status == "Success") {
            contentDataStore.saveAcceptedFriends(friends.acceptedFriendships)
            Log.d("SyncLocalAcceptedFriendsUseCase", "acceptedFriendships synced")
        }
    }
}