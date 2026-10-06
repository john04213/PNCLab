package com.example.jetpackcomposedemos.core

import com.example.jetpackcomposedemos.features.artists.data.ArtistDataSource
import com.example.jetpackcomposedemos.features.artists.data.DefaultArtistRepository
import com.example.jetpackcomposedemos.features.artists.data.HardCodedArtistDataSource
import com.example.jetpackcomposedemos.features.artists.domain.ArtistRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class AppInjectionModule {

    @Binds
    abstract fun bindArtistDataSource(
        implementation: HardCodedArtistDataSource
    ): ArtistDataSource

    @Binds
    abstract fun bindArtistRepository(
        implementation: DefaultArtistRepository
    ): ArtistRepository

}