package com.example.quizit_android_app.model.retrofit

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query


interface Requests {

    @Headers("Content-Type: application/json")
    @POST("user/login")
    suspend fun login(@Body loginRequest: LoginRequestBody): UserResponse

    @PUT("user")
    suspend fun changeUserYear(@Body changeUserYearRequestBody: ChangeUserYearRequestBody): UserResponse

    @GET("user")
    suspend fun getAllUsers(@Query("year") year: Int?): UserResponse

    @GET("user/stats")
    suspend fun getUserStats(@Query("id") userId: Int?): UserStatsResponse

    @DELETE("user")
    suspend fun deleteUser(@Query("id") userId: Int?): StatusResponse

    @GET("user/blocked")
    suspend fun isUserBlocked(@Query("id") userId: Int?): UserBlockedResponse


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
        @Query("id") subjectId: Int?, @Query("year") userYear: Int?, @Query("active") active: Int?
    ): FocusResponse

    // ------------------- Quiz Calls -------------------

    @GET("quiz/subject")
    suspend fun getQuizOfSubject(
        @Query("id") subjectId: Int?, @Query("year") userYear: Int?
    ): QuizOfSubjectResponse

    @GET("quiz/focus")
    suspend fun getQuizOfFocus(@Query("id") focusId: Int?): QuizOfFocusResponse

    // ------------------- Result Calls -------------------

    @POST("result")
    suspend fun postResultOfFocus(@Body result: PostResultRequestBody): GetSingleResultsResponse

    @POST("result/subject")
    suspend fun postResultOfSubject(@Body result: PostResultRequestBodyForSubject): GetSingleResultsResponse

    @GET("result")
    suspend fun getResultsOfUser(
        @Query("userId") userId: Int?, @Query("amount") amount: Int? = null
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
    suspend fun acceptFriend(@Body acceptFriendRequestBody: AcceptFriendRequestBody): FriendshipResponse

    @DELETE("friends")
    suspend fun deleteFriend(@Query("id") friendshipId: Int?): StatusResponse

    // ------------------- Challenge Calls -------------------

    @POST("challenge")
    suspend fun addChallengeForFocus(@Body adChallengeForFocusRequestBody: AdChallengeForFocusRequestBody): AssignResultToChallengeResponse

    @POST("challenge/subject")
    suspend fun addChallengeForSubject(@Body adChallengeForSubjectRequestBody: AdChallengeForSubjectRequestBody): AssignResultToChallengeResponse

    @DELETE("challenge")
    suspend fun deleteChallenge(@Query("id") challengeId: Int?): StatusResponse

    @PUT("challenge")
    suspend fun assignResultToChallenge(@Body assignResultToChallengeRequestBody: AssignResultToChallengeRequestBody): AssignResultToChallengeResponse

    @GET("challenge/friendship")
    suspend fun getChallengesOfFriendship(
        @Query("friendshipId") friendshipId: Int?,
        @Query("userId") userId: Int?
    ): ChallengeResponse

    @GET("challenge")
    suspend fun getChallengesForSubject(
        @Query("subjectId") subjectId: Int?,
        @Query("userId") userId: Int?
    ): ChallengeResponse

    @GET("challenge/done")
    suspend fun getDoneChallenges(@Query("userId") userId: Int?): DoneChallengesResponse

    @GET("challenge/open")
    suspend fun getOpenChallenges(@Query("userId") userId: Int?): OpenChallengesResponse

}