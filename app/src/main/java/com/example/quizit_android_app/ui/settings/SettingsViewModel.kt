package com.example.quizit_android_app.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quizit_android_app.usecases.GetUserUseCase
import com.example.quizit_android_app.usecases.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val logoutUseCase: LogoutUseCase,
): ViewModel() {



    init {
        getUser()
    }

    private fun getUser() {

        viewModelScope.launch {
            val user = getUserUseCase()

        }

    }

    public fun logOut() {

        viewModelScope.launch {
            logoutUseCase()
        }

    }

}