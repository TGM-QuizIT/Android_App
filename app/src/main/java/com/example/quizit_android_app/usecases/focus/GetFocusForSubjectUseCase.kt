package com.example.quizit_android_app.usecases.focus

import com.example.quizit_android_app.model.ContentDataStore
import com.example.quizit_android_app.model.retrofit.DataRepo
import com.example.quizit_android_app.model.retrofit.Focus
import javax.inject.Inject

class GetFocusForSubjectUseCase @Inject constructor(
    val dataRepo: DataRepo,
    val contentDataStore: ContentDataStore
) {
    suspend operator fun invoke(subjectId: Int, active: Int = 1) : List<Focus> {
        val localData = contentDataStore.getFocus().filter { it.subjectId == subjectId }
        return localData.ifEmpty {
            val remoteData = dataRepo.fetchFocusForSubject(subjectId, active)
            contentDataStore.saveFocus(remoteData.focus)
            remoteData.focus
        }
    }
}