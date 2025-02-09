package com.example.quizit_android_app.usecases.localdata.focus

import com.example.quizit_android_app.model.ContentDataStore
import com.example.quizit_android_app.model.retrofit.DataRepo
import javax.inject.Inject

class SyncLocalFocusUseCase @Inject constructor(
    val dataRepo: DataRepo,
    val contentDataStore: ContentDataStore
) {
    suspend operator fun invoke() {
        val focus = dataRepo.fetchAllFocusOfUser()
        contentDataStore.saveFocus(focus)
    }
}