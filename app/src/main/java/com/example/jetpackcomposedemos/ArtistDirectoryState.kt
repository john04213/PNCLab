package com.example.jetpackcomposedemos

data class ArtistDirectoryState (
    val artists: List<Artist> = emptyList(),
    val genreFilter: String = "",
    val locationFilter: String = "",
    val showFilter: Boolean = false,
    val selectedArtistId: Int? = null
){
    val displayedArtist: List<Artist>
        get() = artists.filter {
            (genreFilter == "" || it.genre.contains(genreFilter, ignoreCase = true)) &&
                    (locationFilter == "" || it.location.contains(locationFilter, ignoreCase = true))
        }

    val selectedArtist: Artist?
        get() = artists.find{it.id == selectedArtistId}

}







