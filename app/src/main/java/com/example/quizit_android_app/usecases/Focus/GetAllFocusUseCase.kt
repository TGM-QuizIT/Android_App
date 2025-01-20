package com.example.quizit_android_app.usecases.Focus

import com.example.quizit_android_app.model.DataRepo
import com.example.quizit_android_app.model.Focus
import javax.inject.Inject

class GetAllFocusUseCase @Inject constructor(
    val dataRepo: DataRepo
) {
    suspend operator fun invoke(subjectId: Int, active: Int = 1) : List<Focus> {
        val response = dataRepo.fetchFocusOfUser(subjectId, active)
        return response
    }
}