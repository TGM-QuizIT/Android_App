package com.example.quizit_android_app.usecases.friendship

import com.example.quizit_android_app.model.ContentDataStore
import com.example.quizit_android_app.model.retrofit.AcceptedFriendship
import com.example.quizit_android_app.model.retrofit.DataRepo
import javax.inject.Inject

class GetAcceptedFriendshipsUseCase @Inject constructor(
    val dataRepo: DataRepo,
    val contentDataStore: ContentDataStore
) {
    suspend operator fun invoke() : List<AcceptedFriendship> {
        val localData = contentDataStore.getAcceptedFriends()
        return localData.ifEmpty {
            val remoteData = dataRepo.fetchAllFriends()
            contentDataStore.saveAcceptedFriends(remoteData?.acceptedFriendships)
            remoteData?.acceptedFriendships ?: emptyList()
        }
    }
}