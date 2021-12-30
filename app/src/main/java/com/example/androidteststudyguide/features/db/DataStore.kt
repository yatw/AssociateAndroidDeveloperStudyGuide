package com.example.androidteststudyguide.features.db


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Used DataStore to store key-value pairs
 * https://developer.android.com/topic/libraries/architecture/datastore
 */
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "Environment")

object EnvironmentDataStore {
    val appUserIdKey = stringPreferencesKey("APP_USER_ID")
    val segmentSessionId = stringPreferencesKey("SEGMENT_SESSION_ID")
    val returnedUser = booleanPreferencesKey("RETURNED_USER")

    /**
     * Save data into local storage
     */
    suspend fun <T> setData(context: Context, key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { environment ->
            environment[key] = value
        }
    }

    /**
     * Get string type data from local storage
     */
    suspend fun getStringData(context: Context, key: Preferences.Key<String>): String {
        val stringFlow: Flow<String> = context.dataStore.data.map { environment ->
            environment[key]?:""
        }
        return stringFlow.first()
    }

    /**
     * Get boolean type data from local storage
     */
    suspend fun getBooleanData(context: Context, key: Preferences.Key<Boolean>): Boolean {
        val boolFlow: Flow<Boolean> = context.dataStore.data.map { environment ->
            environment[key]?:false
        }
        return boolFlow.first()
    }

    suspend fun <T> deleteData(context: Context, key: Preferences.Key<T>) {
        context.dataStore.edit { environment ->
            environment.remove(key)
        }
    }

}
