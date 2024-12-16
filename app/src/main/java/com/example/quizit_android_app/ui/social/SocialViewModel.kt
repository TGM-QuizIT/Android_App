package com.example.quizit_android_app.ui.social

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
class SocialViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _selectedTabIndex = mutableStateOf(0)
    val selectedTabIndex: State<Int> = _selectedTabIndex

    private val _friendships = mutableStateOf(listOf<Friendship>())
    val friendships: State<List<Friendship>> = _friendships

    private val _pendingFriendships = mutableStateOf(listOf<PendingFriendship>())
    val pendingFriendships: State<List<PendingFriendship>> = _pendingFriendships

    //TODO States & Abfrage für Punkte,Level,Score

    private val _userResults = mutableStateOf(listOf<Result>())
    val userResults: State<List<Result>> = _userResults

    private val _users = mutableStateOf(listOf<User>())
    val users : State<List<User>> = _users

    private val _filteredUsers = mutableStateOf(listOf<User>())
    val filteredUsers : State<List<User>> = _filteredUsers

    private val _searchText = mutableStateOf("")
    val searchText : State<String> = _searchText

    init {
        val showStatistics: Boolean = savedStateHandle.get<Boolean>("showStatistics") ?: false

        if(showStatistics) {
            updateTabIndex(1)
        }
        setFriendships()
        setPendingFriendships()
        setUserResults()
    }

    private fun setFriendships() {
        _friendships.value = listOf(
            Friendship(1, 1, "Max Mustermann", 1999, "01.01.2021"),
            Friendship(2, 2, "Erika Mustermann", 1999, "01.01.2021"),
            Friendship(3, 3, "Hans Mustermann", 1999, "01.01.2021"),
            Friendship(4, 4, "Lisa Mustermann", 1999, "01.01.2021"),
            Friendship(5, 5, "Peter Mustermann", 1999, "01.01.2021"),
        )
    }

    private fun setPendingFriendships() {
        _pendingFriendships.value = listOf(
            PendingFriendship(1, 1, "Max Mustermann", 1999),
            PendingFriendship(2, 2, "Erika Mustermann", 1999),
            PendingFriendship(3, 3, "Hans Mustermann", 1999),
        )
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


    public fun setUsers() {
        _users.value = listOf(
            User(1, "jdoe", "John Doe", 1, "1AHIT", "schueler", "jdoe@student.tgm.ac.at"),
            User(2, "asmith", "Anna Smith", 2, "2BHIT", "schueler", "asmith@student.tgm.ac.at"),
            User(3, "mbrown", "Michael Brown", 3, "3CHIT", "schueler", "mbrown@student.tgm.ac.at"),
            User(4, "jwhite", "Jessica White", 4, "4DHIT", "schueler", "jwhite@student.tgm.ac.at"),
            User(5, "pdavis", "Paul Davis", 1, "1AHIT", "schueler", "pdavis@student.tgm.ac.at"),
            User(6, "lwilson", "Laura Wilson", 2, "2BHIT", "schueler", "lwilson@student.tgm.ac.at"),
            User(7, "cmiller", "Chris Miller", 3, "3CHIT", "schueler", "cmiller@student.tgm.ac.at"),
            User(8, "akelly", "Alex Kelly", 4, "4DHIT", "schueler", "akelly@student.tgm.ac.at"),
            User(9, "bturner", "Barbara Turner", 1, "1AHIT", "schueler", "bturner@student.tgm.ac.at"),
            User(10, "nroberts", "Nathan Roberts", 2, "2BHIT", "schueler", "nroberts@student.tgm.ac.at")
        )
        filterUsers()
    }

    private fun filterUsers() {
        val query = _searchText.value.trim().lowercase()
        _filteredUsers.value = if (query.isEmpty()) {
            _users.value
        } else {
            _users.value.filter { user ->
                user.userName.lowercase().contains(query) ||
                user.userFullname.lowercase().contains(query) ||
                user.userMail.lowercase().contains(query)
            }
        }

    }

    public fun updateSearchText(text: String) {
        _searchText.value = text
        filterUsers()
    }
}

//Helper classes
data class Friendship(
    val friendshipId: Int,
    val friendId: Int,
    val friendName: String,
    val friendYear: Int,
    val friendshipSince: String
)

data class PendingFriendship(
    val friendshipId: Int,
    val friendId: Int,
    val friendName: String,
    val friendYear: Int
)

data class Result(
    val resultId: Int,
    val resultScore: Float,
    val userId: Int,
    val focusId: Int,
    val resultDate: String
)

data class User(
    val userId: Int,
    val userName: String,
    val userFullname: String,
    val userYear: Int,
    val userClass: String,
    val userType: String,
    val userMail: String
)
