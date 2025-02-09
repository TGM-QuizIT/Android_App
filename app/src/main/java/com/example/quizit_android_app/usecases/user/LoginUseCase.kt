package com.example.quizit_android_app.usecases.user

import android.util.Log
import com.example.quizit_android_app.model.retrofit.DataRepo
import com.example.quizit_android_app.model.retrofit.User
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