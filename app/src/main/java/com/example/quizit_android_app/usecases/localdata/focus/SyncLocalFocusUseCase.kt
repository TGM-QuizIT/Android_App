package com.example.quizit_android_app.usecases.localdata.focus

import com.example.quizit_android_app.model.ContentDataStore
import com.example.quizit_android_app.model.retrofit.DataRepo
import javax.inject.Inject

class SyncLocalFocusUseCase @Inject constructor(
    val dataRepo: DataRepo,
    val contentDataStore: ContentDataStore
) {
    suspend operator fun invoke() {
        // TODO: alle schwerpunkte eines user holen, unabhängig vom fach
        //val focus = dataRepo.fetchFocusOfUser()
        //contentDataStore.saveFocus(focus)
    }
}