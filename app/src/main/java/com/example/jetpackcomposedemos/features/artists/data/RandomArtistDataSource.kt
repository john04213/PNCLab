package com.example.jetpackcomposedemos.features.artists.data

import javax.inject.Inject



class RandomArtistDataSource(private val count: Int = 100): ArtistDataSource {
    override suspend fun getArtists(): List<ArtistDto> = List(count) { index ->
        ArtistDto(
            id = index + 1,
            name = "Artist ${index + 1}",
            genre = listOf("Rock, Pop, Hip-Hop", "Acoustic", "Classical").random(),
            location = listOf("New York", "Los Angeles", "Chicago", "Miami", "Houston").random(),
            imageUrl = "Image URL $index",
            description = "Some fake details ",
            tags = "Tag, tag, tag"
        )
    }
}
