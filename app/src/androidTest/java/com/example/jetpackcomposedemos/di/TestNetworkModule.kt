package com.example.jetpackcomposedemos.di

import dagger.Module
import dagger.hilt.testing.TestInstallIn
import dagger.hilt.components.SingletonComponent
import com.example.jetpackcomposedemos.core.NetworkModule
import com.example.jetpackcomposedemos.features.artists.data.ArtistDataSource
import com.example.jetpackcomposedemos.features.orders.data.OrderDataSource
import com.example.jetpackcomposedemos.testutil.fakes.FakeArtistDataSource
import com.example.jetpackcomposedemos.testutil.fakes.FakeOrderDataSource
import dagger.Binds

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkModule::class]
)
abstract class TestNetworkModule {

    @Binds
    abstract fun bindArtistDataSource(fake: FakeArtistDataSource): ArtistDataSource

    @Binds
    abstract fun bindOrderDataSource(fake: FakeOrderDataSource): OrderDataSource

}