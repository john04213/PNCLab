package com.example.jetpackcomposedemos.features.artists.presentation.composables

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import com.example.jetpackcomposedemos.features.artists.domain.Artist
import com.example.jetpackcomposedemos.features.artists.presentation.state.ArtistDirectoryState
import com.example.jetpackcomposedemos.testutil.builders.anArtist
import com.example.jetpackcomposedemos.ui.theme.KazooTheme
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ArtistDirectoryTest {

    @get:Rule
    val composeRule = createComposeRule()

    private fun artists(count: Int): List<Artist> = List(count) { i ->
    anArtist(
        id = i + 1,
        name = "Artist ${i + 1}",
        genre = if ( i % 2 == 0) "Rock" else ("Pop"),
        location = if ( i % 2 == 0) "New York, NY" else ("Miami"),
    )
    }

    private fun setDirectory(
        state: ArtistDirectoryState,
        useTwoPaneLayout: Boolean = false,
        onBack: () -> Unit = {},
        onArtistSelected: (Int) -> Unit = {},
        onGenreChange: (String) -> Unit = {},
        onLocationChange: (String) -> Unit = {},
        onShowFilterChange: (Boolean) -> Unit = {},
    ) {
        composeRule.setContent {
            KazooTheme {
                ArtistDirectory(
                    state = state,
                    useTwoPaneLayout = useTwoPaneLayout,
                    onBack = onBack,
                    onArtistSelected = onArtistSelected,
                    onGenreChange = onGenreChange,
                    onLocationChange = onLocationChange,
                    onShowFilterChange = onShowFilterChange
                )
            }
        }
    }

// create our composable with a dynamic state
private fun setDirectoryWithHosts(
    initial: ArtistDirectoryState
){
    composeRule.setContent {
        KazooTheme {
            var state by remember { mutableStateOf(initial) }
            ArtistDirectory(
                state = state,
                useTwoPaneLayout = false,
                onGenreChange = { state = state.copy(genreFilter = it) },
                onLocationChange = { state = state.copy(locationFilter = it) },
                onShowFilterChange = { state = state.copy(showFilter = it) }

            )

        }
    }
}

    @Test
    fun givenState_whenShown_thenDisplaysTitleAndArtistCount(){
        setDirectory(ArtistDirectoryState(artists = artists(3)))

        composeRule.onNodeWithText("Available Artists").assertIsDisplayed()
        composeRule.onNodeWithText("Displaying 3 artists").assertIsDisplayed()
    }

    @Test
    fun givenState_whenShown_thenDisplaysTheArtistNames() {
        val artists = artists(3)
        setDirectory(ArtistDirectoryState(artists = artists(3)))

        artists.map{it.name }.forEach { name ->
            composeRule.onNodeWithText(name, useUnmergedTree = true).assertIsDisplayed()
        }
    }

    @Test
    fun givenFilterInState_whenShown_thenOnlyMatchingArtistsAreListed() {
        val artists = artists(4)
        setDirectory(ArtistDirectoryState(
            artists = artists,
            genreFilter = "Rock"
        ))
        composeRule.onNodeWithText("Displaying 2 artists").assertIsDisplayed()
        composeRule.onNodeWithText(artists[0].name, useUnmergedTree = true).assertIsDisplayed()
        composeRule.onNodeWithText(artists[1].name, useUnmergedTree = true).assertDoesNotExist()
    }

    @Test
    fun givenLocationFilterInState_thenOnlyMatchingArtistsAreListed() {
        val artists = artists(4)
        setDirectory(ArtistDirectoryState(
            artists = artists,
            locationFilter = "Miami"
        ))
        composeRule.onNodeWithText("Displaying 2 artists").assertIsDisplayed()
        composeRule.onNodeWithText(artists[0].name, useUnmergedTree = true).assertDoesNotExist()
        composeRule.onNodeWithText(artists[1].name, useUnmergedTree = true).assertIsDisplayed()

    }

    // - - - - - - - - - - - - - - -interaction- - - - - - - - - - - - - - -
    @Test
    fun givenDirectory_whenArtistTapped_thenOnArtistSelectedReceivesItsId() {
        var selectedId: Int? = null

        setDirectory(
            ArtistDirectoryState(artists = artists(3)),
            onArtistSelected = { selectedId = it }
        )

        composeRule.onNodeWithText("Artist 2", useUnmergedTree = true).performClick()

        assertEquals(2, selectedId)
    }

    @Test
    fun givenDirectory_whenShowFilterTapped_thenRequestToShowFilter() {
        var requested: Boolean? = null

        setDirectory(
            ArtistDirectoryState(artists = artists(3)),
            onShowFilterChange = { requested = it }
        )

        composeRule.onNodeWithContentDescription("Filter").performClick()

        assertEquals(true, requested)
    }

    @Test
    fun givenLongList_whenShown_thenOffscreenArtistsAreNotComposedUntilScrolledTo() {
        setDirectoryWithHosts(ArtistDirectoryState(artists = artists(100)))

        composeRule.onNodeWithText("Artist 87",useUnmergedTree = true).assertDoesNotExist()
        // lazy column only composes what is visible, so this node should not exist yet

        // lazyColumn is scrollable, so scroll index 86 to view
        composeRule.onNode(hasScrollAction()).performScrollToIndex(86)
        composeRule.onNodeWithText("Artist 87",useUnmergedTree = true).assertIsDisplayed()
    }

    // --------- tests that require a dynamic state for the composable ---------
    @Test
    fun givenDirectory_WhenShownFilterTapped_ThenFilterSheetAppears(){
        setDirectoryWithHosts(ArtistDirectoryState(artists = artists(4)))

        composeRule.onNodeWithText("Filter Artists").assertDoesNotExist()

        composeRule.onNodeWithContentDescription("Filter").performClick()

        composeRule.onNodeWithText("Filter Artist").assertIsDisplayed()
    }

    @Test
    fun givenDirectory_whenFilterCloseTapped_thenFilterSheetDisappears(){
        setDirectoryWithHosts(ArtistDirectoryState(artists = artists(4),
            showFilter = true
        ))

//        composeRule.onNodeWithContentDescription("Filter").performClick()
        composeRule.onNodeWithText("Filter Artist").assertIsDisplayed()
        composeRule.onNodeWithText("Close").performClick()
        composeRule.onNodeWithText("Filter Artist").assertDoesNotExist()
    }

    @Test
    fun givenFilterSheetOpen_whenGenreTyped_thenListIsFiltered(){
        setDirectoryWithHosts(ArtistDirectoryState(artists = artists(4),
            showFilter = true
        ))
        composeRule.onNodeWithText("Displaying 4 artists").assertIsDisplayed()
        composeRule.onNodeWithText("Genre:").performTextInput("Rock")
        composeRule.onNodeWithText("Displaying 2 artists").assertIsDisplayed()
    }
}
