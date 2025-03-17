package com.example.quizit_android_app.ui.quiz

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quizit_android_app.model.retrofit.AcceptedFriendship
import com.example.quizit_android_app.model.retrofit.Focus
import com.example.quizit_android_app.model.retrofit.OpenChallenges
import com.example.quizit_android_app.model.retrofit.Options
import com.example.quizit_android_app.model.retrofit.Questions
import com.example.quizit_android_app.model.retrofit.Result
import com.example.quizit_android_app.model.retrofit.Subject
import com.example.quizit_android_app.navigation.QuizRoute
import com.example.quizit_android_app.usecases.challenge.AddChallengeForFocusUseCase
import com.example.quizit_android_app.usecases.challenge.AddChallengeForSubjectUseCase
import com.example.quizit_android_app.usecases.challenge.AssignResultToChallengeUseCase
import com.example.quizit_android_app.usecases.friendship.GetAcceptedFriendshipsUseCase
import com.example.quizit_android_app.usecases.quiz.GetQuizOfFocusUseCase
import com.example.quizit_android_app.usecases.quiz.GetQuizOfSubjectUseCase
import com.example.quizit_android_app.usecases.result.AddResultFocusUseCase
import com.example.quizit_android_app.usecases.result.AddResultSubjectUseCase
import com.example.quizit_android_app.usecases.subjects.GetSubjectForFocusIDUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getQuizOfSubjectUseCase: GetQuizOfSubjectUseCase,
    private val getQuizOfFocusUseCase: GetQuizOfFocusUseCase,
    private val getAcceptedFriendshipsUseCase: GetAcceptedFriendshipsUseCase,
    private val addResultSubjectUseCase: AddResultSubjectUseCase,
    private val addResultFocusUseCase: AddResultFocusUseCase,
    private val assignResultToChallengeUseCase: AssignResultToChallengeUseCase,
    private val addChallengeForSubjectUseCase: AddChallengeForSubjectUseCase,
    private val addChallengeForFocusUseCase: AddChallengeForFocusUseCase,
    private val getSubjectForFocusIDUseCase: GetSubjectForFocusIDUseCase
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

    private val _isBottomSheetLoading = mutableStateOf(false)
    val isBottomSheetLoading: State<Boolean> = _isBottomSheetLoading

    private val _friendships = mutableStateOf(listOf<AcceptedFriendship>())
    val friendships: State<List<AcceptedFriendship>> = _friendships

    private val _isFriendshipsLoaded = mutableStateOf(false)
    val isFriendshipsLoaded: State<Boolean> = _isFriendshipsLoaded

    private val _challenge = mutableStateOf<OpenChallenges?>(null)
    val challenge: State<OpenChallenges?> = _challenge

    private val _result = mutableStateOf<com.example.quizit_android_app.model.retrofit.Result?>(null)
    val result: State<Result?> = _result






    init {
        _challenge.value = QuizRoute.from(savedStateHandle).challenge
        Log.d("QuizViewModel", "Challenge: ${_challenge.value}")

        if(_challenge.value == null) {
            _focus.value = QuizRoute.from(savedStateHandle).focus
            _subject.value = QuizRoute.from(savedStateHandle).subject

            if(_subject.value == null) {
                viewModelScope.launch {
                    _subject.value = getSubjectForFocusIDUseCase(_focus.value?.focusId!!)
                }
            }

            if(_focus.value != null) {
                setQuestions(_focus.value!!.focusId, false)
            }
            else {
                setQuestions(_subject.value!!.subjectId, true)
            }
        }
        else {
            if(_challenge.value?.focus != null) {
                _focus.value = _challenge.value!!.focus

                viewModelScope.launch {
                    _subject.value = getSubjectForFocusIDUseCase(_focus.value?.focusId!!)
                }

                setQuestions(_focus.value!!.focusId, false)
                Log.d("QuizViewModel", "Focus: ${_focus.value}")
            }
            else {
                _subject.value = _challenge?.value?.subject
                setQuestions(_subject.value!!.subjectId, true)
                Log.d("QuizViewModel", "Subject: ${_subject.value}")
            }
        }
    }


    private fun setQuestions(id: Int?, isQuizOfSubject: Boolean) {
        Log.d("QuizViewModel", "Setting questions for $id with isQuizOfSubject: $isQuizOfSubject")
        viewModelScope.launch {
            _isLoading.value = true // Ladezustand aktivieren
            try {


                _questions.value = if (isQuizOfSubject) {
                    getQuizOfSubjectUseCase(id!!)
                } else {
                    getQuizOfFocusUseCase(id!!)
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

        if(currentQuestionIndex.value >= questions.value.size) {
            setResult()
        }
    }

    fun calculateScore(): Float {
        val totalQuestions = questions.value.size
        if (totalQuestions == 0) return 0f


        var score = 0f
        val maxScore = questions.value.sumOf { it.options.size } * 0.25f


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

    fun getFriendships() {
        viewModelScope.launch {
            _isBottomSheetLoading.value = true
            try {
                if(!_isFriendshipsLoaded.value) {
                    _friendships.value = getAcceptedFriendshipsUseCase()
                    _isFriendshipsLoaded.value = true
                }


            } catch (e: Exception) {
                Log.e("QuizViewModel", "Error loading friendships", e)
            } finally {
                _isBottomSheetLoading.value = false
            }
        }

    }

    fun resetQuiz() {
        _currentQuestionIndex.value = 0
        _selectedAnswers.value = emptyList()
        _userAnswers.value = mutableMapOf()

    }

    fun challengeFriend(friendshipId: Int, focus: Focus?, subject: Subject?) {
        viewModelScope.launch {
            _isBottomSheetLoading.value = true
            try {
                if(focus == null) {

                    _friendships.value = _friendships.value.filter { it.friendshipId != friendshipId }
                    val response = addChallengeForSubjectUseCase(friendshipId, subject!!.subjectId)

                    if(response.status == "Success") {
                        assignResultToChallengeUseCase(
                            challengeId = response.challenge?.challengeId!!,
                            result = _result.value?.resultId!!
                        )
                    }
                }
                else {
                    _friendships.value = _friendships.value.filter { it.friendshipId != friendshipId }
                    val response = addChallengeForFocusUseCase(friendshipId, focus.focusId!!)

                    if(response.status == "Success") {
                        assignResultToChallengeUseCase(
                            challengeId = response.challenge?.challengeId!!,
                            result = _result.value?.resultId!!
                        )
                    }

                }

            } catch (e: Exception) {
                Log.e("QuizViewModel", "Error challenging friend", e)
            } finally {
                _isBottomSheetLoading.value = false
            }
        }


    }

    fun setResult() {

        val score = calculateScore()*100

        viewModelScope.launch {
            _isLoading.value = true
            if (_challenge.value == null) {
                try {
                    if (_focus.value == null) {
                        _result.value =  addResultSubjectUseCase(_subject.value!!.subjectId, score.toDouble()).result

                    } else {
                        _result.value = addResultFocusUseCase(_focus.value!!.focusId!!, score.toDouble()).result
                    }
                } catch (e: Exception) {
                    Log.e("QuizViewModel", "Error adding result", e)
                } finally {
                    _isLoading.value = false
                }


            } else {
                try {
                    if (_focus.value == null) {
                        val response =
                            addResultSubjectUseCase(_subject.value!!.subjectId, score.toDouble())

                        _result.value = response.result

                        if (response.status == "Success") {
                            assignResultToChallengeUseCase(
                                challengeId = _challenge.value!!.challengeId!!,
                                result = response.result?.resultId!!
                            )
                        }

                    } else {
                        val response =
                            addResultFocusUseCase(_focus.value!!.focusId!!, score.toDouble())

                        _result.value = response.result

                        if (response.status == "Success") {
                            assignResultToChallengeUseCase(
                                challengeId = _challenge.value!!.challengeId!!,
                                result = response.result?.resultId!!
                            )

                        }

                    }
                } catch (e: Exception) {
                    Log.e("QuizViewModel", "Error assigning result to challenge", e)
                } finally {
                    _isLoading.value = false

                }
            }

        }
    }
}


data class ResultItem(
    val question: Questions,
    val userAnswer: List<Int>,
    val correctAnswers: List<Int?>,
    val isCorrect: Boolean
)

