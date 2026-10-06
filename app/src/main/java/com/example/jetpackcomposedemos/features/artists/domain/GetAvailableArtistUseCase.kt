package com.example.jetpackcomposedemos.features.artists.domain

import com.example.jetpackcomposedemos.features.artists.data.DefaultArtistRepository
import javax.inject.Inject

class GetAvailableArtistUseCase @Inject constructor(
    private val artistRepository: ArtistRepository
) {

    suspend operator fun invoke(): List<Artist> {
        return artistRepository
            .getArtists()
            .filter { it.isAvailable }
            .sortedBy { it.name }
    }

}