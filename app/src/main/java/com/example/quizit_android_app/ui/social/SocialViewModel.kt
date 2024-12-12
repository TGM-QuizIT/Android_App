package com.example.quizit_android_app.ui.social

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
class SocialViewModel @Inject constructor(

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

    init {
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