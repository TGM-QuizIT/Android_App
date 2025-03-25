package com.example.quizit_android_app.usecases.quiz

import com.example.quizit_android_app.model.retrofit.DataRepo
import com.example.quizit_android_app.model.retrofit.Questions
import javax.inject.Inject

class GetQuizOfSubjectUseCase @Inject constructor(
    val dataRepo: DataRepo
) {
    suspend operator fun invoke(subjectId: Int) : List<Questions> {
        return dataRepo.fetchQuizOfSubject(subjectId)
    }
}