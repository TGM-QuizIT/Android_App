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
import com.example.quizit_android_app.usecases.challenge.DeleteChallengeUseCase
import com.example.quizit_android_app.usecases.challenge.GetChallengesOfFriendshipUseCase
import com.example.quizit_android_app.usecases.challenge.GetDoneChallengesUseCase
import com.example.quizit_android_app.usecases.friendship.AcceptFriendshipUseCase
import com.example.quizit_android_app.usecases.friendship.AddFriendshipUseCase
import com.example.quizit_android_app.usecases.friendship.DeleteDeclineFriendshipUseCase
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
    private val getFriendshipStatusUseCase: GetFriendshipStatusUseCase,
    private val addFriendshipUseCase: AddFriendshipUseCase,
    private val deleteDeclineFriendshipUseCase: DeleteDeclineFriendshipUseCase,
    private val acceptFriendshipUseCase: AcceptFriendshipUseCase,
    private val deleteChallengeUseCase: DeleteChallengeUseCase

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

    private val _friendshipId = mutableStateOf<Int?>(null)
    val friendshipId: State<Int?> = _friendshipId
    init {
        val friendshipId = UserDetailRoute.from(savedStateHandle).friendshipId
        val user = UserDetailRoute.from(savedStateHandle).user

        Log.d("UserDetailViewModel", "FriendshipId: "+friendshipId)

        if(friendshipId != null) {
            setFriendshipContent(friendshipId, user)
        } else {
            setUserContent(user)
        }
    }

    private fun setFriendshipContent(friendshipId: Int, user: User) {

        _friendshipId.value = friendshipId

        viewModelScope.launch {

            _isLoading.value = true
            try {

                _friendshipStatus.value = getFriendshipStatusUseCase(friendshipId)
                Log.d("UserDetailViewModel", "FriendshisaddasadsadspId: "+friendshipId)
                val challenges = getChallengesOfFriendshipUseCase(friendshipId)
                Log.d("UserDetailViewModel", "Challenges: "+challenges)
                val openChallenges = challenges.openChallenges
                val doneChallenges = challenges.doneChallenges


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

    fun addFriend(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = addFriendshipUseCase(id)

                _user.value = User(
                    userId = response.friendship?.friend?.userId,
                    userName = response.friendship?.friend?.userName,
                    userFullname = response.friendship?.friend?.userFullname,
                    userType = response.friendship?.friend?.userType,
                    userYear = response.friendship?.friend?.userYear,
                    userClass = response.friendship?.friend?.userClass,
                    userMail = response.friendship?.friend?.userMail,
                )

                _friendshipStatus.value = getFriendshipStatusUseCase(response.friendship?.friendshipId!!)
                _friendshipId.value = response.friendship?.friendshipId
                _userStats.value = getUserStatsUseCase(_user.value?.userId)
                val challenges = getChallengesOfFriendshipUseCase(response.friendship?.friendshipId!!)
                _openChallenges.value = challenges.openChallenges
                _doneChallenges.value = challenges.doneChallenges
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun acceptFriendship() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = acceptFriendshipUseCase(friendshipId.value!!)

                _friendshipStatus.value = getFriendshipStatusUseCase(friendshipId.value!!)
                _user.value = User(
                    userId = response.friendship?.friend?.userId,
                    userName = response.friendship?.friend?.userName,
                    userFullname = response.friendship?.friend?.userFullname,
                    userType = response.friendship?.friend?.userType,
                    userYear = response.friendship?.friend?.userYear,
                    userClass = response.friendship?.friend?.userClass,
                    userMail = response.friendship?.friend?.userMail,
                )

                _friendshipId.value = response.friendship?.friendshipId
                _userStats.value = getUserStatsUseCase(_user.value?.userId)
                val challenges = getChallengesOfFriendshipUseCase(response.friendship?.friendshipId!!)
                _openChallenges.value = challenges.openChallenges
                _doneChallenges.value = challenges.doneChallenges
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }

    }

    fun removeFriendship() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = deleteDeclineFriendshipUseCase(friendshipId.value!!)

                if(response.status == "Success") {
                    _friendshipStatus.value = FriendshipStatus.NONE
                    _openChallenges.value = listOf()
                    _doneChallenges.value = listOf()

                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }

    }

    fun declineChallenge(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                deleteChallengeUseCase(id)

                _openChallenges.value = _openChallenges.value.filter { it.challengeId != id }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

}

