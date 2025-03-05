package com.example.quizit_android_app.usecases.subjects

import com.example.quizit_android_app.model.retrofit.DataRepo
import com.example.quizit_android_app.model.retrofit.Subject
import com.example.quizit_android_app.usecases.focus.GetAllFocusOfUserUseCase
import javax.inject.Inject

class GetSubjectForFocusID @Inject constructor(
    val dataRepo: DataRepo,
    val getAllFocusOfUserUseCase: GetAllFocusOfUserUseCase,
    val getAllSubjectsUseCase: GetAllSubjectsUseCase
) {
    suspend operator fun invoke(focusId: Int): Subject? {
        val focuses = getAllFocusOfUserUseCase()
        val focus = focuses.find { it.focusId == focusId }
        val subjects = getAllSubjectsUseCase()

        return subjects.find { it.subjectId == focus?.subjectId }
    }
}