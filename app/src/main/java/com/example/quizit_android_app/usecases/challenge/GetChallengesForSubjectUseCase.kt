package com.example.quizit_android_app.usecases.challenge

import com.example.quizit_android_app.model.retrofit.ChallengeResponse
import com.example.quizit_android_app.model.retrofit.DataRepo
import com.example.quizit_android_app.usecases.focus.GetFocusForSubjectUseCase
import com.example.quizit_android_app.usecases.subjects.GetAllSubjectsUseCase
import javax.inject.Inject

class GetChallengesForSubjectUseCase @Inject constructor(
    val dataRepo: DataRepo,
) {
    suspend operator fun invoke(subjectId: Int? = null, focusId: Int? = null): ChallengeResponse {
        return if (subjectId != null) {
            val response = dataRepo.getChallengesForSubject(subjectId)
            val sortedOpen = response.openChallenges.sortedByDescending { it.challengeDateTime }
            val sortedDone = response.doneChallenges.sortedByDescending { it.challengeDateTime }
            ChallengeResponse("Success", ArrayList(sortedOpen), ArrayList(sortedDone))
        } else if (focusId != null) {
            val focuses = dataRepo.fetchAllFocusOfUser()
            val subject = focuses.focus.find { it.focusId == focusId }?.subjectId
            val response = dataRepo.getChallengesForSubject(subject!!)
            val sortedOpen = response.openChallenges.sortedByDescending { it.challengeDateTime }
            val sortedDone = response.doneChallenges.sortedByDescending { it.challengeDateTime }
            ChallengeResponse("Success", ArrayList(sortedOpen), ArrayList(sortedDone))
        } else {
            ChallengeResponse()
        }


    }
}