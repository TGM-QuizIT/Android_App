package com.example.quizit_android_app.model.retrofit

import android.content.Context
import android.util.Log
import com.example.quizit_android_app.R
import com.example.quizit_android_app.model.SessionManager
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
                        Log.d("login", "${it.userName} ${it.userId}")
                    }
                }
                response.user
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    Log.e("login", "Unauthorized: Invalid credentials")
                } else {
                    Log.e("login", "HTTP error: ${e.code()}")
                }
                null
            } catch (e: Exception) {
                Log.e("login", "Failed to login", e)
                null
            }
        }
    }

    suspend fun changeUserYear(newUserYear: Int): UserResponse  {
        return withContext(Dispatchers.IO) {
            try {
                val id = sessionManager.getUserId()
                val response = service.changeUserYear(ChangeUserYearRequestBody(id, newUserYear))
                withContext(Dispatchers.Main) {
                    response.user?.let {
                        sessionManager.saveSession(it)
                        Log.d("changeUserYear", "${it.userName} ${it.userId} ${it.userYear}")
                    }
                    Log.d("changeUserYear", "Year changed")
                }
                response
            } catch (e: Exception) {
                Log.e("changeUserYear", "Failed to change year", e)
                UserResponse()
            }
        }
    }

    suspend fun fetchAllUsers(year: Int? = null): List<User?> {
        return withContext(Dispatchers.IO) {
            try {
                val response = service.getAllUsers(year)
                withContext(Dispatchers.Main) {
                    for (user in response.users) {
                        Log.d("fetchAllUsers", "")
                    }
                }
                response.users
            } catch (e: Exception) {
                Log.e("fetchAllUsers", "Failed to fetch users", e)
                emptyList()
            }
        }
    }

    suspend fun fetchUserStats(): UserStatsResponse {
        return withContext(Dispatchers.IO) {
            try {
                val id = sessionManager.getUserId()
                val response = service.getUserStats(id)
                withContext(Dispatchers.Main) {
                    Log.d("fetchUserStats", response.stats.toString())
                }
                response
            } catch (e: Exception) {
                Log.e("fetchUserStats", "Failed to fetch user stats", e)
                UserStatsResponse()
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
                        Log.d("fetchSubjects", subject.subjectName + " " + subject.subjectId)
                    }
                }
                response.subjects
            } catch (e: Exception) {
                Log.e("fetchSubjects", "Failed to fetch subjects", e)
                emptyList()
            }
        }
    }

    suspend fun fetchSubjectsOfUser(): List<Subject> {
        return withContext(Dispatchers.IO) {
            try {
                val id = sessionManager.getUserId()
                Log.d("fetchSubjectsOfUser", "User ID: $id")
                val response = service.getSubjectOfUser(id)
                withContext(Dispatchers.Main) {
                    for (subject in response.subjects) {
                        Log.d("fetchSubjectsOfUser", subject.subjectName + " " + subject.subjectId)
                    }
                }
                response.subjects
            } catch (e: Exception) {
                Log.e("fetchSubjectsOfUser", "Failed to fetch subjects", e)
                emptyList()
            }
        }
    }

    // ------------------- Focus Calls -------------------

    suspend fun fetchAllFocusOfUser(): List<Focus> {
        return withContext(Dispatchers.IO) {
            try {
                val subjects = fetchSubjectsOfUser()
                val allFocuses = mutableListOf<Focus>()

                for (subject in subjects) {
                    subject.subjectId.let { subjectId ->
                        val focuses = fetchFocusForSubject(subjectId, active = 1)
                        allFocuses.addAll(focuses)
                    }
                }

                withContext(Dispatchers.Main) {
                    for (focus in allFocuses) {
                        Log.d("fetchAllFocusOfUser", focus.focusName + " " + focus.focusId)
                    }
                }
                allFocuses
            } catch (e: Exception) {
                Log.e("fetchAllFocusOfUser", "Failed to fetch focus", e)
                emptyList()
            }
        }
    }

    suspend fun fetchFocusForSubject(subjectId: Int, active: Int): List<Focus> {
        return withContext(Dispatchers.IO) {
            try {
                val userYear = sessionManager.getUserYear()
                Log.d("fetchFocusForSubject", "User Year: $userYear")
                val response = service.getFocusOfUser(subjectId, userYear = userYear, active)
                Log.d("fetchFocusForSubject", "Subject ID: $subjectId Active: $active")
                withContext(Dispatchers.Main) {
                    for (focus in response.focus) {
                        Log.d("fetchFocusForSubject", focus.focusName + " " + focus.focusId)
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
                        Log.d("fetchQuizOfSubject", question.questionText + " " + question.questionId)
                    }
                }
                response.questions
            } catch (e: Exception) {
                Log.e("fetchQuizOfSubject", "Failed to fetch quiz", e)
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
                        Log.d("fetchQuizOfFocus", question.questionText + " " + question.questionId)
                    }
                }
                response.questions
            } catch (e: Exception) {
                Log.e("fetchQuizOfFocus", "Failed to fetch quiz", e)
                emptyList()
            }
        }
    }

    // ------------------- Result Calls -------------------

    suspend fun postResultOfFocus(focusId: Int, score: Double): GetResultsResponse {
        return withContext(Dispatchers.IO) {
            try {
                val id = sessionManager.getUserId()
                val response = service.postResultOfFocus(PostResultRequestBody(score, id, focusId))
                withContext(Dispatchers.Main) {
                    Log.d("postResultOfFocus", "Focus Result posted")
                }
                response
            } catch (e: Exception) {
                Log.e("postResultOfFocus", "Failed to post focus result", e)
                GetResultsResponse()
            }
        }
    }

    suspend fun postResultOfSubject(subjectID: Int, score: Double): GetResultsResponse {
        return withContext(Dispatchers.IO) {
            try {
                val id = sessionManager.getUserId()
                val response = service.postResultOfSubject(PostResultRequestBody(score, id, subjectID))
                withContext(Dispatchers.Main) {
                    Log.d("postResultOfSubject", "Subject Result posted")
                }
                response
            } catch (e: Exception) {
                Log.e("postResultOfSubject", "Failed to post Subject result", e)
                GetResultsResponse()
            }
        }
    }

    suspend fun fetchResultsOfUser(amount: Int? = null): List<Result> {
        return withContext(Dispatchers.IO) {
            try {
                val id = sessionManager.getUserId()
                val response = service.getResultsOfUser(id, amount)
                withContext(Dispatchers.Main) {
                    for (result in response.results) {
                        Log.d("fetchResultsOfUser", result.resultId.toString() + " " + result.resultScore)
                    }
                }
                response.results
            } catch (e: Exception) {
                Log.e("fetchResultsOfUser", "Failed to fetch results", e)
                emptyList()
            }
        }
    }

    suspend fun fetchResultsOfSubject(subjectId: Int, amount: Int? = null): List<Result> {
        return withContext(Dispatchers.IO) {
            try {
                val id = sessionManager.getUserId()
                val response = service.getResultsOfSubject(id, subjectId, amount)
                withContext(Dispatchers.Main) {
                    for (result in response.results) {
                        Log.d("fetchResultsOfSubject", result.resultId.toString() + " " + result.resultScore)
                    }
                }
                response.results
            } catch (e: Exception) {
                Log.e("fetchResultsOfSubject", "Failed to fetch results", e)
                emptyList()
            }
        }
    }

    suspend fun fetchResultsOfFocus(focusId: Int, amount: Int? = null): List<Result> {
        return withContext(Dispatchers.IO) {
            try {
                val id = sessionManager.getUserId()
                val response = service.getResultsOfFocus(id, focusId, amount)
                withContext(Dispatchers.Main) {
                    for (result in response.results) {
                        Log.d("fetchResultsOfFocus", result.resultId.toString() + " " + result.resultScore)
                    }
                }
                response.results
            } catch (e: Exception) {
                Log.e("fetchResultsOfFocus", "Failed to fetch results", e)
                emptyList()
            }
        }
    }

    // ------------------- Friend Calls -------------------

    suspend fun fetchAllFriends(): AllFriendshipResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val id = sessionManager.getUserId()
                val response = service.getFriends(id)
                withContext(Dispatchers.Main) {
                    for (friend in response.acceptedFriendships) {
                        Log.d("fetchAllFriends", friend.friend?.userName+"")
                    }
                }
                response
            } catch (e: Exception) {
                Log.e("fetchAllFriends", "Failed to fetch friends", e)
                null
            }
        }
    }

    suspend fun addFriendship(friendId: Int): String {
        return withContext(Dispatchers.IO) {
            try {
                val id = sessionManager.getUserId()
                val response = service.addFriend(FriendRequestBody(id, friendId))
                withContext(Dispatchers.Main) {
                    Log.d("addFriendship", "Friendship added")
                }
            } catch (e: Exception) {
                Log.e("addFriendship", "Failed to add friendship", e)
            }.toString()
        }
    }

    suspend fun acceptFriendship(friendshipId: Int): FriendshipResponse {
        return withContext(Dispatchers.IO) {
            try {
                val response = service.acceptFriend(AcceptFriendRequestBody(friendshipId))
                withContext(Dispatchers.Main) {
                    Log.d("acceptFriendship", "Friendship accepted")
                }
                response
            } catch (e: Exception) {
                Log.e("acceptFriendship", "Failed to accept friendship", e)
                FriendshipResponse()
            }
        }
    }

    /**
     * Deletes a friendship -- für anfrage ablehnen oder freundschaft beenden
     */
    suspend fun deleteFriendship(friendshipId: Int): StatusResponse {
        return withContext(Dispatchers.IO) {
            try {
                val response = service.deleteFriend(friendshipId)
                withContext(Dispatchers.Main) {
                    Log.d("deleteFriendship", "Friendship deleted")
                }
                response
            } catch (e: Exception) {
                Log.e("deleteFriendship", "Failed to delete friendship", e)
                StatusResponse()
            }
        }
    }

    // ------------------- Challenge Calls -------------------

    suspend fun addChallengeForFocus(friendshipId: Int, focusId: Int) {
        return withContext(Dispatchers.IO) {
            try {
                val id = sessionManager.getUserId()
                val response = service.addChallengeForFocus(AdChallengeForFocusRequestBody(friendshipId, focusId, id))
                withContext(Dispatchers.Main) {
                    Log.d("addChallengeForFocus", "Challenge added")
                }
            } catch (e: Exception) {
                Log.e("addChallengeForFocus", "Failed to add challenge", e)
            }
        }
    }

    suspend fun addChallengeForSubject(friendshipId: Int, subjectId: Int) {
        return withContext(Dispatchers.IO) {
            try {
                val id = sessionManager.getUserId()
                val response = service.addChallengeForSubject(AdChallengeForSubjectRequestBody(friendshipId, subjectId, id))
                withContext(Dispatchers.Main) {
                    Log.d("addChallengeForSubject", "Challenge added")
                }
            } catch (e: Exception) {
                Log.e("addChallengeForSubject", "Failed to add challenge", e)
            }
        }
    }

    suspend fun deleteChallenge(challengeId: Int): StatusResponse {
        return withContext(Dispatchers.IO) {
            try {
                val response = service.deleteChallenge(challengeId)
                withContext(Dispatchers.Main) {
                    Log.d("deleteChallenge", "Challenge deleted")
                }
                response
            } catch (e: Exception) {
                Log.e("deleteChallenge", "Failed to delete challenge", e)
                StatusResponse()
            }
        }
    }

    suspend fun assignResultToChallenge(challengeId: Int, resultId: Int): AssignResultToChallengeResponse {
        return withContext(Dispatchers.IO) {
            try {
                val response = service.assignResultToChallenge(AssignResultToChallengeRequestBody(challengeId, resultId))
                withContext(Dispatchers.Main) {
                    Log.d("assignResultToChallenge", "Result assigned to challenge")
                }
                response
            } catch (e: Exception) {
                Log.e("assignResultToChallenge", "Failed to assign result to challenge", e)
                AssignResultToChallengeResponse()
            }
        }
    }

    suspend fun getChallengesOfFriendship(friendshipId: Int): ChallengeResponse {
        return withContext(Dispatchers.IO) {
            try {
                val id = sessionManager.getUserId()
                val response = service.getChallengesOfFriendship(friendshipId, id)
                withContext(Dispatchers.Main) {
                    for (challenge in response.openChallenges) {
                        Log.d("getChallengesOfFriendship", challenge.challengeId.toString() + " " + challenge.focus)
                    }
                }
                response
            } catch (e: Exception) {
                Log.e("getChallengesOfFriendship", "Failed to fetch challenges", e)
                ChallengeResponse()
            }
        }
    }

    suspend fun getChallengesForSubject(subjectId: Int): ChallengeResponse {
        return withContext(Dispatchers.IO) {
            try {
                val id = sessionManager.getUserId()
                val response = service.getChallengesForSubject(subjectId, id)
                withContext(Dispatchers.Main) {
                    for (challenge in response.openChallenges) {
                        Log.d("getChallengesForSubject", challenge.challengeId.toString() + " " + challenge.focus)
                    }
                }
                response
            } catch (e: Exception) {
                Log.e("getChallengesForSubject", "Failed to fetch challenges", e)
                ChallengeResponse()
            }
        }
    }

    suspend fun getDoneChallenges(): DoneChallengesResponse {
        return withContext(Dispatchers.IO) {
            try {
                val id = sessionManager.getUserId()
                val response = service.getDoneChallenges(id)
                withContext(Dispatchers.Main) {
                    for (challenge in response.doneChallenges) {
                        Log.d("getDoneChallenges()", challenge.challengeId.toString() + " " + challenge.focus)
                    }
                }
                response
            } catch (e: Exception) {
                Log.e("getDoneChallenges()", "Failed to fetch challenges", e)
                DoneChallengesResponse()
            }
        }
    }

    suspend fun fetchAllOpenChallenges(): OpenChallengesResponse {
        return withContext(Dispatchers.IO) {
            try {
                val id = sessionManager.getUserId()
                val response = service.getOpenChallenges(id)
                withContext(Dispatchers.Main) {
                    for (challenge in response.openChallenges) {
                        Log.d("fetchAllOpenChallenges()", challenge.challengeId.toString() + " " + challenge.focus)
                    }
                }
                response
            } catch (e: Exception) {
                Log.e("fetchAllOpenChallenges()", "Failed to fetch challenges", e)
                OpenChallengesResponse()
            }
        }
    }





}

