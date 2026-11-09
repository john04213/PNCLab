package com.example.jetpackcomposedemos.core.structure

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.example.jetpackcomposedemos.ui.theme.LocalSpacing

// used for the screens shown bebore sign-in
// they handle every system bar for themselves
// and they add the side margins

@Composable
fun FullScreenBox(content: @Composable () -> Unit)  {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(horizontal = LocalSpacing.current.large)
    ) {
        content()
    }
}