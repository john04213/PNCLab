package com.example.jetpackcomposedemos

data class Artist (
    val id: Int,
    val name: String,
    val genre: String,
    val location: String,
    val imageUrl: String,
    val description: String,
    val tags: String
){

// we would like a static function to generate some fake artist data
// but kotlin does not have "Static" keyword to create class-level function
// Instead, we do it using a "companion object"

companion object {
    fun generateArtists(num: Int): List<Artist> = List(num) { index ->
        Artist (
            id = index + 1,
            name = "Artist ${index + 1}",
            genre = listOf("Rock, Pop, Hip-Hop", "Acoustic", "Classical").random(),
            location = listOf("New York", "Los Angeles", "Chicago", "Miami","Houston" ).random(),
            imageUrl = "Image URL $index",
            description = "Some fake details ",
            tags = "Tags, tags, Tags"
        )
    }
}
}

