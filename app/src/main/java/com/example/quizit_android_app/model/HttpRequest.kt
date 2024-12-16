package com.example.quizit_android_app.model

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Context
import com.example.quizit_android_app.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SubjectsRepository (context: Context) {
    private val apiKey: String = context.getString(R.string.api_key)

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://projekte.tgm.ac.at/quizit/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service: SubjectRequests = retrofit.create(SubjectRequests::class.java)

    suspend fun getSubjects() = service.getSubjects(apiKey)
}

fun fetchSubjects(context: Context) {
    // Dispatcher IO --> network requests --> background thread
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val repository = SubjectsRepository(context)
            val response = repository.getSubjects()
            // Dispatcher Main --> update UI --> main thread
            withContext(Dispatchers.Main) {
                for (subject in response.subjects) {
                    Log.d("Retrofit Test", subject.subjectName + " " + subject.subjectId)
                }
            }
        } catch (e: Exception) {
            Log.e("Retrofit Test", "Failed to fetch subjects", e)
        }
    }
}


