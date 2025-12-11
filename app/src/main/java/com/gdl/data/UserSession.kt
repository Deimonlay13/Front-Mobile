package com.gdl.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow

private val Context.dataStore by preferencesDataStore(name = "user_session")

class UserSession(private val context: Context) {

    companion object {
        private val USER_ID = longPreferencesKey("user_id")
        private val TOKEN = stringPreferencesKey("token")
    }

    suspend fun saveUserSession(id: Long, token: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_ID] = id
            prefs[TOKEN] = token
        }
    }

    fun getUserId(): Flow<Long> =
        context.dataStore.data.map { prefs ->
            prefs[USER_ID] ?: 0L
        }

    fun getToken(): Flow<String> =
        context.dataStore.data.map { prefs ->
            prefs[TOKEN] ?: ""
        }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}

