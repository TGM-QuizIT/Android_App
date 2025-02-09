package com.example.quizit_android_app.usecases.user

import android.util.Log
import com.example.quizit_android_app.model.ContentDataStore
import com.example.quizit_android_app.model.retrofit.DataRepo
import com.example.quizit_android_app.usecases.localdata.focus.SyncLocalFocusUseCase
import com.example.quizit_android_app.usecases.localdata.subjects.SyncLocalSubjectsUseCase
import javax.inject.Inject

class ChangeUserYearUseCase @Inject constructor(
    val dataRepo: DataRepo,
    val syncLocalSubjectsUseCase: SyncLocalSubjectsUseCase,
    val syncLocalFocusUseCase: SyncLocalFocusUseCase
) {
    suspend operator fun invoke(newUserYear: Int) {
        val response = dataRepo.changeUserYear(newUserYear)
        Log.d("ChangeUserYearUseCase", "invoke: $newUserYear")
        if (response.status != null) {
            syncLocalSubjectsUseCase()
            syncLocalFocusUseCase()
        }
    }
}