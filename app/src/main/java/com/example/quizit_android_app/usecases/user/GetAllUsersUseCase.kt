package com.example.quizit_android_app.usecases.user

import com.example.quizit_android_app.model.DataRepo
import javax.inject.Inject

class GetAllUsersUseCase @Inject constructor(
    val dataRepo: DataRepo
) {
    suspend operator fun invoke(year: Int? = null) {
        dataRepo.fetchAllUsers(year)
    }
}