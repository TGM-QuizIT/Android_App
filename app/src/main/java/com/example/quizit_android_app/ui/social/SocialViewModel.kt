package com.example.quizit_android_app.ui.social

import  android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.quizit_android_app.model.retrofit.AcceptedFriendship
import com.example.quizit_android_app.model.retrofit.DoneChallenges
import com.example.quizit_android_app.model.retrofit.PendingFriendship
import com.example.quizit_android_app.model.retrofit.User
import com.example.quizit_android_app.model.retrofit.UserStatsResponse
import com.example.quizit_android_app.navigation.SocialRoute
import com.example.quizit_android_app.usecases.challenge.GetDoneChallengesUseCase
import com.example.quizit_android_app.usecases.friendship.AcceptFriendshipUseCase
import com.example.quizit_android_app.usecases.friendship.DeleteDeclineFriendshipUseCase
import com.example.quizit_android_app.usecases.friendship.GetAcceptedFriendshipsUseCase
import com.example.quizit_android_app.usecases.friendship.GetPendingFriendshipsUseCase
import com.example.quizit_android_app.usecases.result.GetResultsUserUseCase
import com.example.quizit_android_app.usecases.user.GetAllUsersUseCase
import com.example.quizit_android_app.usecases.user.GetUserStatsUseCase
import com.example.quizit_android_app.usecases.user.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class SocialViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    val getAllFriendshipsUseCase: GetAcceptedFriendshipsUseCase,
    val getPendingFriendshipsUseCase: GetPendingFriendshipsUseCase,
    val getAllUsersUseCase: GetAllUsersUseCase,
    val getUserStatsUseCase: GetUserStatsUseCase,
    val getUserUseCase: GetUserUseCase,
    val getResultsUserUseCase: GetResultsUserUseCase,
    val getDoneChallengesUseCase: GetDoneChallengesUseCase,
    val acceptFriendshipUseCase: AcceptFriendshipUseCase,
    val deleteDeclineFriendshipUseCase: DeleteDeclineFriendshipUseCase,

): ViewModel() {
    private val _selectedTabIndex = mutableStateOf(0)
    val selectedTabIndex: State<Int> = _selectedTabIndex

    private val _friendships = mutableStateOf(listOf<AcceptedFriendship>())
    val friendships: State<List<AcceptedFriendship>> = _friendships

    private val _pendingFriendship = mutableStateOf(listOf<PendingFriendship>())
    val pendingFriendship: State<List<PendingFriendship>> = _pendingFriendship

    private var _isLoading = mutableStateOf(false)
    val isLoading: Boolean get() = _isLoading.value

    private var _isModalSheetLoading = mutableStateOf(false)
    val isModalSheetLoading: Boolean get() = _isModalSheetLoading.value

    private var _userStats = mutableStateOf<UserStatsResponse?>(null)
    val userStats: State<UserStatsResponse?> = _userStats

    //TODO States & Abfrage für Punkte,Level,Score

    private val _userResults = mutableStateOf(listOf<com.example.quizit_android_app.model.retrofit.Result>())
    val userResults: State<List<com.example.quizit_android_app.model.retrofit.Result>> = _userResults

    private val _doneChallenges = mutableStateOf(listOf<DoneChallenges>())
    val doneChallenges: State<List<DoneChallenges>> = _doneChallenges

    private val _users = mutableStateOf(listOf<User?>())
    val users : State<List<User?>> = _users

    private val _filteredUsers = mutableStateOf(listOf<User?>())
    val filteredUsers : State<List<User?>> = _filteredUsers

    private val _searchText = mutableStateOf("")
    val searchText : State<String> = _searchText

    init {
        Log.d("SocialViewModel", "init: $savedStateHandle")
        val showStatistics: Boolean = savedStateHandle.toRoute<SocialRoute>()?.showStatistics ?: false

        if(showStatistics) {
            updateTabIndex(1)
        }
        setContent()
    }

    private fun setContent() {

        viewModelScope.launch {
            _isLoading.value = true
            try {
                if(_friendships.value.isEmpty()) {
                    _friendships.value = getAllFriendshipsUseCase()
                }

                if(_pendingFriendship.value.isEmpty()) {
                    _pendingFriendship.value = getPendingFriendshipsUseCase()
                }
                if(_userStats.value == null) {
                    _userStats.value = getUserStatsUseCase()
                }
                if(_userResults.value.isEmpty()) {
                    _userResults.value = getResultsUserUseCase()
                }
                if(_doneChallenges.value.isEmpty()) {
                    _doneChallenges.value = getDoneChallengesUseCase().doneChallenges
                }
            } catch (e: Exception) {
                //TODO Error handling
            } finally {
                _isLoading.value = false
            }
        }


    }

    fun updateTabIndex(index: Int) {
        _selectedTabIndex.value = index
    }

    fun updateIsModalSheetLoading() {
        _isModalSheetLoading.value = !_isModalSheetLoading.value
    }


    public fun setUsers() {
        viewModelScope.launch {
            _isModalSheetLoading.value = true
            try {

                if(_users.value.isEmpty()) {
                    val user = getUserUseCase()
                    _users.value = getAllUsersUseCase()

                    val pendingIds = _pendingFriendship.value.map { it.friend?.userId }
                    val acceptedIds = _friendships.value.map { it.friend?.userId }

                    _users.value = _users.value.filter {
                        it?.userId != user?.userId &&
                                it?.userId !in pendingIds &&  // Entfernt Nutzer aus _pendingFriendships
                                it?.userId !in acceptedIds

                    }


                    filterUsers()
                }
            } catch (e: Exception) {
                //TODO Error handling
            } finally {
                _isModalSheetLoading.value = false
            }
        }

    }

    private fun filterUsers() {
        val query = _searchText.value.trim().lowercase()
        _filteredUsers.value = if (query.isEmpty()) {
            _users.value
        } else {
            _users.value.filter { user ->
                user?.userName!!.lowercase().contains(query) ||
                user?.userFullname!!.lowercase().contains(query) ||
                user?.userMail!!.lowercase().contains(query)
            }
        }

    }

    public fun updateSearchText(text: String) {
        _searchText.value = text
        filterUsers()
    }

    public fun acceptFriendship(isAccept: Boolean, id: Int) {
        viewModelScope.launch {

            if(isAccept) {

                val friendshipResponse = acceptFriendshipUseCase(id)
                val friendship = friendshipResponse.friendship

                if(friendshipResponse.status == "Success") {
                    val acceptedFriendship = AcceptedFriendship(
                        friendshipId = friendship!!.friendshipId,
                        friendshipSince = friendship.friendshipSince,
                        friend = friendship.friend,

                        )
                    _friendships.value = _friendships.value + acceptedFriendship
                    _pendingFriendship.value = _pendingFriendship.value.filter { it.friendshipId != id }
                }

            } else {

                deleteDeclineFriendshipUseCase(id)
                _pendingFriendship.value = _pendingFriendship.value.filter { it.friendshipId != id }



            }

        }

    }
}
