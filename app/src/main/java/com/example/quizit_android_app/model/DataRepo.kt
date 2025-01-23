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

    suspend fun changeUserYear(newUserYear: Int): Boolean  {
        return withContext(Dispatchers.IO) {
            try {
                val id = sessionManager.getUserId()
                val response = service.changeUserYear(ChangeUserYearRequestBody(id, newUserYear))
                withContext(Dispatchers.Main) {
                    response.user?.let {
                        sessionManager.saveSession(it)
                        Log.d("Retrofit Test", "${it.userName} ${it.userId}")
                    }
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
                val response = service.getSubjects()
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
                val id = sessionManager.getUserId()
                Log.d("Retrofit Test", "User ID: $id")
                val response = service.getSubjectOfUser(id)
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

    // ------------------- Focus Calls -------------------

    suspend fun fetchFocusOfUser(subjectId: Int, active: Int): List<Focus> {
        return withContext(Dispatchers.IO) {
            try {
                val userYear = sessionManager.getUserYear()
                Log.d("Retrofit Test FetchFocus", "User Year: $userYear")
                val response = service.getFocusOfUser(subjectId, userYear = userYear, active)
                Log.d("Retrofit Test FetchFocus", "Subject ID: $subjectId Active: $active")
                withContext(Dispatchers.Main) {
                    for (focus in response.focus) {
                        Log.d("Retrofit Test FetchFocus", focus.focusName + " " + focus.focusId)
                    }
                }
                response.focus
            } catch (e: Exception) {
                Log.e("Retrofit Test FetchFocus", "Failed to fetch focus", e)
                emptyList()
            }
        }
    }

    // ------------------- Quiz Calls -------------------

    suspend fun fetchQuizOfSubject(subjectId: Int): List<Questions> {
        return withContext(Dispatchers.IO) {
            try {
                val userYear = sessionManager.getUserYear()
                val response = service.getQuizOfSubject(subjectId, userYear)
                withContext(Dispatchers.Main) {
                    for (question in response.questions) {
                        Log.d("Retrofit Test", question.questionText + " " + question.questionId)
                    }
                }
                response.questions
            } catch (e: Exception) {
                Log.e("Retrofit Test", "Failed to fetch quiz", e)
                emptyList()
            }
        }
    }

    suspend fun fetchQuizOfFocus(focusId: Int): List<Questions> {
        return withContext(Dispatchers.IO) {
            try {
                val response = service.getQuizOfFocus(focusId)
                withContext(Dispatchers.Main) {
                    for (question in response.questions) {
                        Log.d("Retrofit Test", question.questionText + " " + question.questionId)
                    }
                }
                response.questions
            } catch (e: Exception) {
                Log.e("Retrofit Test", "Failed to fetch quiz", e)
                emptyList()
            }
        }
    }




}

