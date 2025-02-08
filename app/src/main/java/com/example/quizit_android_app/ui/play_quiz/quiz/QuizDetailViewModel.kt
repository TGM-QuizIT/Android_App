package com.example.quizit_android_app.ui.play_quiz.quiz

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.quizit_android_app.model.Subject
import com.example.quizit_android_app.ui.social.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuizDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private var _results by mutableStateOf(listOf<Result>())
    val results: List<Result> get() = _results



    init {


    }

    private fun setContent() {

    }

    private fun setResults() {

    }

    private fun setChallenges(id: Int) {


    }


}

