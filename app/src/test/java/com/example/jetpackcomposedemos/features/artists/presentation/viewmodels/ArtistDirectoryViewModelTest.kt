package com.example.jetpackcomposedemos.features.artists.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import com.example.jetpackcomposedemos.testutil.MainDispatcherRule
import org.junit.Rule
import com.example.jetpackcomposedemos.features.artists.data.FakeArtistRepository
import com.example.jetpackcomposedemos.features.artists.domain.RefreshArtistsUseCase
import com.example.jetpackcomposedemos.features.artists.domain.GetAvailableArtistsUseCase
import org.junit.Test
import junit.framework.TestCase.assertEquals

class ArtistDirectoryViewModelTest {

    @get:Rule
    val mainDispatcherRule =
        MainDispatcherRule()

    private fun viewModelWith(savedState: SavedStateHandle = SavedStateHandle()):
            ArtistDirectoryViewModel {
            val repository = FakeArtistRepository()
            return ArtistDirectoryViewModel(
                getAvailableArtists = GetAvailableArtistsUseCase(repository),
                refreshArtists = RefreshArtistsUseCase(repository),
                savedStateHandle = savedState
            )
        }


    @Test
    fun `filter saved before the process was killed are shown again`() {
        // create the saved state that Android would give to our view model
        val restored = SavedStateHandle(mapOf(
            ArtistDirectoryViewModel.GENRE_KEY to "Rock",
            ArtistDirectoryViewModel.LOCATION_KEY to "Miami"
        ))
        val viewModel = viewModelWith(restored)
        assertEquals("Rock", viewModel.uiState.value.genreFilter)
        assertEquals("Miami", viewModel.uiState.value.locationFilter)
    }

    @Test
    fun `changing a filter write it to the saved state`() {
        val savedState = SavedStateHandle()
        val viewModel = viewModelWith(savedState)

        viewModel.updateGenreFilter("Pop")
        viewModel.updateLocationFilter("Chicago")

        assertEquals("Pop", savedState.get<String>(ArtistDirectoryViewModel.GENRE_KEY))
        assertEquals("Chicago", savedState.get<String>(ArtistDirectoryViewModel.LOCATION_KEY))
    }

    @Test
    fun `a view model with noting saved starts with empty filter`() {
        val viewModel = viewModelWith()
        assertEquals("", viewModel.uiState.value.genreFilter)
        assertEquals("", viewModel.uiState.value.locationFilter)
    }
}