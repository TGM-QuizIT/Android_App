package com.example.quizit_android_app.usecases.friendship

import android.util.Log
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
        Log.d("GetAcceptedFriendshipsUseCase", "using localData $localData")
        return localData.ifEmpty {
            val remoteData = dataRepo.fetchAllFriends()
            if (remoteData?.status == "Success") {
                contentDataStore.saveAcceptedFriends(remoteData.acceptedFriendships)
            }
            Log.d("GetAcceptedFriendshipsUseCase", "using remoteData")
            remoteData?.acceptedFriendships ?: emptyList()
        }
    }
}