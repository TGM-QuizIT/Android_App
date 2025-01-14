package com.example.quizit_android_app.model

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "session_prefs")

class SessionManager @Inject constructor(private val context: Context) {

    private val dataStore = context.dataStore
    private val gson = Gson()


    companion object {
        val USER_KEY = stringPreferencesKey("user")
    }

    val user: Flow<User?> = dataStore.data
        .map { preferences ->
            preferences[USER_KEY]?.let { gson.fromJson(it, User::class.java) }
        }

    suspend fun saveSession(user: User) {
        dataStore.edit { preferences ->
            preferences[USER_KEY] = gson.toJson(user)
        }
    }

    suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun isUserLoggedIn(): Boolean {
        return user.map { it != null }.first()
    }

    suspend fun getUser(): User? {
        return user.first()
    }
}