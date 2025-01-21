package com.example.quizit_android_app.model

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query


interface Requests {
    // ------------------- User Calls -------------------
    /*@Headers("Content-Type: application/json")
    @POST("user/login")
    suspend fun login(@Header("Authorization") authHeader: String, @Body params: RequestBody): LoginResponse*/

    @Headers("Content-Type: application/json")
    @POST("user/login")
    suspend fun login(@Body loginRequest: LoginRequestBody): UserResponse

    @PUT("user")
    suspend fun changeUserYear(@Body changeUserYearRequestBody: ChangeUserYearRequestBody): UserResponse



    // ------------------- Subject Calls -------------------
    @GET("subject")
    suspend fun getSubjects(): SubjectsResponse

    @GET("subject")
    suspend fun getSubjectOfUser(@Query("id") id: Int?): SubjectsResponse

    @POST("subject")
    suspend fun createSubject(@Body newSubject: NewSubjectRequest): SubjectsResponse


    // ------------------- Focus Calls -------------------
    @GET("focus")
    suspend fun getFocusOfUser(@Query("id") subjectId: Int?, @Query("year") userYear: Int?, @Query("active") active: Int?): FocusResponse

    // ------------------- Quiz Calls -------------------

    @GET("quiz/subject")
    suspend fun getQuizOfSubject(@Query("id") subjectId: Int?, @Query("year") userYear: Int?): QuizOfSubjectResponse

    @GET("quiz/focus")
    suspend fun getQuizOfFocus(@Query("id") focusId: Int?): QuizOfFocusResponse

    // ------------------- Result Calls -------------------
}