package com.example.jetpackcomposedemos.features.artists.presentation.composables

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier

//the two scopes a shared element needs
// passing them through ArtistDirectory -> ArtistList -> ArtistCard
// would change every signature, so we will pass them through
// compostionalLocal instead

val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null}
val LocalPaneAnimatedScope = compositionLocalOf<AnimatedVisibilityScope?> { null }


// Marks an image as the same element on the list card and on the details pane,
// so it animates between them.
// Outside a SharedTransionLayout (e.g. previews, UI tests) it does nothing
@Composable
fun Modifier.artistImageSharedElement(artistId: Int): Modifier {
    val sharedScope = LocalSharedTransitionScope.current ?: return this
    val animatedScope = LocalPaneAnimatedScope.current ?: return this

    return with(sharedScope) {
        this@artistImageSharedElement.sharedElement(
            sharedContentState = rememberSharedContentState(key = "artist-image-$artistId"),
            animatedVisibilityScope = animatedScope
        )
    }
}