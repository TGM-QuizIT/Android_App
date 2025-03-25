package com.example.quizit_android_app.usecases.friendship

import com.example.quizit_android_app.model.ContentDataStore
import com.example.quizit_android_app.model.SessionManager
import com.example.quizit_android_app.model.retrofit.AcceptedFriendship
import com.example.quizit_android_app.model.retrofit.AllFriendshipResponse
import com.example.quizit_android_app.model.retrofit.DataRepo
import com.example.quizit_android_app.model.retrofit.PendingFriendship
import javax.inject.Inject

class GetAllFriendshipsUseCase @Inject constructor(
    val dataRepo: DataRepo,
    val getAcceptedFriendshipsUseCase: GetAcceptedFriendshipsUseCase,
    val getPendingFriendshipsUseCase: GetPendingFriendshipsUseCase,
    val sessionManager: SessionManager
) {
    suspend operator fun invoke() : AllFriendshipResponse? {
        val acceptedFriends: ArrayList<AcceptedFriendship> = ArrayList(getAcceptedFriendshipsUseCase())
        val pendingFriends: ArrayList<PendingFriendship> = ArrayList(getPendingFriendshipsUseCase())
        val userId = sessionManager.getUserId()
        if (acceptedFriends.isEmpty() || pendingFriends.isNotEmpty() || userId == null) {
            return null
        }
        return AllFriendshipResponse("Success", userId, acceptedFriends, pendingFriends)
    }
}