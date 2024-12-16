package com.example.quizit_android_app.model

import androidx.compose.ui.res.stringResource
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path


interface SubjectRequests {
    @GET("subject")
    suspend fun getSubjects(@Header("Authorization") authHeader: String): SubjectsResponse

    @GET("subject?id={id}")
    suspend fun getSubjectOfUser(@Header("Authorization") authHeader: String, @Path("id") id: Int): SubjectsResponse

    @POST("subject")
    suspend fun createSubject(@Header("Authorization") authHeader: String, @Body newSubject: NewSubjectRequest): SubjectsResponse
}