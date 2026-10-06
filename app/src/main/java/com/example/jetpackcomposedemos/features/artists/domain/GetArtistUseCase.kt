package com.example.jetpackcomposedemos.features.artists.domain

import javax.inject.Inject

class GetArtistUseCase @Inject constructor(
    private val artistRepository: ArtistRepository
) {

    suspend operator fun invoke(id: Int): Artist? {
        return artistRepository.getArtist(id)
    }

}
