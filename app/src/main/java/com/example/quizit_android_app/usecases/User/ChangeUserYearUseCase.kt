package com.example.quizit_android_app.usecases.User

import android.util.Log
import com.example.quizit_android_app.model.DataRepo
import com.example.quizit_android_app.model.SessionManager
import javax.inject.Inject

class ChangeUserYearUseCase @Inject constructor(
    val dataRepo: DataRepo
) {
    suspend operator fun invoke(newUserYear: Int) {
        dataRepo.changeUserYear(newUserYear)
        Log.d("ChangeUserYearUseCase", "invoke: $newUserYear")
    }
}