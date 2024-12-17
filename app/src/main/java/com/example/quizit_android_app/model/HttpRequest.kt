package com.example.quizit_android_app.model

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Context
import com.example.quizit_android_app.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DataRepo @Inject constructor(private val context: Context) {
    private val apiKey: String = context.getString(R.string.api_key)

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://projekte.tgm.ac.at/quizit/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service: SubjectRequests = retrofit.create(SubjectRequests::class.java)

    suspend fun fetchSubjects(): List<Subject> {
        return withContext(Dispatchers.IO) {
            try {
                val repository = DataRepo(context)
                val response = repository.service.getSubjects(apiKey)
                withContext(Dispatchers.Main) {
                    for (subject in response.subjects) {
                        Log.d("Retrofit Test", subject.subjectName + " " + subject.subjectId)
                    }
                }
                response.subjects
            } catch (e: Exception) {
                Log.e("Retrofit Test", "Failed to fetch subjects", e)
                emptyList()
            }
        }
    }
    suspend fun fetchSubjectsOfUser(id: Int): List<Subject> {
        return withContext(Dispatchers.IO) {
            try {
                val repository = DataRepo(context)
                val response = repository.service.getSubjectOfUser(apiKey, id)
                withContext(Dispatchers.Main) {
                    for (subject in response.subjects) {
                        Log.d("Retrofit Test", subject.subjectName + " " + subject.subjectId)
                    }
                }
                response.subjects
            } catch (e: Exception) {
                Log.e("Retrofit Test", "Failed to fetch subjects", e)
                emptyList()
            }
        }
    }
}

