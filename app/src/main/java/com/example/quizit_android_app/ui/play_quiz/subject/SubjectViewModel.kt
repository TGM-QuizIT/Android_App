package com.example.quizit_android_app.ui.play_quiz.subject

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizit_android_app.model.retrofit.Subject
import com.example.quizit_android_app.usecases.localdata.subjects.SyncLocalSubjectsUseCase
import com.example.quizit_android_app.usecases.subjects.GetAllSubjectsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubjectViewModel @Inject constructor(
    private val getAllSubjectsUseCase: GetAllSubjectsUseCase,
    private val syncLocalSubjectsUseCase: SyncLocalSubjectsUseCase,
    private val savedStateHandle: SavedStateHandle,

    ): ViewModel() {
    private var _subjectList = mutableStateOf(listOf<Subject>())
    val subjectList: List<Subject> get() = _subjectList.value

    private var _isLoading = mutableStateOf(false)
    val isLoading: Boolean get() = _isLoading.value


    init {
        setSubjects()
    }

    private fun setSubjects() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                if(_subjectList.value.isEmpty()) {
                    _subjectList.value = getAllSubjectsUseCase()
                }

                Log.d("SubjectViewModel", "setSubjects: ${_subjectList.value}")
            } catch (e: Exception) {
                Log.e("SubjectViewModel", "Error fetching subjects", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refreshData(onComplete: () -> Unit) {

        viewModelScope.launch {
            try {
                syncLocalSubjectsUseCase()

                _subjectList.value = getAllSubjectsUseCase()
            } catch (e: Exception) {
                Log.e("SubjectViewModel", "Error fetching subjects", e)
            } finally {
                onComplete()
            }
        }

    }

}