package com.example.quizit_android_app.ui.quiz

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _currentQuestionIndex = mutableStateOf(0)
    val currentQuestionIndex: State<Int> = _currentQuestionIndex

    private val _selectedAnswers = mutableStateOf(listOf<Int>())
    val selectedAnswers: State<List<Int>> = _selectedAnswers

    private val _userAnswers = mutableStateOf(mutableMapOf<Int, List<Int>>())
    val userAnswers: State<Map<Int, List<Int>>> = _userAnswers


    private val _questions = mutableStateOf(listOf<Question>())
    val questions: State<List<Question>> = _questions

    private val _focus = mutableStateOf("")
    val focus: State<String> = _focus




    init {
        val focusId: Int = savedStateHandle.get<String>("focusId")?.toIntOrNull() ?: 0
        setQuestions(focusId)
        setFocusName(focusId)
    }


    private fun setQuestions(id: Int) {
        _questions.value = listOf(
            Question(
                questionId = 1,
                questionText = "Welche Ereignisse führten zum Ausbruch des Zweiten Weltkriegs im Jahr 1939?",
                options = listOf(
                    Option(1, "Der Angriff auf Pearl Harbor durch Japan", false),
                    Option(2, "Der Vertrag von Versailles", true),
                    Option(3, "Der Einmarsch Deutschlands in Polen", true),
                    Option(4, "Der Austritt Großbritanniens aus dem Völkerbund", false)
                ),
                mChoice = true
            ),
            Question(
                questionId = 2,
                questionText = "Wann begann der Zweite Weltkrieg?",
                options = listOf(
                    Option(5, "1914", false),
                    Option(6, "1939", true),
                    Option(7, "1941", false),
                    Option(8, "1945", false)
                ),
                mChoice = false
            ),
            Question(
                questionId = 3,
                questionText = "Wer war der Führer von Nazi-Deutschland während des Zweiten Weltkriegs?",
                options = listOf(
                    Option(9, "Adolf Hitler", true),
                    Option(10, "Joseph Stalin", false),
                    Option(11, "Winston Churchill", false),
                    Option(12, "Franklin D. Roosevelt", false)
                ),
                mChoice = false
            ),
            Question(
                questionId = 4,
                questionText = "Was war das Hauptziel des Marshallplans?",
                options = listOf(
                    Option(13, "Die Unterstützung von Deutschland", false),
                    Option(14, "Die Förderung der wirtschaftlichen Erholung Europas nach dem Zweiten Weltkrieg", true),
                    Option(15, "Die Gründung der NATO", false),
                    Option(16, "Die Schaffung des europäischen Binnenmarkts", false)
                ),
                mChoice = false
            ),
            Question(
                questionId = 5,
                questionText = "Welche Nation trat zuletzt dem Zweiten Weltkrieg bei?",
                options = listOf(
                    Option(17, "Japan", false),
                    Option(18, "Italien", false),
                    Option(19, "Die Vereinigten Staaten", true),
                    Option(20, "Die Sowjetunion", false)
                ),
                mChoice = false
            )
        )

    }

    private fun setFocusName(id: Int) {
        _focus.value = "2. Weltkrieg, ID: "+id
    }

    fun toggleAnswer(optionId: Int) {
       val question = questions.value[currentQuestionIndex.value]

        if (question.mChoice) {
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
        _userAnswers.value[currentQuestionId] = _selectedAnswers.value
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
                if (option.optionCorrect) {
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
            val correctAnswers = question.options.filter { it.optionCorrect }.map { it.optionId }
            ResultItem(
                question = question,
                userAnswer = userAnswer,
                correctAnswers = correctAnswers,
                isCorrect = userAnswer.containsAll(correctAnswers) && correctAnswers.containsAll(userAnswer)
            )
        }
    }




}

data class Question(
    val questionId: Int,
    val questionText: String,
    val options: List<Option>,
    val mChoice: Boolean
)

data class Option(
    val optionId: Int,
    val optionText: String,
    val optionCorrect: Boolean
)

data class ResultItem(
    val question: Question,
    val userAnswer: List<Int>,
    val correctAnswers: List<Int>,
    val isCorrect: Boolean
)

