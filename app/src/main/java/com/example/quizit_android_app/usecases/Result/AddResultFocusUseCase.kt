package com.example.quizit_android_app.usecases.Result

import com.example.quizit_android_app.model.DataRepo
import javax.inject.Inject

class AddResultFocusUseCase @Inject constructor(
    val dataRepo: DataRepo
) {
    suspend operator fun invoke(focusId: Int, score: Double) {
        dataRepo.postResultOfFocus(focusId, score)
    }
}