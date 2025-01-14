package com.example.quizit_android_app.usecases

import com.example.quizit_android_app.model.SessionManager
import javax.inject.Inject

class IsUserLoggedInUseCase @Inject constructor(
    val sessionManager: SessionManager
) {
    suspend operator fun invoke(): Boolean {
        return sessionManager.isUserLoggedIn()
    }
}