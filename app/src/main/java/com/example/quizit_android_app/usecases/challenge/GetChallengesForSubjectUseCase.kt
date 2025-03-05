package com.example.quizit_android_app.usecases.challenge

import com.example.quizit_android_app.model.ContentDataStore
import com.example.quizit_android_app.model.retrofit.ChallengeResponse
import com.example.quizit_android_app.model.retrofit.DataRepo
import com.example.quizit_android_app.usecases.focus.GetFocusForSubjectUseCase
import com.example.quizit_android_app.usecases.subjects.GetAllSubjectsUseCase
import javax.inject.Inject

class GetChallengesForSubjectUseCase @Inject constructor(
    val dataRepo: DataRepo,
    val contentDataStore: ContentDataStore
) {
    suspend operator fun invoke(subjectId: Int? = null, focusId: Int? = null): ChallengeResponse {
        return if (subjectId != null) {
            val localOpen = contentDataStore.getOpenChallenges().filter { it.subject?.subjectId == subjectId }
            val localDone = contentDataStore.getDoneChallenges().filter { it.subject?.subjectId == subjectId }
            if (localOpen.isEmpty() || localDone.isEmpty()) {
                val response = dataRepo.getChallengesForSubject(subjectId)
                contentDataStore.saveOpenChallenges(response.openChallenges)
                contentDataStore.saveDoneChallenges(response.doneChallenges)
                ChallengeResponse("Success", ArrayList(response.openChallenges.sortedByDescending { it.challengeDateTime }), ArrayList(response.doneChallenges.sortedByDescending { it.challengeDateTime }))
            } else {
                ChallengeResponse("Success", ArrayList(localOpen.sortedByDescending { it.challengeDateTime }), ArrayList(localDone.sortedByDescending { it.challengeDateTime }))
            }
        } else if (focusId != null) {
            val localFocus = contentDataStore.getFocus().find { it.focusId == focusId }
            val localOpen = contentDataStore.getOpenChallenges().filter { it.subject?.subjectId == localFocus?.subjectId }
            val localDone = contentDataStore.getDoneChallenges().filter { it.subject?.subjectId == localFocus?.subjectId }
            if (localOpen.isEmpty() || localDone.isEmpty()) {
                val focuses = dataRepo.fetchAllFocusOfUser()
                val subject = focuses.focus.find { it.focusId == focusId }?.subjectId
                val response = dataRepo.getChallengesForSubject(subject!!)
                contentDataStore.saveOpenChallenges(response.openChallenges)
                contentDataStore.saveDoneChallenges(response.doneChallenges)
                ChallengeResponse("Success", ArrayList(response.openChallenges.sortedByDescending { it.challengeDateTime }), ArrayList(response.doneChallenges.sortedByDescending { it.challengeDateTime }))
            } else {
                ChallengeResponse("Success", ArrayList(localOpen.sortedByDescending { it.challengeDateTime }), ArrayList(localDone.sortedByDescending { it.challengeDateTime }))
            }
        } else {
            ChallengeResponse()
        }


    }
}