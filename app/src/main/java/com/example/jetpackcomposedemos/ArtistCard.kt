package com.example.jetpackcomposedemos

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetpackcomposedemos.ui.theme.JetpackComposeDemosTheme

@Composable
fun ArtistCard(artist: Artist) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(){
            Text(
                text = artist.name,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(16.dp)
            )
            Text("Genre: ${artist.genre}")
            Text("Location: ${artist.location}")
        }
    }

}

@Preview(showBackground = true)
@Composable
fun ArtistList() {
    JetpackComposeDemosTheme {
        val artist = Artist(
            id = 1,
            name = "Artist Name",
            genre = "Genre",
            location = "Location",
            imageUrl = "Image URL",
            description = "Description",
            tags = "Tags"
        )
        ArtistCard(
            artist = artist
        )
    }
}