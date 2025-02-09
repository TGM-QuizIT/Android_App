package com.example.quizit_android_app.usecases.localdata.friendship

import com.example.quizit_android_app.model.ContentDataStore
import com.example.quizit_android_app.model.retrofit.DataRepo
import javax.inject.Inject

class SyncLocalAcceptedFriendsUseCase @Inject constructor(
    val dataRepo: DataRepo,
    val contentDataStore: ContentDataStore
) {
    suspend operator fun invoke() {
        val friends = dataRepo.fetchAllFriends()
        contentDataStore.saveAcceptedFriends(friends?.acceptedFriendships)
    }
}