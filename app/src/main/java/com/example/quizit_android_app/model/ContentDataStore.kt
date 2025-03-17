package com.example.quizit_android_app.model

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.quizit_android_app.model.retrofit.AcceptedFriendship
import com.example.quizit_android_app.model.retrofit.DoneChallenges
import com.example.quizit_android_app.model.retrofit.Focus
import com.example.quizit_android_app.model.retrofit.OpenChallenges
import com.example.quizit_android_app.model.retrofit.PendingFriendship
import com.example.quizit_android_app.model.retrofit.Result
import com.example.quizit_android_app.model.retrofit.Stats
import com.example.quizit_android_app.model.retrofit.Subject
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "content_prefs")

class ContentDataStore @Inject constructor(private val context: Context)  {
    private val dataStore = context.dataStore
    private val gson = Gson()

    companion object {
        val SUBJECTS_KEY = stringPreferencesKey("subjects")
        val FOCUS_KEY = stringPreferencesKey("focus")
        val RESULT_KEY = stringPreferencesKey("result")
        val OPEN_CHALLENGES_KEY = stringPreferencesKey("open_challenges")
        val DONE_CHALLENGES_KEY = stringPreferencesKey("done_challenges")
        val STATS_KEY = stringPreferencesKey("stats")
        val ACCEPTED_FRIENDS_KEY = stringPreferencesKey("accepted_friends")
        val PENDING_FRIENDS_KEY = stringPreferencesKey("pending_friends")

        val PENDING_RESULT_KEY = stringPreferencesKey("pending_result")

    }

    // ------------------- Local UI Elements -------------------
    suspend fun saveSubjects(subjects: List<Subject?>) {
        dataStore.edit { preferences ->
            preferences[SUBJECTS_KEY] = gson.toJson(subjects)
        }
    }

    suspend fun getSubjects(): List<Subject> {
        return dataStore.data
            .map { preferences ->
                preferences[SUBJECTS_KEY]?.let {
                    gson.fromJson(it, Array<Subject>::class.java)?.toList() ?: emptyList()
                } ?: emptyList()
            }.first()
    }

    suspend fun saveFocus(focus: List<Focus>?) {
        dataStore.edit { preferences ->
            preferences[FOCUS_KEY] = gson.toJson(focus)
        }
    }

    suspend fun getFocus(): List<Focus> {
        return dataStore.data
            .map { preferences ->
                preferences[FOCUS_KEY]?.let {
                    gson.fromJson(it, Array<Focus>::class.java)?.toList() ?: emptyList()
                } ?: emptyList()
            }.first()
    }

    suspend fun saveResults(results: List<Result>?) {
        dataStore.edit { preferences ->
            preferences[RESULT_KEY] = gson.toJson(results)
        }
    }

    suspend fun getResults(): List<Result> {
        return dataStore.data
            .map { preferences ->
                preferences[RESULT_KEY]?.let {
                    gson.fromJson(it, Array<Result>::class.java)?.toList() ?: emptyList()
                } ?: emptyList()
            }.first()
    }

    suspend fun saveOpenChallenges(openChallenges: List<OpenChallenges>?) {
        dataStore.edit { preferences ->
            preferences[OPEN_CHALLENGES_KEY] = gson.toJson(openChallenges)
        }
    }

    suspend fun getOpenChallenges(): List<OpenChallenges> {
        return dataStore.data
            .map { preferences ->
                preferences[OPEN_CHALLENGES_KEY]?.let {
                    gson.fromJson(it, Array<OpenChallenges>::class.java)?.toList() ?: emptyList()
                } ?: emptyList()
            }.first()
    }

    suspend fun saveDoneChallenges(doneChallenges: List<DoneChallenges>?) {
        dataStore.edit { preferences ->
            preferences[DONE_CHALLENGES_KEY] = gson.toJson(doneChallenges)
        }
    }

    suspend fun getDoneChallenges(): List<DoneChallenges> {
        val resp = dataStore.data
            .map { preferences ->
                preferences[DONE_CHALLENGES_KEY]?.let {
                    gson.fromJson(it, Array<DoneChallenges>::class.java)?.toList() ?: emptyList()
                } ?: emptyList()
            }.first()
        Log.d("ContentDataStore", "getDoneChallenges: $resp")
        return resp
    }

    suspend fun saveStats(stats: Stats?) {
        dataStore.edit { preferences ->
            preferences[STATS_KEY] = gson.toJson(stats)
        }
    }

    suspend fun getStats(): Stats {
        return dataStore.data
            .map { preferences ->
                preferences[STATS_KEY]?.let {
                    gson.fromJson(it, Stats::class.java) ?: Stats()
                } ?: Stats()
            }.first()
    }

    suspend fun saveAcceptedFriends(friends: List<AcceptedFriendship>?) {
        dataStore.edit { preferences ->
            preferences[ACCEPTED_FRIENDS_KEY] = gson.toJson(friends)
        }
    }

    suspend fun getAcceptedFriends(): List<AcceptedFriendship> {
        return dataStore.data
            .map { preferences ->
                preferences[ACCEPTED_FRIENDS_KEY]?.let {
                    gson.fromJson(it, Array<AcceptedFriendship>::class.java)?.toList() ?: emptyList()
                } ?: emptyList()
            }.first()
    }

    suspend fun savePendingFriends(friends: List<PendingFriendship>?) {
        dataStore.edit { preferences ->
            preferences[PENDING_FRIENDS_KEY] = gson.toJson(friends)
        }
    }

    suspend fun getPendingFriends(): List<PendingFriendship> {
        return dataStore.data
            .map { preferences ->
                preferences[PENDING_FRIENDS_KEY]?.let {
                    gson.fromJson(it, Array<PendingFriendship>::class.java)?.toList() ?: emptyList()
                } ?: emptyList()
            }.first()
    }

    suspend fun clearData() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    // ------------------- Local User Inputs -------------------
    suspend fun savePendingResult(result: Result?) {
        dataStore.edit { preferences ->
            preferences[PENDING_RESULT_KEY] = gson.toJson(result)
        }
    }

    suspend fun getPendingResult(): Result {
        return dataStore.data
            .map { preferences ->
                preferences[PENDING_RESULT_KEY]?.let { gson.fromJson(it, Result::class.java) } ?: Result()
            }.first()
    }
}