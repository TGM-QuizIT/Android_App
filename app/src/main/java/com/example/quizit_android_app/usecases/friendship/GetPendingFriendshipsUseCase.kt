package com.example.quizit_android_app.usecases.friendship

import android.util.Log
import com.example.quizit_android_app.model.ContentDataStore
import com.example.quizit_android_app.model.retrofit.DataRepo
import com.example.quizit_android_app.model.retrofit.PendingFriendship
import javax.inject.Inject

class GetPendingFriendshipsUseCase @Inject constructor(
    val dataRepo: DataRepo,
    val contentDataStore: ContentDataStore
) {
    suspend operator fun invoke() : List<PendingFriendship> {
        val localData = contentDataStore.getPendingFriends()
        Log.d("GetPendingFriendshipsUseCase", "localData: $localData")
        return localData.ifEmpty {
            val remoteData = dataRepo.fetchAllFriends()
            Log.d("GetPendingFriendshipsUseCase", "remoteData: $remoteData")
            contentDataStore.savePendingFriends(remoteData?.pendingFriendships)
            remoteData?.pendingFriendships ?: emptyList()
        }
    }
}