package com.example.quizit_android_app.usecases.user

import com.example.quizit_android_app.model.SessionManager
import com.example.quizit_android_app.model.retrofit.User
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    val sessionManager: SessionManager
) {
    suspend operator fun invoke(): User? {
        return sessionManager.getUser()
    }
}