package com.example.quizit_android_app.usecases.user

import com.example.quizit_android_app.model.SessionManager
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    val sessionManager: SessionManager
) {
    suspend operator fun invoke() {
        sessionManager.clearSession()
    }
}