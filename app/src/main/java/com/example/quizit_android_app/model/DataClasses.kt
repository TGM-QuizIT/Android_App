package com.example.quizit_android_app.model

import com.google.gson.annotations.SerializedName
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject

// ------------------- User Persist Dataclasses -------------------

data class UserPersist (
    var userId       : Int?    = null,
    var userName     : String? = null,
    var userYear     : Int?    = null,
    var userFullname : String? = null,
    var userClass    : String? = null,
    var userType     : String? = null,
    var userMail     : String? = null
)

// ------------------- User Dataclasses -------------------
data class LoginResponse(
    @SerializedName("status") val status: String,
    @SerializedName("user") val user: User?,
    @SerializedName("reason") val reason: String?
)

data class User (
    @SerializedName("userId"       ) var userId       : Int?    = null,
    @SerializedName("userName"     ) var userName     : String? = null,
    @SerializedName("userYear"     ) var userYear     : Int?    = null,
    @SerializedName("userFullname" ) var userFullname : String? = null,
    @SerializedName("userClass"    ) var userClass    : String? = null,
    @SerializedName("userType"     ) var userType     : String? = null,
    @SerializedName("userMail"     ) var userMail     : String? = null
)

data class LoginRequestBody(
    @SerializedName("userName") val username: String,
    @SerializedName("password") val password: String
)


// ------------------- Subject Dataclasses -------------------
data class Subject (
    @SerializedName("subjectId"           ) var subjectId           : Int,
    @SerializedName("subjectName"         ) var subjectName         : String = "",
    @SerializedName("subjectImageAddress" ) var subjectImageAddress : String = ""

)

data class SubjectsResponse(
    @SerializedName("status") val status: String,
    @SerializedName("subjects") val subjects: List<Subject>
)

data class NewSubjectRequest(
    @SerializedName("subjectName") val subjectName: String,
    @SerializedName("subjectImageAddress") val subjectImageAddress: String
)

