package com.example.quizit_android_app.ui.play_quiz.focus

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizit_android_app.model.retrofit.Focus
import com.example.quizit_android_app.model.retrofit.Subject
import com.example.quizit_android_app.navigation.FocusRoute
import com.example.quizit_android_app.usecases.focus.GetFocusForSubjectUseCase
import com.example.quizit_android_app.usecases.localdata.focus.SyncLocalFocusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FocusViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getFocusForSubjectUseCase: GetFocusForSubjectUseCase,
    private val syncLocalFocusUseCase: SyncLocalFocusUseCase
): ViewModel() {

    private var _focusList by mutableStateOf(listOf<Focus>())
    val focusList: List<Focus> get() = _focusList

    private var _subject = mutableStateOf<Subject?>(null)
    val subject: Subject? get() = _subject.value

    private var _overallQuestionCount = mutableStateOf<Int>(0)
    val overallQuestionCount: Int get() = _overallQuestionCount.value

    private var _isLoading = mutableStateOf(false)
    val isLoading: Boolean get() = _isLoading.value

    init {
        _subject.value = FocusRoute.from(savedStateHandle).subject
        Log.d("FocusViewModel", "Subject: "+_subject.value)

        setFocusList(_subject.value!!.subjectId)
    }

    private fun setFocusList(id: Int) {

        viewModelScope.launch {
            _isLoading.value = true
            try {
                if(_focusList.isEmpty()) {
                    _focusList = getFocusForSubjectUseCase(id)
                    Log.d("FocusViewModel", "FocusList: "+_focusList)
                }
                _overallQuestionCount.value = getQuestionCount()

            } catch (e: Exception) {
                Log.e("FocusViewModel", "Error fetching focus: "+e)
            } finally {
                _isLoading.value = false
            }

        }
    }

    private fun getQuestionCount(): Int {
        var count = 0
        _focusList.forEach { focus ->
            count = count + focus.questionCount!!
        }

        Log.d("",count.toString())
        return count
    }

    fun refreshData(onComplete: () -> Unit) {

        viewModelScope.launch {
            try {
                syncLocalFocusUseCase()

                _focusList = getFocusForSubjectUseCase(_subject.value!!.subjectId)
                _overallQuestionCount.value = getQuestionCount()
            } catch (e: Exception) {
                Log.e("FocusViewModel", "Error fetching focus: "+e)
            } finally {
                onComplete()
            }
        }

    }


}







