package com.example.quizit_android_app.ui.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizit_android_app.usecases.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
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
            val user  = loginUseCase(_username.value, _password.value)
            if(user != null) {
                _isLogInSuccess.value = true

            }

        }

    }
}