package com.example.jetpackcomposedemos

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jetpackcomposedemos.databinding.LegacyArtistCardBinding

class ArtistsViewAdapter(
    private val artists: List<Artist>,
    private val onArtistSelected: (Int) -> Unit
): RecyclerView.Adapter<ArtistCardViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArtistCardViewHolder {
        Log.d("ArtistRecycler", "Creating a new ViewHolder")

        val inflater = LayoutInflater.from(parent.context)

        val binding = LegacyArtistCardBinding.inflate(
                inflater,
                parent,
                false
            )

        return ArtistCardViewHolder(binding, onArtistSelected)
    }

    override fun onBindViewHolder(
        holder: ArtistCardViewHolder,
        position: Int
    ){
        val artist = artists[position]
        holder.bind(artist)
        Log.d("ArtistRecycler", "Binding${artist.name} for position $position")
    }

    override fun getItemCount(): Int {
        return artists.size
    }



}


