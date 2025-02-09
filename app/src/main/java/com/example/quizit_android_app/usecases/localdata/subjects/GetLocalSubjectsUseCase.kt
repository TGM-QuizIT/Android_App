package com.example.quizit_android_app.usecases.localdata.subjects

import com.example.quizit_android_app.model.ContentDataStore
import javax.inject.Inject

class GetLocalSubjectsUseCase @Inject constructor(
    val contentDataStore: ContentDataStore
) {
    suspend operator fun invoke() = contentDataStore.getSubjects()
}