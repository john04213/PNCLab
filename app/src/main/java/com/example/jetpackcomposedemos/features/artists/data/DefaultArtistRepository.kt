package com.example.jetpackcomposedemos.features.artists.data

import com.example.jetpackcomposedemos.features.artists.domain.ArtistRepository
import com.example.jetpackcomposedemos.features.artists.domain.Artist
import javax.inject.Inject

class DefaultArtistRepository @Inject constructor(
    private val dataSource: ArtistDataSource
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