package com.example.jetpackcomposedemos.features.auth.data

import com.example.jetpackcomposedemos.features.auth.domain.AuthRepository
import com.example.jetpackcomposedemos.features.auth.domain.User

class FakeAuthRepository: AuthRepository {

   override suspend fun login(userId: String, passcode: String): Result<User> {

       if(userId == "hello" && passcode == "world"){
           return Result.success(User(
               id = "abc",
               username = "hello",
               token = "Good for 1 free pizza"
           ))

       }
       return Result.failure(IllegalArgumentException("Invalid credentials"))

    }

}