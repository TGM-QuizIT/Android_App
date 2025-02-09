package com.example.quizit_android_app.usecases.localdata.focus

import com.example.quizit_android_app.model.ContentDataStore
import com.example.quizit_android_app.model.retrofit.Focus
import javax.inject.Inject

class SaveToLocalFocusUseCase @Inject constructor(
    val contentDataStore: ContentDataStore
) {
    suspend operator fun invoke(newFocuses: List<Focus>) {
        val existingFocuses = contentDataStore.getFocus().toMutableList()
        newFocuses.forEach { newFocus ->
            if (existingFocuses.none { it.focusId == newFocus.focusId }) {
                existingFocuses.add(newFocus)
            }
        }
        contentDataStore.saveFocus(existingFocuses)
    }
}