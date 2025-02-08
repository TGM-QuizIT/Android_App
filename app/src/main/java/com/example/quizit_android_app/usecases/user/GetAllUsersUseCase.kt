package com.example.quizit_android_app.usecases.user

import android.util.Log
import com.example.quizit_android_app.model.retrofit.DataRepo
import com.example.quizit_android_app.model.retrofit.User
import javax.inject.Inject

class GetAllUsersUseCase @Inject constructor(
    val dataRepo: DataRepo
) {
    suspend operator fun invoke(year: Int? = null): List<User?> {

        val users = dataRepo.fetchAllUsers(year)
        Log.d("GetAllUsersUseCase", "invoke: $users")
        return users
    }
}