package com.example.jetpackcomposedemos

import androidx.recyclerview.widget.RecyclerView
import com.example.jetpackcomposedemos.databinding.LegacyArtistCardBinding

class ArtistCardViewHolder (
    private val binding: LegacyArtistCardBinding,
    private val onArtistSelected:  (Int) -> Unit
): RecyclerView.ViewHolder(binding.root){
    fun bind(artist: Artist){
        binding.artistNameText.text = artist.name
        binding.genreText.text = "Genre: ${artist.genre}"
        binding.locationText.text = "Location: ${artist.location}"
        binding.descriptionText.text = artist.description

        binding.root.setOnClickListener{
            onArtistSelected(artist.id)
        }
    }
}