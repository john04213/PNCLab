package com.example.jetpackcomposedemos.features.artists.presentation.composables

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable

@Composable
fun TextFilter(
    label: String,
    Filter: String,
    onFilterChange: (String) -> Unit,


){
    TextField(
        value = Filter,
        onValueChange = onFilterChange,
        label = {
            Text("$label:")
        }
    )
}



