package com.example.jetpackcomposedemos.core.room

import android.content.Context
import androidx.room.Room
import com.example.jetpackcomposedemos.features.orders.data.local.OrderDao
import com.example.jetpackcomposedemos.features.todo.data.ToDoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): AppDatabase {
        return  Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app.database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideToDoDao(database: AppDatabase): ToDoDao {
        return database.toDoDao()
    }

    @Provides
    @Singleton
    fun provideOrderDao(database: AppDatabase): OrderDao {
        return database.orderDao()
    }
}