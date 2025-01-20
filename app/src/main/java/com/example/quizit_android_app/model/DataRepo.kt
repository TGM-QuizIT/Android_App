package com.example.quizit_android_app.model

import android.content.Context
import android.util.Log
import com.example.quizit_android_app.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject


class DataRepo @Inject constructor(private val context: Context) {
    private val apiKey: String = context.getString(R.string.api_key)
    private val sessionManager = SessionManager(context)

    private val client = OkHttpClient.Builder()
        .addInterceptor(ApiKeyInterceptor(apiKey))
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://projekte.tgm.ac.at/quizit/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    private val service: Requests = retrofit.create(Requests::class.java)

    // ------------------- User Calls -------------------

    suspend fun login(username: String, password: String): User? {
        return withContext(Dispatchers.IO) {
            try {
                val response = service.login(LoginRequestBody(username, password))
                withContext(Dispatchers.Main) {
                    response.user?.let {
                        // Save the session with login
                        sessionManager.saveSession(it)
                        Log.d("Retrofit Test", "${it.userName} ${it.userId}")
                    }
                }
                response.user
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    Log.e("Retrofit Test", "Unauthorized: Invalid credentials")
                } else {
                    Log.e("Retrofit Test", "HTTP error: ${e.code()}")
                }
                null
            } catch (e: Exception) {
                Log.e("Retrofit Test", "Failed to login", e)
                null
            }
        }
    }

    suspend fun changeUserYear(userId: Int): Boolean  {
        return withContext(Dispatchers.IO) {
            try {
                val id = sessionManager.getUserId()
                //val response = service.changeUserYear(id)
                withContext(Dispatchers.Main) {
                    Log.d("Retrofit Test", "Year changed")
                }
                true
            } catch (e: Exception) {
                Log.e("Retrofit Test", "Failed to change year", e)
                false
            }
        }
    }


    // ------------------- Subject Calls -------------------
    suspend fun fetchSubjects(): List<Subject> {
        return withContext(Dispatchers.IO) {
            try {
                val repository = DataRepo(context)
                val response = repository.service.getSubjects()
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

    suspend fun fetchSubjectsOfUser(): List<Subject> {
        return withContext(Dispatchers.IO) {
            try {
                val repository = DataRepo(context)
                val id = sessionManager.getUserId()
                val response = repository.service.getSubjectOfUser(id)
                withContext(Dispatchers.Main) {
                    for (subject in response.subjects) {
                        Log.d("Retrofit Test", subject.subjectName + " " + subject.subjectId)
                    }
                }
                response.subjects
            } catch (e: Exception) {
                Log.e("Retrofit Test Fail", "Failed to fetch subjects", e)
                emptyList()
            }
        }
    }
}

