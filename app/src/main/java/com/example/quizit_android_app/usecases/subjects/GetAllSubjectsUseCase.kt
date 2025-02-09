package com.example.quizit_android_app.usecases.subjects

import android.util.Log
import com.example.quizit_android_app.model.retrofit.Subject
import com.example.quizit_android_app.model.retrofit.DataRepo
import javax.inject.Inject

class GetAllSubjectsUseCase @Inject constructor(
    val dataRepo: DataRepo
) {
    suspend operator fun invoke(): List<Subject> {
        val myreturn = dataRepo.fetchSubjectsOfUser()
        Log.d("GetAllSubjectsUseCase", "invoke: $myreturn")
        return myreturn
    }
}