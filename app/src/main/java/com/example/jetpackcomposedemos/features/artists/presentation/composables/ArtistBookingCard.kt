package com.example.jetpackcomposedemos.features.artists.presentation.composables

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.jetpackcomposedemos.features.artists.domain.Artist

@Composable
fun ArtistBookingCard(artist: Artist) {
    Log.d("Compose Demo", "ArtistBookingCard: Composing")
// longer syntax, works with mutableState wrapper around our values
//    val selectedState = remember {
//        mutableStateOf(false)
//    }

    // shorter syntax
    var isSelected  by remember {
        mutableStateOf(false)
    }


    Column(
        modifier = Modifier.padding(16.dp)

    ) {
        ArtistInformation(artist = artist)
        BookingControls(
            isSelected = isSelected,
            onSelectionChange = {
                isSelected = !isSelected
            }
        )
    }
}

@Composable
fun ArtistInformation(artist: Artist) {

    Log.d("Composed Demo", "Artist information: Composing")
    Text(artist.name)
    Text(artist.genre)

}

@Composable
fun BookingControls(
    isSelected: Boolean,
    onSelectionChange: () -> Unit

) {
    Log.d("Composed Demo", "Artist information: Composing with $isSelected")

    Button(
        onClick = onSelectionChange
    ) {
        Text(
            if (isSelected) {
                "Remove from event"
            } else {
                "Add to event"
            }
        )
    }
}
