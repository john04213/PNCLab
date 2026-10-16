package com.example.jetpackcomposedemos.di

import android.content.Context
import androidx.room.Room
import com.example.jetpackcomposedemos.core.room.DatabaseModule
import com.example.jetpackcomposedemos.core.room.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
object TestDatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase{
        return Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
    }

    @Provides
    @Singleton
    fun providesToDoDao(db: AppDatabase) = db.toDoDao()

    @Provides
    @Singleton
    fun providesOrderDao(db: AppDatabase) = db.orderDao()

    @Provides
    @Singleton
    fun providesArtistDap(db: AppDatabase) = db.artistDao()


}