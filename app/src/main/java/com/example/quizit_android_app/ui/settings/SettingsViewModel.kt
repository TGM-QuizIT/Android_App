package com.example.quizit_android_app.ui.settings

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizit_android_app.model.retrofit.User
import com.example.quizit_android_app.usecases.user.ChangeUserYearUseCase
import com.example.quizit_android_app.usecases.user.GetUserUseCase
import com.example.quizit_android_app.usecases.user.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val changeUserYearUseCase: ChangeUserYearUseCase,
): ViewModel() {

    private var _user = mutableStateOf<User?>(null)
    val user: User? get() = _user.value

    private var _isLoading = mutableStateOf(false)
    val isLoading: Boolean get() = _isLoading.value


    init {
        getUser()
    }

    private fun getUser() {

        viewModelScope.launch {
            _isLoading.value = true
            try  {
                if(_user.value == null) _user.value = getUserUseCase()
            } catch (e: Exception) {
                _user.value = null
            } finally {
                _isLoading.value = false
            }


        }

    }

    fun updateUserYear(year: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                changeUserYearUseCase(newUserYear = year)
                _user.value?.userYear = year

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }

        }

    }

    public fun logOut() {

        viewModelScope.launch {
            logoutUseCase()
        }

    }


}