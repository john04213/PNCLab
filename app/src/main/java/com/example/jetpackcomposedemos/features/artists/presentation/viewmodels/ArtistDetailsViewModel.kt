package com.example.jetpackcomposedemos.features.artists.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcomposedemos.core.LoadableState
import com.example.jetpackcomposedemos.features.artists.data.GetArtistUseCase
import com.example.jetpackcomposedemos.features.artists.domain.Artist

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistDetailsViewModel @Inject constructor(
    private val getArtist: GetArtistUseCase
): ViewModel() {

    private val _uiState: MutableStateFlow<LoadableState<Artist>> = MutableStateFlow(
        LoadableState.Loading
    )

    val uiState: StateFlow<LoadableState<Artist>> = _uiState.asStateFlow()

    fun loadArtist(id: Int) {
        viewModelScope.launch {
            val artist = getArtist(id)
            if(artist == null) {
                _uiState.value = LoadableState.Empty
            } else {
                _uiState.value = LoadableState.Success(artist)
            }
        }
    }
}