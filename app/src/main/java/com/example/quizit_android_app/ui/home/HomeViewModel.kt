package com.example.quizit_android_app.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizit_android_app.model.Subject
import com.example.quizit_android_app.usecases.Subjects.GetAllSubjectsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getAllSubjectsUseCase: GetAllSubjectsUseCase,
): ViewModel() {

    private var _subjectList by mutableStateOf(listOf<Subject>())
    val subjectList: List<Subject> get() = _subjectList

    private var _isLoading by mutableStateOf(false)
    val isLoading: Boolean get() = _isLoading

    init {
        setSubjects()
    }



    private fun setSubjects() {
        viewModelScope.launch {
            _isLoading = true
            try {
                _subjectList = getAllSubjectsUseCase()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading = false
            }
        }
    }
}