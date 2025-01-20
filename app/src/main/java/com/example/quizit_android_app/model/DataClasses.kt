package com.example.quizit_android_app.model

import com.google.gson.annotations.SerializedName

// ------------------- User Dataclasses -------------------
data class UserResponse(
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

data class ChangeUserYearRequestBody(
    @SerializedName("userId") val userId: Int?,
    @SerializedName("userYear") val userYear: Int?
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

// ------------------- Focus Dataclasses -------------------

data class FocusResponse (
    @SerializedName("status" ) var status : String?          = null,
    @SerializedName("focus"  ) var focus  : List<Focus> = arrayListOf()
)

data class Focus (
    @SerializedName("focusId"           ) var focusId           : Int?    = null,
    @SerializedName("focusName"         ) var focusName         : String? = null,
    @SerializedName("focusYear"         ) var focusYear         : Int?    = null,
    @SerializedName("focusImageAddress" ) var focusImageAddress : String? = null,
    @SerializedName("subjectId"         ) var subjectId         : Int?    = null,
    @SerializedName("questionCount"     ) var questionCount     : Int?    = null
)

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



