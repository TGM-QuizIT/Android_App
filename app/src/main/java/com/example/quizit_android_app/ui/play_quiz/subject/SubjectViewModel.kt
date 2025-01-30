package com.example.quizit_android_app.ui.play_quiz.subject

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizit_android_app.model.Subject
import com.example.quizit_android_app.usecases.subjects.GetAllSubjectsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubjectViewModel @Inject constructor(
    private val getAllSubjectsUseCase: GetAllSubjectsUseCase,
    private val savedStateHandle: SavedStateHandle,

    ): ViewModel() {
    private var _subjectList = mutableStateOf(listOf<Subject>())
    val subjectList: List<Subject> get() = _subjectList.value


    init {
        setSubjects()
    }

    private fun setSubjects() {
        viewModelScope.launch {
            _subjectList.value = getAllSubjectsUseCase()
            Log.d("SubjectViewModel", "setSubjects: ${_subjectList.value}")
        }
    }

}