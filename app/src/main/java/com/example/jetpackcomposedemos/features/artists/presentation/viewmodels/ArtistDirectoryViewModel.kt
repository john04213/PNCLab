package com.example.jetpackcomposedemos.features.artists.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.example.jetpackcomposedemos.features.artists.domain.Artist
import com.example.jetpackcomposedemos.features.artists.presentation.state.ArtistDirectoryState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ArtistDirectoryViewModel: ViewModel() {

    // keep the actual mutable state value private
    private val _uiState = MutableStateFlow(
        ArtistDirectoryState(
            artists = Artist.getArtists()
        )
    )

    // expose (publicly) a rea-only view of the state object
    val uiState: StateFlow<ArtistDirectoryState> = _uiState.asStateFlow()


//    This approach is vulnerable to race conditioning
//    fun updateGenreFilter(genre: String) {
//        _uiState.value = _uiState.value.copy((genreFiler = genre)
//    }
//
    fun updateGenreFilter(genre: String) {
        // change the state in a way that can prevent race conditions
        _uiState.update { currentState ->
            currentState.copy(
                genreFilter = genre
            )
        }
    }
    fun updateLocationFilter(location: String) {
        // change the state in a way that can prevent race conditions
        _uiState.update  {currentState ->
            currentState.copy(
                locationFilter = location
            )
        }
    }

    fun updateShowFilter(show: Boolean) {
        // change the state in a way that can prevent race conditions
        _uiState.update  {currentState ->
            currentState.copy(
                showFilter = show
            )
        }
    }

    fun setSelectedArtist(artist: Int) {
        _uiState.update {currentState ->
            currentState.copy(
                selectedArtistId = artist
            )
        }
    }
}