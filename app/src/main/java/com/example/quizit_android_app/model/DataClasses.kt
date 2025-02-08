package com.example.quizit_android_app.model

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Base64
import androidx.navigation.NavType
import com.fasterxml.jackson.annotation.JsonInclude
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.Serializable

// ------------------- User Dataclasses -------------------
data class UserResponse(
    @SerializedName("status") val status: String,
    @SerializedName("user") val user: User? = null,
    @SerializedName("users") val users: List<User?> = emptyList(),
    @SerializedName("reason") val reason: String?
)

@kotlinx.serialization.Serializable
@Parcelize
data class User (
    @SerializedName("userId"       ) var userId       : Int?    = null,
    @SerializedName("userName"     ) var userName     : String? = null,
    @SerializedName("userYear"     ) var userYear     : Int?    = null,
    @SerializedName("userFullname" ) var userFullname : String? = null,
    @SerializedName("userClass"    ) var userClass    : String? = null,
    @SerializedName("userType"     ) var userType     : String? = null,
    @SerializedName("userMail"     ) var userMail     : String? = null
): Parcelable

data class LoginRequestBody(
    @SerializedName("userName") val username: String,
    @SerializedName("password") val password: String
)

data class ChangeUserYearRequestBody(
    @SerializedName("userId") val userId: Int?,
    @SerializedName("userYear") val userYear: Int?
)

data class UserStatsResponse (
    @SerializedName("status" ) var status : String? = null,
    @SerializedName("stats"  ) var stats  : Stats?  = Stats()
)

data class Stats (
    @SerializedName("avgPoints" ) var avgPoints : Double? = null,
    @SerializedName("ranking"   ) var ranking   : Int?    = null,
    @SerializedName("winRate"   ) var winRate   : Double?    = null
)


// ------------------- Subject Dataclasses -------------------
@kotlinx.serialization.Serializable
@Parcelize
data class Subject (
    @SerializedName("subjectId"           ) var subjectId           : Int   = 0,
    @SerializedName("subjectName"         ) var subjectName         : String = "",
    @SerializedName("subjectImageAddress" ) var subjectImageAddress : String = ""

): Parcelable

data class SubjectsResponse(
    @SerializedName("status") val status: String,
    @SerializedName("subjects") val subjects: List<Subject>
)

data class NewSubjectRequest(
    @SerializedName("subjectName") val subjectName: String,
    @SerializedName("subjectImageAddress") val subjectImageAddress: String
)

// ------------------- Focus Dataclasses -------------------

data class FocusResponse (
    @SerializedName("status" ) var status : String?          = null,
    @SerializedName("focuses"  ) var focus  : List<Focus> = arrayListOf()
)

@kotlinx.serialization.Serializable
@Parcelize
data class Focus (
    @SerializedName("focusId"           ) var focusId           : Int?    = null,
    @SerializedName("focusName"         ) var focusName         : String? = null,
    @SerializedName("focusYear"         ) var focusYear         : Int?    = null,
    @SerializedName("focusImageAddress" ) var focusImageAddress : String? = null,
    @SerializedName("subjectId"         ) var subjectId         : Int?    = null,
    @SerializedName("questionCount"     ) var questionCount     : Int?    = null
): Parcelable

// ------------------- Quiz Dataclasses -------------------

data class QuizOfSubjectResponse (
    @SerializedName("status"    ) var status    : String?              = null,
    @SerializedName("subjectId" ) var subjectId : Int?                 = null,
    @SerializedName("questions" ) var questions : ArrayList<Questions> = arrayListOf()
)

data class QuizOfFocusResponse (
    @SerializedName("status"    ) var status    : String?              = null,
    @SerializedName("focusId"   ) var focusId   : Int?                 = null,
    @SerializedName("questions" ) var questions : ArrayList<Questions> = arrayListOf()
)

data class Options (
    @SerializedName("optionId"      ) var optionId      : Int?     = null,
    @SerializedName("optionText"    ) var optionText    : String?  = null,
    @SerializedName("optionCorrect" ) var optionCorrect : Boolean? = null
)

data class Questions (
    @SerializedName("questionId"   ) var questionId   : Int?               = null,
    @SerializedName("questionText" ) var questionText : String?            = null,
    @SerializedName("options"      ) var options      : ArrayList<Options> = arrayListOf(),
    @SerializedName("focusId"      ) var focusId      : Int?               = null,
    @SerializedName("mChoice"      ) var mChoice      : Boolean?           = null,
    @SerializedName("textInput"    ) var textInput    : Boolean?           = null,
    @SerializedName("imageAddress" ) var imageAddress : String?            = null
)

// ------------------- Result Dataclasses -------------------

