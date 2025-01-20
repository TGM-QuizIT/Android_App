package com.example.quizit_android_app.usecases.Subjects

import android.util.Log
import com.example.quizit_android_app.model.Subject
import com.example.quizit_android_app.model.DataRepo
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