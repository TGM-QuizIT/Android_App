package com.example.quizit_android_app.usecases.friendship

import com.example.quizit_android_app.model.ContentDataStore
import com.example.quizit_android_app.model.retrofit.AcceptedFriendships
import com.example.quizit_android_app.model.retrofit.DataRepo
import javax.inject.Inject

class GetAcceptedFriendships @Inject constructor(
    val dataRepo: DataRepo,
    val contentDataStore: ContentDataStore
) {
    suspend operator fun invoke() : List<AcceptedFriendships> {
        val localData = contentDataStore.getAcceptedFriends()
        return localData.ifEmpty {
            val remoteData = dataRepo.fetchAllFriends()
            contentDataStore.saveAcceptedFriends(remoteData?.acceptedFriendships)
            remoteData?.acceptedFriendships ?: emptyList()
        }
    }
}