package com.example.quizit_android_app.usecases.localdata.subjects

import com.example.quizit_android_app.model.ContentDataStore
import com.example.quizit_android_app.model.retrofit.Subject
import javax.inject.Inject

class SaveToLocalSubjectsUseCase @Inject constructor(
    val contentDataStore: ContentDataStore
) {
    suspend operator fun invoke(subjects: List<Subject>) {
        contentDataStore.saveSubjects(subjects)
    }
}