package com.example.jetpackcomposedemos.features.artists.data

interface  ArtistDataSource {
    suspend fun getArtists(): List<ArtistDto>
}
