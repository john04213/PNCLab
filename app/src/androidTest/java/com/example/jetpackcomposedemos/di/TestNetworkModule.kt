package com.example.jetpackcomposedemos.di

import com.example.jetpackcomposedemos.core.NetworkModule
import com.example.jetpackcomposedemos.features.artists.data.ArtistDataSource
import com.example.jetpackcomposedemos.features.orders.data.MyOrdersDataSource
import com.example.jetpackcomposedemos.features.orders.data.OrderDataSource
import com.example.jetpackcomposedemos.testutil.fakes.FakeArtistDataSource
import com.example.jetpackcomposedemos.testutil.fakes.FakeMyOrderDataSource
import com.example.jetpackcomposedemos.testutil.fakes.FakeOrderDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

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

    @Binds
    abstract fun bindMyOrderDataSource(fake: FakeMyOrderDataSource): MyOrdersDataSource

}