data class GetResultsResponse (
    @SerializedName("status"  ) var status  : String?            = null,
    @SerializedName("results" ) var results : ArrayList<Result> = arrayListOf()
)

data class Result (
    @SerializedName("resultId"    ) var resultId    : Int?    = null,
    @SerializedName("resultScore" ) var resultScore : Double?    = null,
    @SerializedName("userId"      ) var userId      : Int?    = null,
    @SerializedName("focusId"     ) var focusId     : Int?    = null,
    @SerializedName("subjectId"   ) var subjectId   : String? = null,
    @SerializedName("resultDate"  ) var resultDate  : String? = null
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class PostResultRequestBody (
    @SerializedName("resultScore") val resultScore: Double,
    @SerializedName("userId") val userId: Int?,
    @SerializedName("focusId") val focusId: Int? = null,
    @SerializedName("subjectId") val subjectId: Int? = null
)

// ------------------- Friendship Dataclasses -------------------

data class FriendshipResponse (
    @SerializedName("status"     ) var status     : String?     = null,
    @SerializedName("friendship" ) var friendship : Friendship? = Friendship()
)

data class Friendship (
    @SerializedName("friendshipId"      ) var friendshipId      : Int?     = null,
    @SerializedName("friend"            ) var friend            : Friend?  = Friend(),
    @SerializedName("friendshipPending" ) var friendshipPending : Boolean? = null,
    @SerializedName("friendshipSince"   ) var friendshipSince   : String?  = null,
    @SerializedName("actionReq"         ) var actionReq         : Boolean? = null
)

data class AllFriendshipResponse (
    @SerializedName("status"              ) var status              : String?                        = null,
    @SerializedName("userId"              ) var userId              : Int?                           = null,
    @SerializedName("acceptedFriendships" ) var acceptedFriendships : ArrayList<AcceptedFriendships> = arrayListOf(),
    @SerializedName("pendingFriendships"  ) var pendingFriendships  : ArrayList<PendingFriendships>  = arrayListOf()
)

data class Friend (
    @SerializedName("userId"       ) var userId       : Int?     = null,
    @SerializedName("userName"     ) var userName     : String?  = null,
    @SerializedName("userYear"     ) var userYear     : Int?     = null,
    @SerializedName("userFullname" ) var userFullname : String?  = null,
    @SerializedName("userClass"    ) var userClass    : String?  = null,
    @SerializedName("userType"     ) var userType     : String?  = null,
    @SerializedName("userMail"     ) var userMail     : String?  = null,
    @SerializedName("userBlocked"  ) var userBlocked  : Boolean? = null
)

data class AcceptedFriendships (
    @SerializedName("friendshipId"    ) var friendshipId    : Int?    = null,
    @SerializedName("friend"          ) var friend          : Friend? = Friend(),
    @SerializedName("friendshipSince" ) var friendshipSince : String? = null
)

data class PendingFriendships (
    @SerializedName("friendshipId"    ) var friendshipId    : Int?     = null,
    @SerializedName("friend"          ) var friend          : Friend?  = Friend(),
    @SerializedName("actionReq"       ) var actionReq       : Boolean? = null,
    @SerializedName("friendshipSince" ) var friendshipSince : String?  = null
)

data class FriendRequestBody (
    @SerializedName("user1Id") val userId: Int? = null,
    @SerializedName("user2Id") val friendId: Int
)

data class AcceptFriendRequestBody(
    @SerializedName("id") val id: Int
)

// ------------------- Challenge Dataclasses -------------------

data class AdChallengeForFocusRequestBody (
    @SerializedName("friendshipId") val friendshipId: Int,
    @SerializedName("focusId") val focusId: Int,
    @SerializedName("userId") val userId: Int?
)

data class AdChallengeForSubjectRequestBody (
    @SerializedName("friendshipId") val friendshipId: Int,
    @SerializedName("subjectId") val focusId: Int,
    @SerializedName("userId") val userId: Int?
)

data class AssignResultToChallengeRequestBody (
    @SerializedName("challengeId") val challengeId: Int,
    @SerializedName("resultId") val resultId: Int
)

data class AssignResultToChallengeResponse (
    @SerializedName("status"    ) var status    : String?    = null,
    @SerializedName("challenge" ) var challenge : Challenge? = Challenge()
)

data class FriendScore (
    @SerializedName("resultId"       ) var resultId       : Int?    = null,
    @SerializedName("resultScore"    ) var resultScore    : Double? = null,
    @SerializedName("userId"         ) var userId         : Int?    = null,
    @SerializedName("focus"          ) var focus          : Focus?  = Focus(),
    @SerializedName("resultDateTime" ) var resultDateTime : String? = null
)

data class Challenge (
    @SerializedName("challengeId"       ) var challengeId       : Int?         = null,
    @SerializedName("challengeDateTime" ) var challengeDateTime : String?      = null,
    @SerializedName("friendship"        ) var friendship        : Friendship?  = Friendship(),
    @SerializedName("focus"             ) var focus             : Focus?       = Focus(),
    @SerializedName("friendScore"       ) var friendScore       : FriendScore? = FriendScore()
)

data class ChallengeResponse (
    @SerializedName("status"         ) var status         : String?                   = null,
    @SerializedName("openChallenges" ) var openChallenges : ArrayList<OpenChallenges> = arrayListOf(),
    @SerializedName("doneChallenges" ) var doneChallenges : ArrayList<DoneChallenges> = arrayListOf()
)

data class OpenChallenges (
    @SerializedName("challengeId"       ) var challengeId       : Int?        = null,
    @SerializedName("challengeDateTime" ) var challengeDateTime : String?     = null,
    @SerializedName("friendship"        ) var friendship        : Friendship? = Friendship(),
    @SerializedName("focus"             ) var focus             : Focus?      = Focus(),
    @SerializedName("subject"           ) var subject           : Subject?    = null,
    @SerializedName("score"             ) var score             : Double?     = null,
    @SerializedName("friendScore"       ) var friendScore       : Double?     = null
)

data class Score (
    @SerializedName("resultId"       ) var resultId       : Int?    = null,
    @SerializedName("resultScore"    ) var resultScore    : Double? = null,
    @SerializedName("userId"         ) var userId         : Int?    = null,
    @SerializedName("focus"          ) var focus          : Focus?  = Focus(),
    @SerializedName("resultDateTime" ) var resultDateTime : String? = null
)

data class DoneChallenges (
    @SerializedName("challengeId"       ) var challengeId       : Int?         = null,
    @SerializedName("challengeDateTime" ) var challengeDateTime : String?      = null,
    @SerializedName("friendship"        ) var friendship        : Friendship?  = Friendship(),
    @SerializedName("focus"             ) var focus             : Focus?       = Focus(),
    @SerializedName("subject"           ) var subject           : Subject?    = null,
    @SerializedName("score"             ) var score             : Score?       = Score(),
    @SerializedName("friendScore"       ) var friendScore       : FriendScore? = FriendScore()
)

data class DoneChallengesResponse (
    @SerializedName("status"         ) var status         : String?                   = null,
    @SerializedName("doneChallenges" ) var doneChallenges : ArrayList<DoneChallenges> = arrayListOf()
)


object CustomNavType {

    val SubjectType = object : NavType<Subject>(
        isNullableAllowed = false,
    )  {
        override fun get(bundle: Bundle, key: String): Subject? {
            return Json.decodeFromString(bundle.getString(key)?: return  null)
        }

        override fun parseValue(value: String): Subject {
            return Json.decodeFromString(decodeFromBase64UrlSafe(value))
        }

        override fun put(bundle: Bundle, key: String, value: Subject) {
            bundle.putString(key, Json.encodeToString(value))
        }

        override fun serializeAsValue(value: Subject): String {
            return encodeToBase64UrlSafe(Json.encodeToString(value))
        }
    }

    val FocusType = object : NavType<Focus?>(
        isNullableAllowed = true,
    ) {
        override fun get(bundle: Bundle, key: String): Focus? {
            val encoded = bundle.getString(key) ?: return null
            if (encoded == "null") return null
            return try {
                Json.decodeFromString(decodeFromBase64UrlSafe(encoded))
            } catch (e: Exception) {
                null
            }
        }

        override fun parseValue(value: String): Focus? {
            if (value == "null") return null
            return try {
                Json.decodeFromString(decodeFromBase64UrlSafe(value))
            } catch (e: Exception) {
                null
            }
        }

        override fun put(bundle: Bundle, key: String, value: Focus?) {
            if (value == null) {
                bundle.putString(key, "null")
            } else {
                bundle.putString(key, encodeToBase64UrlSafe(Json.encodeToString(value)))
            }
        }

        override fun serializeAsValue(value: Focus?): String {
            return if (value == null) "null" else encodeToBase64UrlSafe(Json.encodeToString(value))
        }
    }
}

fun encodeToBase64UrlSafe(data: String): String {
    return Base64.encodeToString(data.toByteArray(Charsets.UTF_8), Base64.URL_SAFE or Base64.NO_WRAP)
}

fun decodeFromBase64UrlSafe(encoded: String): String {
    return String(Base64.decode(encoded, Base64.URL_SAFE or Base64.NO_WRAP), Charsets.UTF_8)
}

