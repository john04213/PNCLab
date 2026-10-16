package com.example.jetpackcomposedemos.features.artists.domain

import com.example.jetpackcomposedemos.features.artists.data.ArtistDataSource
import com.example.jetpackcomposedemos.features.artists.data.ArtistMapper
import com.example.jetpackcomposedemos.features.artists.data.HardCodedArtistDataSource

class AsyncRepository(
    private val dataSource: ArtistDataSource = HardCodedArtistDataSource()
) {
    suspend fun getArtists(): List<Artist> {
        return dataSource.getArtists()

            .map { artistDto ->
                ArtistMapper.mapToDomain(artistDto)
            }
    }
}

