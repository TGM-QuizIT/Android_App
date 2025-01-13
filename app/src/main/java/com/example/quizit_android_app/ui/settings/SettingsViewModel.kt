package com.example.quizit_android_app.ui.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(

): ViewModel() {

    init {
        getUser()
    }

    private fun getUser() {
        // TODO Get user data
    }

    public fun logOut() {
        // TODO Log out user
    }

}