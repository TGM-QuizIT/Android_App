package com.example.quizit_android_app.ui.quiz

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizit_android_app.model.retrofit.Focus
import com.example.quizit_android_app.model.retrofit.Questions
import com.example.quizit_android_app.model.retrofit.Subject
import com.example.quizit_android_app.navigation.QuizRoute
import com.example.quizit_android_app.usecases.quiz.GetQuizOfFocusUseCase
import com.example.quizit_android_app.usecases.quiz.GetQuizOfSubjectUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getQuizOfSubjectUseCase: GetQuizOfSubjectUseCase,
    private val getQuizOfFocusUseCase: GetQuizOfFocusUseCase
): ViewModel() {
    private val _currentQuestionIndex = mutableStateOf(0)
    val currentQuestionIndex: State<Int> = _currentQuestionIndex

    private val _selectedAnswers = mutableStateOf(listOf<Int>())
    val selectedAnswers: State<List<Int>> = _selectedAnswers

    private val _userAnswers = mutableStateOf(mutableMapOf<Int, List<Int>>())
    val userAnswers: State<Map<Int, List<Int>>> = _userAnswers


    private val _questions = mutableStateOf(listOf<Questions>())
    val questions: State<List<Questions>> = _questions

    private val _focus = mutableStateOf<Focus?>(null)
    val focus: State<Focus?> = _focus

    private val _subject = mutableStateOf<Subject?>(null)
    val subject: State<Subject?> = _subject

    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading






    init {
        _focus.value = QuizRoute.from(savedStateHandle).focus
        _subject.value = QuizRoute.from(savedStateHandle).subject


        val id = _focus.value?.focusId ?: _subject.value?.subjectId
        val isQuizOfSubject = _focus.value == null


        setQuestions(id, isQuizOfSubject)
    }


    private fun setQuestions(id: Int?, isQuizOfSubject: Boolean) {
        viewModelScope.launch {
            _isLoading.value = true // Ladezustand aktivieren
            try {

                if(_questions.value.isEmpty()) {
                    _questions.value = if (isQuizOfSubject) {
                        getQuizOfSubjectUseCase(id!!)
                    } else {
                        getQuizOfFocusUseCase(id!!)
                    }

                }

            } catch (e: Exception) {
                Log.e("QuizViewModel", "Error loading questions", e)
            } finally {
                _isLoading.value = false // Ladezustand deaktivieren
            }
        }
    }


    fun toggleAnswer(optionId: Int) {
       val question = questions.value[currentQuestionIndex.value]

        if (question.mChoice == true) {
             if (_selectedAnswers.value.contains(optionId)) {
                 _selectedAnswers.value = _selectedAnswers.value - optionId
            } else {
                _selectedAnswers.value = _selectedAnswers.value + optionId

            }
        } else {
            _selectedAnswers.value = if (_selectedAnswers.value.contains(optionId)) {
                emptyList()
            } else {
                listOf(optionId)
            }
        }
    }

    fun nextQuestion() {

        val currentQuestionId = questions.value[currentQuestionIndex.value].questionId
        _userAnswers.value[currentQuestionId!!] = _selectedAnswers.value
        _currentQuestionIndex.value += 1
        _selectedAnswers.value = emptyList()

        calculateScore()
    }

    fun calculateScore(): Float {
        val totalQuestions = questions.value.size
        if (totalQuestions == 0) return 0f


        var score = 0f
        val maxScore = questions.value.sumOf { it.options.size } * 0.25f
        Log.d("", "Max Score: $maxScore")

        questions.value.forEach { question ->
            val userAnswer = userAnswers.value[question.questionId] ?: emptyList()

            if (userAnswer.isEmpty()) {
                return@forEach
            }

            question.options.forEach { option ->

                Log.d("Score", "Score: $score")
                if (option.optionCorrect == true) {
                    score += if (userAnswer.contains(option.optionId)) {
                        0.25f
                    } else {
                        -0.25f
                    }
                } else {
                    score += if (userAnswer.contains(option.optionId)) {
                        -0.25f
                    } else {
                        0.25f
                    }
                }


            }
        }


        // Score als Prozentsatz (0 bis 1)
        //return (score / maxScore).coerceIn(0f, 1f)
        return score/totalQuestions;
    }


    fun getResult(): List<ResultItem> {
        return questions.value.map { question ->
            val userAnswer = userAnswers.value[question.questionId] ?: emptyList()
            val correctAnswers = question.options.filter { it.optionCorrect!! }.map { it.optionId }
            ResultItem(
                question = question,
                userAnswer = userAnswer,
                correctAnswers = correctAnswers,
                isCorrect = userAnswer.containsAll(correctAnswers) && correctAnswers.containsAll(userAnswer)
            )
        }
    }




}


data class ResultItem(
    val question: Questions,
    val userAnswer: List<Int>,
    val correctAnswers: List<Int?>,
    val isCorrect: Boolean
)

