package com.example.quizit_android_app.ui.social

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.quizit_android_app.model.retrofit.DoneChallenges
import com.example.quizit_android_app.model.retrofit.OpenChallenges
import com.example.quizit_android_app.model.retrofit.User
import com.example.quizit_android_app.model.retrofit.UserStatsResponse
import com.example.quizit_android_app.navigation.UserDetailRoute
import com.example.quizit_android_app.usecases.challenge.GetChallengesOfFriendshipUseCase
import com.example.quizit_android_app.usecases.challenge.GetDoneChallengesUseCase
import com.example.quizit_android_app.usecases.friendship.FriendshipStatus
import com.example.quizit_android_app.usecases.friendship.GetFriendshipStatusUseCase
import com.example.quizit_android_app.usecases.user.GetUserStatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getUserStatsUseCase: GetUserStatsUseCase,
    private val getChallengesOfFriendshipUseCase: GetChallengesOfFriendshipUseCase,
    private val getDoneChallengesUseCase: GetDoneChallengesUseCase,
    private val getFriendshipStatusUseCase: GetFriendshipStatusUseCase

): ViewModel() {

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _user = mutableStateOf<User?>(null)
    val user: State<User?> = _user

    private val _userStats = mutableStateOf<UserStatsResponse?>(null)
    val userStats: State<UserStatsResponse?> = _userStats

    private val _friendshipStatus = mutableStateOf<FriendshipStatus>(FriendshipStatus.NONE)
    val friendshipStatus: State<FriendshipStatus> = _friendshipStatus

    private val _openChallenges = mutableStateOf(listOf<OpenChallenges>())
    val openChallenges: State<List<OpenChallenges>> = _openChallenges

    private val _doneChallenges = mutableStateOf(listOf<DoneChallenges>())
    val doneChallenges: State<List<DoneChallenges>> = _doneChallenges

    init {
        val friendshipId = UserDetailRoute.from(savedStateHandle).friendshipId
        val user = UserDetailRoute.from(savedStateHandle).user

        Log.d("UserDetailViewModel", "FriendshipId: "+friendshipId)

        if(friendshipId != null) {
            setFriendshipContent(friendshipId)
        } else {
            setUserContent(user)
        }
    }

    private fun setFriendshipContent(friendshipId: Int) {

        viewModelScope.launch {

            _isLoading.value = true
            try {

                _friendshipStatus.value = getFriendshipStatusUseCase(friendshipId)
                Log.d("UserDetailViewModel", "FriendshisaddasadsadspId: "+friendshipId)
                val challenges = getChallengesOfFriendshipUseCase(friendshipId)
                Log.d("UserDetailViewModel", "Challenges: "+challenges)
                val openChallenges = challenges.openChallenges
                val doneChallenges = challenges.doneChallenges

                val friend = openChallenges.first().friendship?.friend

                val user = User(
                    userId = friend?.userId,
                    userName = friend?.userName,
                    userYear = friend?.userYear,
                    userFullname = friend?.userFullname,
                    userClass = friend?.userClass,
                    userType = friend?.userType,
                    userMail = friend?.userMail,

                )

                _user.value = user
                _openChallenges.value = openChallenges
                _doneChallenges.value = doneChallenges
                _userStats.value = getUserStatsUseCase(user.userId)


            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }


    }

    private fun setUserContent(user: User?) {
        _user.value = user!!

        viewModelScope.launch {
            _isLoading.value = true
            try {
                _userStats.value = getUserStatsUseCase(user.userId)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }



    }

}

