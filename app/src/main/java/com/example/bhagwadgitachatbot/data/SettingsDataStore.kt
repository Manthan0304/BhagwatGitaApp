package com.example.bhagwadgitachatbot.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsDataStore(private val context: Context) {
    companion object {
        val FONT_SIZE = floatPreferencesKey("font_size")
        const val DEFAULT_FONT_SIZE = 16f
    }

    val fontSizeFlow: Flow<Float> = context.settingsDataStore.data
        .map { preferences ->
            preferences[FONT_SIZE] ?: DEFAULT_FONT_SIZE
        }

    suspend fun setFontSize(size: Float) {
        context.settingsDataStore.edit { preferences ->
            preferences[FONT_SIZE] = size
        }
    }
} 