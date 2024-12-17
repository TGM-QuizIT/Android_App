package com.example.quizit_android_app.usecases

import android.util.Log
import com.example.quizit_android_app.model.Subject
import com.example.quizit_android_app.model.DataRepo
import javax.inject.Inject

class GetAllSubjectsUseCase @Inject constructor(
    val dataRepo: DataRepo
) {
    suspend operator fun invoke(id: Int): List<Subject> {
        val myreturn = dataRepo.fetchSubjectsOfUser(id)
        Log.d("GetAllSubjectsUseCase", "invoke: $myreturn")
        return myreturn
    }
}