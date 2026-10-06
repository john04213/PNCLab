package com.example.jetpackcomposedemos.features.auth.presentation

import com.example.jetpackcomposedemos.features.auth.domain.User

sealed interface LoginUiState {

    data object idle: LoginUiState
    data object Loading: LoginUiState
    data class Success(val user: User): LoginUiState
    data class Error(val message: String): LoginUiState
}