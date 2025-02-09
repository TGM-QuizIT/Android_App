package com.example.quizit_android_app.usecases.focus

import android.util.Log
import com.example.quizit_android_app.model.retrofit.DataRepo
import com.example.quizit_android_app.model.retrofit.Focus
import javax.inject.Inject

class GetAllFocusUseCase @Inject constructor(
    val dataRepo: DataRepo
) {
    suspend operator fun invoke(subjectId: Int, active: Int = 1) : List<Focus> {
        val response = dataRepo.fetchFocusOfUser(subjectId, active)
        Log.d("GetAllFocusUseCase", "invoke: $response")
        return response
    }
}