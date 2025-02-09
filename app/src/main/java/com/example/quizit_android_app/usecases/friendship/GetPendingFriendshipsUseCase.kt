package com.example.quizit_android_app.usecases.friendship

import com.example.quizit_android_app.model.ContentDataStore
import com.example.quizit_android_app.model.retrofit.DataRepo
import com.example.quizit_android_app.model.retrofit.PendingFriendships
import javax.inject.Inject

class GetPendingFriendshipsUseCase @Inject constructor(
    val dataRepo: DataRepo,
    val contentDataStore: ContentDataStore
) {
    suspend operator fun invoke() : List<PendingFriendships> {
        val localData = contentDataStore.getPendingFriends()
        return localData.ifEmpty {
            val remoteData = dataRepo.fetchAllFriends()
            contentDataStore.savePendingFriends(remoteData?.pendingFriendships)
            remoteData?.pendingFriendships ?: emptyList()
        }
    }
}