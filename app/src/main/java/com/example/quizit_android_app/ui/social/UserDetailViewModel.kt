package com.example.quizit_android_app.ui.social

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.example.quizit_android_app.navigation.UserDetailRoute
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
class UserDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    // TODO Add States für User

    private val _userResults = mutableStateOf(listOf<Result>())
    val userResults: State<List<Result>> = _userResults

    init {
        val userId: Int = savedStateHandle.toRoute<UserDetailRoute>().id
        setUserResults()
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

}

