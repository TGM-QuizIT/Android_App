package com.example.quizit_android_app.ui.play_quiz.quiz

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.toRoute
import com.example.quizit_android_app.model.retrofit.DoneChallenges
import com.example.quizit_android_app.model.retrofit.Focus
import com.example.quizit_android_app.model.retrofit.OpenChallenges
import com.example.quizit_android_app.model.retrofit.Result
import com.example.quizit_android_app.model.retrofit.Subject
import com.example.quizit_android_app.navigation.QuizDetailRoute
import com.example.quizit_android_app.usecases.challenge.DeleteChallengeUseCase
import com.example.quizit_android_app.usecases.challenge.GetChallengesForSubjectUseCase
import com.example.quizit_android_app.usecases.quiz.GetQuizOfSubjectUseCase
import com.example.quizit_android_app.usecases.result.GetResultsFocusUseCase
import com.example.quizit_android_app.usecases.result.GetResultsSubjectUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    val getChallengesForSubjectUseCase: GetChallengesForSubjectUseCase,
    val getResultsSubjectUseCase: GetResultsSubjectUseCase,
    val getResultsFocusUseCase: GetResultsFocusUseCase,
    val deleteChallengeUseCase: DeleteChallengeUseCase
): ViewModel() {

    private var _subject by mutableStateOf<Subject?>(null)
    val subject: Subject? get() = _subject

    private var _focus by mutableStateOf<Focus?>(null)
    val focus: Focus? get() = _focus

    private var _isLoading by mutableStateOf<Boolean>(false)
    val isLoading: Boolean get() = _isLoading

    private var _openChallenges by mutableStateOf(listOf<OpenChallenges>())
    val openChallenges: List<OpenChallenges> get() = _openChallenges

    private var _doneChallenges by mutableStateOf(listOf<DoneChallenges>())
    val doneChallenges: List<DoneChallenges> get() = _doneChallenges

    private var _results by mutableStateOf(listOf<Result>())
    val results: List<Result> get() = _results





    init {

        val subject = QuizDetailRoute.from(savedStateHandle).subject
        val focus = QuizDetailRoute.from(savedStateHandle).focus

        setContent(subject, focus)

    }

    fun declineChallenge(id: Int) {
        viewModelScope.launch {
            try {
                _openChallenges = _openChallenges.filter { it.challengeId != id }
                deleteChallengeUseCase(id)

            } catch(e: Exception) {
                Log.d("QuizDetailViewModel", "Error declining challenge: " + e)
            }

        }
    }

    private fun setContent(subject: Subject?, focus: Focus?) {

        _subject = subject
        _focus = focus

        viewModelScope.launch {
            _isLoading = true

            try {

                if (_subject != null) {
                    val challenges =
                        getChallengesForSubjectUseCase(subjectId = subject?.subjectId!!)
                    _openChallenges = challenges.openChallenges
                    _doneChallenges = challenges.doneChallenges

                    _results = getResultsSubjectUseCase(subject.subjectId)
                } else {
                    val challenges = getChallengesForSubjectUseCase(focusId = focus?.focusId!!)
                    _openChallenges = challenges.openChallenges
                    _doneChallenges = challenges.doneChallenges

                    _results = getResultsFocusUseCase(focus.focusId!!)

                }


            } catch (e: Exception) {
                Log.d("QuizDetailViewModel", "Error fetching quiz: " + e)

            } finally {
                _isLoading = false
            }
        }

    }



}

