package com.example.jetpackcomposedemos

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.example.jetpackcomposedemos.databinding.LegacyArtistApprovalBinding

@Composable

fun LegacyArtistApprovalCard(
    artist: Artist,
    isApproved: Boolean,
    onApprovalChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    AndroidViewBinding(
        factory = LegacyArtistApprovalBinding::inflate,
        modifier = modifier
    ){
        artistNameText.text = artist.name
        genreText.text = "Genre: ${artist.genre}"
        locationText.text = "Location: ${artist.location}"
        description.text = artist.description
        approvalStatusText.text = "Approval status: ${if (isApproved) "Approved" else "Pending"}"
        approvalButton.text =
            if (isApproved){
                "Revoke approval"
            } else {
                "Approve Booking "
            }

        approvalButton.setOnClickListener {
            onApprovalChange(!isApproved)
        }
    }
}