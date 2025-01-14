package com.example.quizit_android_app.model

import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query


interface Requests {
    // ------------------- User Calls -------------------
    /*@Headers("Content-Type: application/json")
    @POST("user/login")
    suspend fun login(@Header("Authorization") authHeader: String, @Body params: RequestBody): LoginResponse*/

    @Headers("Content-Type: application/json")
    @POST("user/login")
    suspend fun login(@Body loginRequest: LoginRequestBody): LoginResponse



    // ------------------- Subject Calls -------------------
    @GET("subject")
    suspend fun getSubjects(): SubjectsResponse

    @GET("subject")
    suspend fun getSubjectOfUser(@Query("id") id: Int?): SubjectsResponse

    @POST("subject")
    suspend fun createSubject(@Body newSubject: NewSubjectRequest): SubjectsResponse
}