package com.example.quizit_android_app.ui.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(

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

        // TODO Handle Login, set isLogInSuccess to true if login is successful and save user data

    }


}