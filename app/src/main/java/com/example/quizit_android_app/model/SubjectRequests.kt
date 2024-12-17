package com.example.quizit_android_app.model

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query


interface SubjectRequests {
    @GET("subject")
    suspend fun getSubjects(@Header("Authorization") authHeader: String): SubjectsResponse

    @GET("subject")
    suspend fun getSubjectOfUser(
        @Header("Authorization") authHeader: String, @Query("id") id: Int?
    ): SubjectsResponse

    @POST("subject")
    suspend fun createSubject(
        @Header("Authorization") authHeader: String,
        @Body newSubject: NewSubjectRequest
    ): SubjectsResponse
}