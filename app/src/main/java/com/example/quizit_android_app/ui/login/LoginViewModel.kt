package com.example.quizit_android_app.ui.login

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.quizit_android_app.navigation.LoginRoute
import com.example.quizit_android_app.usecases.user.IsUserBlockedUseCase
import com.example.quizit_android_app.usecases.user.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val loginUseCase: LoginUseCase,
    private val isUserBlockedUseCase: IsUserBlockedUseCase

    ): ViewModel() {

    private var _username = mutableStateOf("")
    val username: State<String> = _username

    private var _password = mutableStateOf("")
    val password: State<String> = _password

    private var _passwordVisibility = mutableStateOf(false)
    val passwordVisibility: State<Boolean> = _passwordVisibility

    private var _isLogInSuccess  = mutableStateOf(false)
    val isLogInSuccess: State<Boolean> = _isLogInSuccess

    private var _errorMessage = mutableStateOf("")
    val errorMessage: State<String> = _errorMessage

    private var _isUserBlocked = mutableStateOf(false)
    val isUserBlocked: State<Boolean> = _isUserBlocked

    init {
        val isUserBlocked = savedStateHandle.toRoute<LoginRoute>().isUserBlocked


        if(isUserBlocked) {
            _errorMessage.value = "Dein Account ist blockiert, wende dich an deinen KV!"
        }

    }


    public fun setPasswordVisibility() {
        _passwordVisibility.value = !_passwordVisibility.value
    }

    public fun setUsername(username: String) {
        _username.value = username
    }

    public fun setPassword(password: String) {
        _password.value = password
    }
    public fun login() {
        viewModelScope.launch {

            try {
                val user  = loginUseCase(_username.value, _password.value)
                _isUserBlocked.value = isUserBlockedUseCase().blocked ?: false
                if(user != null && !_isUserBlocked.value) {
                    _isLogInSuccess.value = true
                    _errorMessage.value = ""

                } else {
                    if(_isUserBlocked.value) {
                        _errorMessage.value = "Dein Account ist blockiert, wende dich an deinen KV!"
                    } else {
                        _errorMessage.value = "Login fehlgeschlagen. Überprüfe deine Eingaben"
                    }
                }

            } catch (e: Exception) {
                _errorMessage.value = "Login fehlgeschlagen. Überprüfe deine Eingaben"
            }
        }

    }
}