package com.example.quizit_android_app.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizit_android_app.usecases.user.IsUserLoggedInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase

): ViewModel() {

    private var _isLoggedIn = mutableStateOf(true)
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