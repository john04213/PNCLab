package com.example.jetpackcomposedemos.features.auth.data.storage

import com.example.jetpackcomposedemos.features.auth.domain.User

class FakeTokenStore(var saved: User? = null): TokenStore {

    var clearCalls = 0

    override suspend fun load(): User?  = saved

    override suspend fun save(user: User) {
        saved = user
    }

    override suspend fun clear() {
        clearCalls++
        saved = null
    }
}