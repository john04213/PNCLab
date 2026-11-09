package com.example.jetpackcomposedemos.core.settings

import kotlinx.coroutines.flow.Flow

interface  settingsRepository {
    val settings: Flow<AppSettings>

    suspend fun setThemeMode(mode: ThemeMode)

    suspend fun setShowArtistLocation(show: Boolean)

}