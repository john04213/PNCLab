package com.example.jetpackcomposedemos.features.artists.domain

data class Artist (
    val id: Int,
    val name: String,
    val genre: String,
    val location: String,
    val imageUrl: String,
    val description: String,
   val isAvailable: Boolean
){

// we would like a static function to generate some fake artist data
// but kotlin does not have "Static" keyword to create class-level function
// Instead, we do it using a "companion object"


}
