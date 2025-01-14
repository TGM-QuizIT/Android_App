package com.example.quizit_android_app.usecases

import android.util.Log
import com.example.quizit_android_app.model.DataRepo
import com.example.quizit_android_app.model.User
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    val dataRepo: DataRepo
) {
    suspend operator fun invoke(username: String, password: String) : User? {
        val response = dataRepo.login(username, password)
        Log.d("LoginUseCase", "invoke: $response")
        return response
    }
}