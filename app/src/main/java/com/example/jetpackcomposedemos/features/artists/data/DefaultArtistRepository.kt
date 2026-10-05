package com.example.jetpackcomposedemos.features.artists.data

import com.example.jetpackcomposedemos.features.artists.domain.ArtistRepository
import com.example.jetpackcomposedemos.features.artists.domain.Artist

class DefaultArtostRepository(
    private val dataSource: ArtistDataSource = HardCodedArtistDataSource()
): ArtistRepository {

    override suspend fun getArtists(): List<Artist> {
        return dataSource.getArtists().map {
            ArtistMapper.mapToDomain(it)
            }
    }

    override suspend fun getArtist(id: Int): Artist? {
        return getArtists()
            .firstOrNull {
                it.id == id
            }
    }
}