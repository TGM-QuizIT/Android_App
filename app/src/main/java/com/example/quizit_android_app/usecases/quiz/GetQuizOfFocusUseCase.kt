package com.example.quizit_android_app.usecases.quiz

import com.example.quizit_android_app.model.DataRepo
import com.example.quizit_android_app.model.Questions
import javax.inject.Inject

class GetQuizOfFocusUseCase @Inject constructor(
    val dataRepo: DataRepo
) {
    suspend operator fun invoke(focusId: Int) : List<Questions> {
        return dataRepo.fetchQuizOfFocus(focusId)
    }
}