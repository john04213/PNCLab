package com.example.jetpackcomposedemos.di

import android.content.Context
import androidx.room.Room
import com.example.jetpackcomposedemos.core.room.AppDatabase
import com.example.jetpackcomposedemos.core.room.DatabaseModule
import com.example.jetpackcomposedemos.features.artists.data.local.ArtistDao
import com.example.jetpackcomposedemos.features.orders.data.local.OrderDao
import com.example.jetpackcomposedemos.features.todo.data.ToDoDao
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
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
    }

    @Provides
    @Singleton
    fun provideToDoDao(db: AppDatabase): ToDoDao = db.toDoDao()

    @Provides
    @Singleton
    fun provideOrderDao(db: AppDatabase): OrderDao = db.orderDao()

    @Provides
    @Singleton
    fun provideArtistDao(db: AppDatabase): ArtistDao = db.artistDao()



}




