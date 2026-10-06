package com.example.jetpackcomposedemos.features.artists.data

import com.example.jetpackcomposedemos.features.artists.domain.Artist
import com.example.jetpackcomposedemos.features.artists.domain.ArtistRepository
import javax.inject.Inject

class GetArtistUseCase @Inject constructor(
    private val artistRepository: ArtistRepository
) {

    suspend operator fun invoke(id: Int): Artist? {
        return artistRepository.getArtist(id)
    }

}
