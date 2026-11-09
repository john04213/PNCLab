package com.example.jetpackcomposedemos.core

import com.example.jetpackcomposedemos.BuildConfig
import com.example.jetpackcomposedemos.features.auth.data.oidc.AuthApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthServer


@Module
@InstallIn(SingletonComponent::class)
class OidcModule {

    @Provides
    @Singleton
    @AuthServer
    fun provideAuthClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .apply {
                if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BASIC
                    // BASIC, not BODY b/c would print access and refresh tokens to logcat

                })
                }
            }
            .build()
    }
    @Provides
    @Singleton
    @AuthServer
    fun providesAuthRetrofit(@AuthServer client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.AUTH_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(@AuthServer retrofit: Retrofit ): AuthApi {
        return retrofit.create(AuthApi::class.java)

    }
}