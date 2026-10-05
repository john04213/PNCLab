package com.example.jetpackcomposedemos.features.artists.data

import com.example.jetpackcomposedemos.features.artists.domain.Artist

object AtistMapper {

    fun mapToDoomain(dto: ArtistDto): Artist {
        return Artist(
            id = dto.id,
            name = dto.name,
            genre = dto.genre,
            location = dto.location,
            imageUrl = dto.imageUrl,
            description = dto.description,
            isAvailable = true
        )
    }

    fun mapToDto(domain: Artist): ArtistDto {
        return ArtistDto(
            id = domain.id,
            name = domain.name,
            genre = domain.genre,
            location = domain.location,
            imageUrl = domain.imageUrl,
            description = domain.description,
            tags = ""
        )
    }
}
