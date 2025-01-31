package com.example.quizit_android_app.ui.play_quiz.quiz

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.quizit_android_app.model.Subject
import com.example.quizit_android_app.ui.social.Friendship
import com.example.quizit_android_app.ui.social.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuizDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private var _results by mutableStateOf(listOf<Result>())
    val results: List<Result> get() = _results

    private var _challenges by mutableStateOf(listOf<Challenge>())
    val challenges: List<Challenge> get() = _challenges

    init {

        setResults()
        setChallenges()
    }

    private fun setResults() {

    }

    private fun setChallenges() {

    }


}

data class Challenge(
    val challengeId: Int,
    val friendship: Friendship,
    val focusId: Int,
    val subject: Subject,
    val challengeDate: String,
    val result1: Int,
    val result2: Int,
)