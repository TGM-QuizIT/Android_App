package com.example.quizit_android_app.usecases.focus

import com.example.quizit_android_app.model.ContentDataStore
import com.example.quizit_android_app.model.retrofit.DataRepo
import com.example.quizit_android_app.model.retrofit.Focus
import javax.inject.Inject

class GetAllFocusOfUserUseCase @Inject constructor(
    val dataRepo: DataRepo,
    val dataStore: ContentDataStore
) {
    suspend operator fun invoke(): List<Focus> {
        val localData = dataStore.getFocus()
        return localData.ifEmpty {
            val remoteData = dataRepo.fetchAllFocusOfUser()
            dataStore.saveFocus(remoteData.focus)
            remoteData.focus
        }
    }
}