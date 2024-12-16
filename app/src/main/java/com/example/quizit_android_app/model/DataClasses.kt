package com.example.quizit_android_app.model

import com.google.gson.annotations.SerializedName

data class Subjects (
    @SerializedName("subjectId"           ) var subjectId           : Int,
    @SerializedName("subjectName"         ) var subjectName         : String = "",
    @SerializedName("subjectImageAddress" ) var subjectImageAddress : String = ""

)

data class SubjectsResponse(
    @SerializedName("status") val status: String,
    @SerializedName("subjects") val subjects: List<Subjects>
)

data class NewSubjectRequest(
    @SerializedName("subjectName") val subjectName: String,
    @SerializedName("subjectImageAddress") val subjectImageAddress: String
)