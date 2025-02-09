package com.example.quizit_android_app.usecases.subjects

import android.util.Log
import com.example.quizit_android_app.model.ContentDataStore
import com.example.quizit_android_app.model.retrofit.Subject
import com.example.quizit_android_app.model.retrofit.DataRepo
import javax.inject.Inject

class GetAllSubjectsUseCase @Inject constructor(
    val dataRepo: DataRepo,
    val contentDataStore: ContentDataStore
) {
    suspend operator fun invoke(): List<Subject> {
        val localData = contentDataStore.getSubjects()
        return localData.ifEmpty {
            val remoteData = dataRepo.fetchSubjectsOfUser()
            contentDataStore.saveSubjects(remoteData)
            remoteData
        }
    }
}