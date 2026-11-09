package com.example.jetpackcomposedemos.features.auth.data.storage

import com.example.jetpackcomposedemos.features.auth.domain.User

interface TokenStore {

    suspend fun load(): User?
    suspend fun save(user: User)
    suspend fun clear()
}