package com.example.quizit_android_app.ui

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizit_android_app.usecases.IsUserLoggedInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase

): ViewModel() {

    private var _isLoggedIn = mutableStateOf(false)
    val isLoggedIn: State<Boolean> = _isLoggedIn

    init {
        checkLogin()
    }

    private fun checkLogin() {
        viewModelScope.launch {
            _isLoggedIn.value = isUserLoggedInUseCase()
        }
    }

    fun setLoggedIn(value: Boolean) {
        _isLoggedIn.value = value
    }

}