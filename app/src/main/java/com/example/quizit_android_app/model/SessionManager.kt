package com.example.quizit_android_app.model

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "session_prefs")

class SessionManager(private val context: Context) {

    private val dataStore = context.dataStore

    companion object {
        val USERNAME_KEY = stringPreferencesKey("username")
        val TOKEN_KEY = stringPreferencesKey("token")
    }

    val username: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[USERNAME_KEY]
        }

    val token: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[TOKEN_KEY]
        }

    suspend fun saveSession(username: String?, token: String) {
        dataStore.edit { preferences ->
            preferences[USERNAME_KEY] = username?: ""
            preferences[TOKEN_KEY] = token
        }
    }

    suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun isUserLoggedIn(): Boolean {
        return username.map { it != null }.first()
    }
}