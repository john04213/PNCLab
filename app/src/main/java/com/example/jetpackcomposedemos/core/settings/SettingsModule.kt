package com.example.jetpackcomposedemos.core.settings

import android.content.Context
import androidx.datastore.core.DataStore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SettingsDataStore


private val Context.settingsDataStore: DataStore<Preferences>
by preferencesDataStore(name = "settings")
@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsBindingsModule {
    @Binds
    abstract fun bindSettingsRepository(implementation: DataStoreSettingsRepository):
            settingsRepository
}

@Module
@InstallIn(SingletonComponent::class)
object SettingsProvidesModule {
    @Provides
    @Singleton
    @SettingsDataStore
    fun providesSettingsDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.settingsDataStore
}
