package com.example.jetpackcomposedemos.core

import com.example.jetpackcomposedemos.features.artists.data.ArtistDataSource
import com.example.jetpackcomposedemos.features.artists.data.DefaultArtistRepository
import com.example.jetpackcomposedemos.features.artists.data.HardCodedArtistDataSource
import com.example.jetpackcomposedemos.features.artists.data.RandomArtistDataSource
import com.example.jetpackcomposedemos.features.artists.domain.ArtistRepository
import com.example.jetpackcomposedemos.features.orders.data.DefaultOrderRepository
import com.example.jetpackcomposedemos.features.orders.data.LocalOnlyOrderRepository
import com.example.jetpackcomposedemos.features.orders.domain.OrderRepository
import com.example.jetpackcomposedemos.features.todo.data.DefaultToDoRepository
import com.example.jetpackcomposedemos.features.todo.domain.ToDoRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationScope

@Module
@InstallIn(SingletonComponent::class)
abstract class AppInjectionModule {



    @Binds
    abstract fun bindArtistRepository(
        implementation: DefaultArtistRepository
    ): ArtistRepository


    @Binds
    abstract fun bindOrderRepository(
        implementation: LocalOnlyOrderRepository
        ): OrderRepository

    @Binds
    abstract fun bindTodoRepository(
        implementation: DefaultToDoRepository
    ): ToDoRepository


    companion object {
        @Provides
        @Singleton
        @ApplicationScope
        fun provideAppScope(): CoroutineScope {
            return CoroutineScope(SupervisorJob() + Dispatchers.Default)
            // NOTE using SupervisorJob keeps one failed child from
            // cancelling the scope for everyone else
        }
    }
}


