package com.example.quizit_android_app.usecases.localdata.friendship

import android.util.Log
import com.example.quizit_android_app.model.ContentDataStore
import com.example.quizit_android_app.model.retrofit.DataRepo
import javax.inject.Inject

class SyncLocalPendingFriendsUseCase @Inject constructor(
    val dataRepo: DataRepo,
    val contentDataStore: ContentDataStore
) {
    suspend operator fun invoke() {
        val pendingFriends = dataRepo.fetchAllFriends()
        Log.d("SyncLocalPendingFriendsUseCase", "invoke: $pendingFriends")
        if (pendingFriends?.status == "Success") {
            contentDataStore.savePendingFriends(pendingFriends.pendingFriendships)
        }
    }
}