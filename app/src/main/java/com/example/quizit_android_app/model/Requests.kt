package com.example.quizit_android_app.model

import retrofit2.http.Body
import retrofit2.http.DELETE
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

    @GET("user")
    suspend fun getAllUsers(@Query("year") year: Int?): UserResponse

    @GET("user/stats")
    suspend fun getUserStats(@Query("id") userId: Int?): UserStatsResponse


    // ------------------- Subject Calls -------------------
    @GET("subject")
    suspend fun getSubjects(): SubjectsResponse

    @GET("subject")
    suspend fun getSubjectOfUser(@Query("id") id: Int?): SubjectsResponse

    @POST("subject")
    suspend fun createSubject(@Body newSubject: NewSubjectRequest): SubjectsResponse


    // ------------------- Focus Calls -------------------
    @GET("focus")
    suspend fun getFocusOfUser(
        @Query("id") subjectId: Int?,
        @Query("year") userYear: Int?,
        @Query("active") active: Int?
    ): FocusResponse

    // ------------------- Quiz Calls -------------------

    @GET("quiz/subject")
    suspend fun getQuizOfSubject(
        @Query("id") subjectId: Int?,
        @Query("year") userYear: Int?
    ): QuizOfSubjectResponse

    @GET("quiz/focus")
    suspend fun getQuizOfFocus(@Query("id") focusId: Int?): QuizOfFocusResponse

    // ------------------- Result Calls -------------------

    @POST("result")
    suspend fun postResultOfFocus(@Body result: PostResultRequestBody): GetResultsResponse

    @POST("result/subject")
    suspend fun postResultOfSubject(@Body result: PostResultRequestBody): GetResultsResponse

    @GET("result")
    suspend fun getResultsOfUser(
        @Query("userId") userId: Int?,
        @Query("amount") amount: Int? = null
    ): GetResultsResponse

    @GET("result")
    suspend fun getResultsOfSubject(
        @Query("userId") userId: Int?,
        @Query("subjectId") subjectId: Int?,
        @Query("amount") amount: Int? = null
    ): GetResultsResponse

    @GET("result")
    suspend fun getResultsOfFocus(
        @Query("userId") userId: Int?,
        @Query("focusId") focusId: Int?,
        @Query("amount") amount: Int? = null
    ): GetResultsResponse

    // ------------------- Friend Calls -------------------

    @GET("friends")
    suspend fun getFriends(@Query("id") userId: Int?): AllFriendshipResponse

    @POST("friends")
    suspend fun addFriend(@Body friendRequestBody: FriendRequestBody): FriendshipResponse

    @PUT("friends/accept")
    suspend fun acceptFriend(@Body acceptFriendRequestBody: AcceptFriendRequestBody ): FriendshipResponse

    @DELETE("friends")
    suspend fun deleteFriend(@Query("id") friendshipId: Int?)

    // ------------------- Challenge Calls -------------------

    @POST("challenge")
    suspend fun addChallengeForFocus(@Body adChallengeForFocusRequestBody: AdChallengeForFocusRequestBody)

    @POST("challenge/subject")
    suspend fun addChallengeForSubject(@Body adChallengeForSubjectRequestBody: AdChallengeForSubjectRequestBody)

    @DELETE("challenge")
    suspend fun deleteChallenge(@Query("id") challengeId: Int?)

    @PUT("challenge")
    suspend fun assignResultToChallenge(@Body assignResultToChallengeRequestBody: AssignResultToChallengeRequestBody): AssignResultToChallengeResponse

    @GET("challenge/friendship")
    suspend fun getChallengesOfFriendship(@Query("friendshipId") friendshipId: Int?, @Query("userId") userId: Int?): ChallengeResponse

    @GET("challenge")
    suspend fun getChallengesForSubject(@Query("subjectId") subjectId: Int?, @Query("userId") userId: Int?): ChallengeResponse

    @GET("challenge/done")
    suspend fun getDoneChallenges(@Query("userId") userId: Int?): DoneChallengesResponse

}