package com.example.quizit_android_app.usecases.localdata.focus

import com.example.quizit_android_app.model.ContentDataStore
import com.example.quizit_android_app.model.retrofit.Focus
import javax.inject.Inject

class GetLocalFocusUseCase @Inject constructor(
    val contentDataStore: ContentDataStore
) {
    suspend operator fun invoke(subjectId: Int): List<Focus> {
        return contentDataStore.getFocus().filter { it.subjectId == subjectId }
    }
}