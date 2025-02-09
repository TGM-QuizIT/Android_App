package com.example.quizit_android_app.usecases.localdata.friendship

import com.example.quizit_android_app.model.ContentDataStore
import com.example.quizit_android_app.model.retrofit.DataRepo
import javax.inject.Inject

class SyncLocalPendingFriendsUseCase @Inject constructor(
    val dataRepo: DataRepo,
    val contentDataStore: ContentDataStore
) {
    suspend operator fun invoke() {
        val pendingFriends = dataRepo.fetchAllFriends()
        contentDataStore.savePendingFriends(pendingFriends?.pendingFriendships)
    }
}