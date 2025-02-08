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
import com.example.quizit_android_app.model.AcceptedFriendships
import com.example.quizit_android_app.model.Friendship
import com.example.quizit_android_app.model.PendingFriendships
import com.example.quizit_android_app.model.User
import com.example.quizit_android_app.model.UserStatsResponse
import com.example.quizit_android_app.navigation.SocialRoute
import com.example.quizit_android_app.usecases.friendship.GetAcceptedFriendships
import com.example.quizit_android_app.usecases.friendship.GetAllFriendshipsUseCase
import com.example.quizit_android_app.usecases.friendship.GetPendingFriendshipsUseCase
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
    val getAllFriendshipsUseCase: GetAcceptedFriendships,
    val getPendingFriendshipsUseCase: GetPendingFriendshipsUseCase,
    val getAllUsersUseCase: GetAllUsersUseCase,
    val getUserStatsUseCase: GetUserStatsUseCase,
    val getUserUseCase: GetUserUseCase,
): ViewModel() {
    private val _selectedTabIndex = mutableStateOf(0)
    val selectedTabIndex: State<Int> = _selectedTabIndex

    private val _friendships = mutableStateOf(listOf<AcceptedFriendships>())
    val friendships: State<List<AcceptedFriendships>> = _friendships

    private val _pendingFriendships = mutableStateOf(listOf<PendingFriendships>())
    val pendingFriendships: State<List<PendingFriendships>> = _pendingFriendships

    private var _isLoading = mutableStateOf(false)
    val isLoading: Boolean get() = _isLoading.value

    private var _isModalSheetLoading = mutableStateOf(false)
    val isModalSheetLoading: Boolean get() = _isModalSheetLoading.value

    private var _userStats = mutableStateOf<UserStatsResponse?>(null)
    val userStats: State<UserStatsResponse?> = _userStats

    //TODO States & Abfrage für Punkte,Level,Score

    private val _userResults = mutableStateOf(listOf<Result>())
    val userResults: State<List<Result>> = _userResults

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
        setUserResults()
    }

    private fun setContent() {

        viewModelScope.launch {
            _isLoading.value = true
            try {
                if(_friendships.value.isEmpty()) {
                    _friendships.value = getAllFriendshipsUseCase()
                }

                if(_pendingFriendships.value.isEmpty()) {
                    _pendingFriendships.value = getPendingFriendshipsUseCase()
                }
                if(_userStats.value == null) {
                    _userStats.value = getUserStatsUseCase()
                }
            } catch (e: Exception) {
                //TODO Error handling
            } finally {
                _isLoading.value = false
            }
        }


    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUserResults() {
        _userResults.value = listOf(
            Result(1, 0.85f, 1, 101, "01.01.2022"),
            Result(2, 0.90f, 2, 102, "02.01.2022"),
            Result(3, 0.78f, 3, 103, "03.01.2022"),
            Result(4, 0.92f, 4, 104, "04.01.2022"),
            Result(5, 0.88f, 5, 105, "05.01.2022")
        ).sortedByDescending { result ->
            // Datum in ein LocalDate umwandeln, um es korrekt zu vergleichen
            LocalDate.parse(result.resultDate, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
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
                    _users.value = _users.value.filter { it?.userId != user?.userId }
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
}

//Helper classes


data class Result(
    val resultId: Int,
    val resultScore: Float,
    val userId: Int,
    val focusId: Int,
    val resultDate: String
)

