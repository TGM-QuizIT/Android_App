package com.example.quizit_android_app.usecases.localdata.focus

import android.util.Log
import com.example.quizit_android_app.model.ContentDataStore
import com.example.quizit_android_app.model.retrofit.DataRepo
import javax.inject.Inject

class SyncLocalFocusUseCase @Inject constructor(
    val dataRepo: DataRepo,
    val contentDataStore: ContentDataStore
) {
    suspend operator fun invoke() {
        val focus = dataRepo.fetchAllFocusOfUser()
        if (focus.status == "Success") {
            contentDataStore.saveFocus(focus.focus)
            Log.d("SyncLocalFocusUseCase", "Focus synced")
        }
    }
}