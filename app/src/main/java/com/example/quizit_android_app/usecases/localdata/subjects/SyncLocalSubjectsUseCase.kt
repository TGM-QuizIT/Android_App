package com.example.quizit_android_app.usecases.localdata.subjects

import com.example.quizit_android_app.model.ContentDataStore
import com.example.quizit_android_app.model.retrofit.DataRepo
import javax.inject.Inject

class SyncLocalSubjectsUseCase @Inject constructor(
    val dataRepo: DataRepo,
    val contentDataStore: ContentDataStore
) {
    suspend operator fun invoke() {
        val subjects = dataRepo.fetchSubjectsOfUser()
        contentDataStore.saveSubjects(subjects)
    }
}