package com.example.quizit_android_app.usecases.user

import com.example.quizit_android_app.model.ContentDataStore
import com.example.quizit_android_app.model.SessionManager
import com.example.quizit_android_app.model.retrofit.DataRepo
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    val dataRepo: DataRepo,
    val dataStore: ContentDataStore,
    val sessionManager: SessionManager
) {
    suspend operator fun invoke() {
        val response = dataRepo.deleteUser()
        if (response.status == "Success") {
            dataStore.clearData()
            sessionManager.clearSession()
        }

    }
}
