package com.example.quizit_android_app.ui

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizit_android_app.usecases.user.IsUserBlockedUseCase
import com.example.quizit_android_app.usecases.user.IsUserLoggedInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase,
    private val isUserBlockedUseCase: IsUserBlockedUseCase


): ViewModel() {

    private var _isLoggedIn = mutableStateOf(true)
    val isLoggedIn: State<Boolean> = _isLoggedIn

    private var _isUserBlocked = mutableStateOf(false)
    val isUserBlocked: State<Boolean> = _isUserBlocked

    init {
        checkLogin()
    }

    private fun checkLogin() {
        viewModelScope.launch {
            _isLoggedIn.value = isUserLoggedInUseCase()

            val response = isUserBlockedUseCase()

            if(response.status == "Success") {

                Log.d("MainViewModel", ""+response.blocked)
                _isUserBlocked.value = response.blocked == true
            }
        }
    }

    fun setLoggedIn(value: Boolean) {
        _isLoggedIn.value = value
    }

}