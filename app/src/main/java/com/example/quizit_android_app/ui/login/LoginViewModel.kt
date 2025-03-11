package com.example.quizit_android_app.ui.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.quizit_android_app.navigation.LoginRoute
import com.example.quizit_android_app.usecases.user.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val loginUseCase: LoginUseCase,

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
                if(user != null) {
                    _isLogInSuccess.value = true
                    _errorMessage.value = ""

                } else {
                    _errorMessage.value = "Login fehlgeschlagen. Überprüfe deine Eingaben"
                }

            } catch (e: Exception) {
                _errorMessage.value = "Login fehlgeschlagen. Überprüfe deine Eingaben"
            }
        }

    }
}