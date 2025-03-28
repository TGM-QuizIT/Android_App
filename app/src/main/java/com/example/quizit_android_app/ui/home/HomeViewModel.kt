package com.example.quizit_android_app.ui.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.quizit_android_app.model.retrofit.Focus
import com.example.quizit_android_app.model.retrofit.Friend
import com.example.quizit_android_app.model.retrofit.FriendScore
import com.example.quizit_android_app.model.retrofit.Friendship
import com.example.quizit_android_app.model.retrofit.OpenChallenges
import com.example.quizit_android_app.model.retrofit.Subject
import com.example.quizit_android_app.model.retrofit.UserStatsResponse
import com.example.quizit_android_app.navigation.HomeRoute
import com.example.quizit_android_app.usecases.challenge.DeleteChallengeUseCase
import com.example.quizit_android_app.usecases.challenge.GetChallengesOfUserUseCase
import com.example.quizit_android_app.usecases.localdata.challenge.SyncLocalDoneChallengesUseCase
import com.example.quizit_android_app.usecases.localdata.challenge.SyncLocalOpenChallengesUseCase
import com.example.quizit_android_app.usecases.localdata.focus.SyncLocalFocusUseCase
import com.example.quizit_android_app.usecases.localdata.friendship.SyncLocalAcceptedFriendsUseCase
import com.example.quizit_android_app.usecases.localdata.friendship.SyncLocalPendingFriendsUseCase
import com.example.quizit_android_app.usecases.localdata.subjects.SyncLocalSubjectsUseCase
import com.example.quizit_android_app.usecases.localdata.userstats.SyncLocalUserStatsUseCase
import com.example.quizit_android_app.usecases.subjects.GetAllSubjectsUseCase
import com.example.quizit_android_app.usecases.user.GetUserStatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getAllSubjectsUseCase: GetAllSubjectsUseCase,
    private val getUserStatsUseCase: GetUserStatsUseCase,
    private val getChallengesOfUserUseCase: GetChallengesOfUserUseCase,
    private val syncSubjectsUseCase: SyncLocalSubjectsUseCase,
    private val syncFocusUseCase: SyncLocalFocusUseCase,
    private val syncChallengesOfUserUseCase: SyncLocalOpenChallengesUseCase,
    private val syncLocalDoneChallengesUseCase: SyncLocalDoneChallengesUseCase,
    private val syncLocalAcceptedFriendsUseCase: SyncLocalAcceptedFriendsUseCase,
    private val syncLocalPendingFriendsUseCase: SyncLocalPendingFriendsUseCase,
    private val syncUserStatsUseCase: SyncLocalUserStatsUseCase,
    private val deleteChallengeUseCase: DeleteChallengeUseCase
): ViewModel() {

    private var _subjectList by mutableStateOf(listOf<Subject>())
    val subjectList: List<Subject> get() = _subjectList

    private var _stats by mutableStateOf<UserStatsResponse?>(null)
    val stats: UserStatsResponse? get() = _stats

    private var _isLoading by mutableStateOf(false)
    val isLoading: Boolean get() = _isLoading

    private var _challenges by mutableStateOf(listOf<OpenChallenges>())
    val challenges: List<OpenChallenges> get() = _challenges

    init {
        setContent()

    }



    fun setContent() {
        viewModelScope.launch {
            _isLoading = true
            try {

                if(_subjectList.isEmpty()) {
                    _subjectList = getAllSubjectsUseCase()


                }
                if(_stats == null) _stats = getUserStatsUseCase()

                Log.d("HomeViewModel", "stats: $_stats")
                _challenges = getChallengesOfUserUseCase().openChallenges

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading = false
            }
        }
    }

    fun deleteChallenge(id: Int) {
        viewModelScope.launch {
            try {
                _challenges = _challenges.filter { it.challengeId != id }
                deleteChallengeUseCase(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun refreshData(onComplete: () -> Unit) {
        viewModelScope.launch {
            try {

                syncSubjectsUseCase()
                syncFocusUseCase()
                syncChallengesOfUserUseCase()
                syncUserStatsUseCase()

                _stats = getUserStatsUseCase()
                _subjectList = getAllSubjectsUseCase()
                _challenges = getChallengesOfUserUseCase().openChallenges

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                onComplete()
            }
        }
    }
}