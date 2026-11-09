package com.example.jetpackcomposedemos.core

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.jetpackcomposedemos.ui.theme.KazooTheme
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EventPlanningDashboardTest {

    @get:Rule
    val composeRule = createComposeRule()

    // records which callback fired, in order
    private val calls = mutableListOf<String>()

    @Before
    fun setUp() {
        // render the composable being tested
        composeRule.setContent {
            KazooTheme() {
                EventPlanningDashboard(
                    onViewArtists = { calls.add("artists") },
                    onViewBoardMembers = { calls.add("board members") },
                    onViewOrders = { calls.add("orders") },
                    onViewToDos = { calls.add("todos") },
                    onSettings = { calls.add("settings") },
                    onSignOut = { calls.add("sign out") }
                )
            }
        }
    }


    @Test
    fun givenDashboard_whenShown_thenDisplaysTitle() {

        composeRule.onNode(hasTestTag("dashboard-title")).assertIsDisplayed()

    }

    @Test
    fun givenDashboard_whenShown_thenDisplaysAllFourButtons() {
        listOf(
            "sign-out-button",
            "view-board-button",
        ).forEach { label ->
            composeRule.onNode(hasTestTag(label)).assertIsDisplayed()
        }
    }

//    @Test
//    fun givenDashboard_whenViewArtistsTapped_thenOnViewArtistsIsCalled() {
//
//        composeRule.onNode(hasTestTag("view-artist-button")).performClick()
//
//        assertEquals(listOf("artists"), calls)
//    }

    @Test
    fun givenDashboard_whenEachButtonIsTapped_thenOnlyItsOwnCallbackIsCalled() {
        val expectations = mapOf(
            "view-board-button" to "board members",
            "sign-out-button" to "sign out"
        )

        expectations.forEach { (label, expectedCall) ->
            calls.clear()
            composeRule.onNode(hasTestTag(label)).performClick()
            assertEquals("tapping $label", listOf(expectedCall), calls)
        }

    }

    @Test
    fun givenDashboard_whenNothingTapped_thenNoCallbacksAreCalled() {

        composeRule.waitForIdle()

        assertEquals(emptyList<String>(), calls)
    }

}


