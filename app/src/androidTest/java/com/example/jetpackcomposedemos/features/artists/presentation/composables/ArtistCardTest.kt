package com.example.jetpackcomposedemos.features.artists.presentation.composables

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import com.example.jetpackcomposedemos.testutil.builders.anArtist
import com.example.jetpackcomposedemos.ui.theme.KazooTheme
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test

class ArtistCardTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val artist = anArtist(
        name = "High Voltage",
        genre = "Rock",
        location = "Los Angeles, CA",
        description = "Loud and proud"
    )

    private var clickCount = 0

    private fun setCard(){
        composeRule.setContent {
            KazooTheme{
                ArtistCard(
                    artist = artist,
                    onClick = { clickCount++ }
                )
            }
        }
    }

//    @Test
//    fun explore_printSemanticsTrees(){
//        setCard()
//        composeRule.onRoot().printToLog("Artist_Card_Merges")
//        composeRule.onRoot(useUnmergedTree = true).printToLog("Artist_Card_Unmerged")
//    }

    @Test
    fun givenArtist_whenShown_thenDisplaysNameGenreLocationAndDescription(){
        setCard()

        composeRule.onNodeWithText(artist.name, useUnmergedTree = true).assertIsDisplayed()
        composeRule.onNodeWithText("Genre: ${artist.genre}", useUnmergedTree = true).assertIsDisplayed()
        composeRule.onNodeWithText("Location: ${artist.location}", useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun givenCard_whenTapped_thenOnClickedIsCalledOnce(){

        setCard()

        composeRule.onNodeWithText(artist.name, useUnmergedTree = true).performClick()
        assertEquals(1, clickCount)
    }


    @Test
    fun givenNotFavorite_whenStarTapped_thenBecomesFavorite() {
        setCard()

        composeRule.onNodeWithContentDescription("Not favorite", useUnmergedTree = true).performClick()

        composeRule.onNodeWithContentDescription("Favorite", useUnmergedTree = true).assertIsDisplayed()

        composeRule.onNodeWithContentDescription("Not favorite", useUnmergedTree = true).assertDoesNotExist()
    }

    @Test
    fun givenFavorite_whenStartTappedAgain_thenReturnsToNotFavorite(){
        setCard()

        composeRule.onNodeWithContentDescription("Not favorite", useUnmergedTree = true).performClick()

        composeRule.onNodeWithContentDescription("Favorite", useUnmergedTree = true).performClick()

        composeRule.onNodeWithContentDescription("Not favorite", useUnmergedTree = true).assertIsDisplayed()

        composeRule.onNodeWithContentDescription("Favorite", useUnmergedTree = true).assertDoesNotExist()



    }
}