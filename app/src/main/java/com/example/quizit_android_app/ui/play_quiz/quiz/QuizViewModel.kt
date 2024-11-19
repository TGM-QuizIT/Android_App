package com.example.quizit_android_app.ui.quiz

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel

class QuizViewModel: ViewModel() {
    private val _currentQuestionIndex = mutableStateOf(0)
    val currentQuestionIndex: State<Int> = _currentQuestionIndex

    private val _selectedAnswers = mutableStateOf(listOf<Int>())
    val selectedAnswers: State<List<Int>> = _selectedAnswers



    private val _questions = mutableStateOf(listOf<Question>())
    val questions: State<List<Question>> = _questions

    private val _focus = mutableStateOf("")
    val focus: State<String> = _focus

    private val _showSelectionError = mutableStateOf(false)
    val showSelectionError: State<Boolean> = _showSelectionError


    init {
        setQuestions()
        setFocusName(0)
    }


    private fun setQuestions() {
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
        _focus.value = "2. Weltkrieg"
    }

    fun toggleAnswer(optionId: Int) {
       val question = questions.value[currentQuestionIndex.value]

        if (question.mChoice) {
             if (_selectedAnswers.value.contains(optionId)) {
                 _selectedAnswers.value = _selectedAnswers.value - optionId
            } else {
                _selectedAnswers.value = _selectedAnswers.value + optionId
                 _showSelectionError.value = false
            }
        } else {
            _selectedAnswers.value = if (_selectedAnswers.value.contains(optionId)) {
                emptyList()
            } else {
                _showSelectionError.value = false
                listOf(optionId)
            }
        }
    }

    fun nextQuestion() {

        if(_selectedAnswers.value.isEmpty()) {
            _showSelectionError.value = true
        }
        else {
            _showSelectionError.value = false
            _currentQuestionIndex.value += 1
            _selectedAnswers.value = emptyList()
        }

    }

    fun calculateScore(): Int {
        var correctAnswers = 0
        questions.value.forEachIndexed { index, question ->
            val userAnswer = selectedAnswers.value.contains(question.options.find { it.optionCorrect }?.optionId)
            if (userAnswer) {
                correctAnswers++
            }
        }
        return correctAnswers
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