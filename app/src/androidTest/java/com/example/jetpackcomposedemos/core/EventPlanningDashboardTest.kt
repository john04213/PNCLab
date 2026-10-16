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
    fun setUp(){
        //render the composable being tested
        composeRule.setContent{
            KazooTheme {
                EventPlanningDashboard(
                    onViewArtists = { calls.add("onViewArtists") },
                    onViewBoardMembers = { calls.add("onViewBoardMembers") },
                    onViewOrders = { calls.add("onViewOrders") },
                    onViewToDos = { calls.add("onViewToDos") }
                )
            }
        }
    }
    @Test
    fun givenDashboard_whenShown_thenDisplaysTitle() {
        composeRule.onNode(hasTestTag("dashboard-title")).assertIsDisplayed()
    }
    @Test
    fun givenDashboard_whenShown_thenDisplaysAllFourButtons(){
        listOf(
            "view-artists-button",
            "view-board-members-button",
            "view-orders-button",
            "view-todos-button"
        ).forEach { label ->
            composeRule.onNode(hasTestTag(label)).assertIsDisplayed()
        }
    }

    @Test
    fun givenDashboard_whenViewArtistsTapped_thenOnViewArtistsIsCalled(){
        composeRule.onNode(hasTestTag("view-artists-button")).performClick()
        assertEquals(listOf("onViewArtists"), calls)
    }

    @Test
    fun givenDashBoard_whenEachButtonIsTapped_thenOnlyItsOwnCallbackIsCalled(){
        val expectations = mapOf(
            "view-artists-button" to "onViewArtists",
            "view-board-members-button" to "onViewBoardMembers",
            "view-orders-button" to "onViewOrders",
            "view-todos-button" to "onViewToDos"

        )
        expectations.forEach { (label, expectedCall) ->
            calls.clear()
            composeRule.onNode(hasTestTag(label)).performClick()
            assertEquals("tapping $label",listOf(expectedCall), calls)
        }
    }

    @Test
    fun givenDashboard_whenNothingTapped_thenNoCallbacksAreCalled(){
        composeRule.waitForIdle()
        assertEquals(emptyList<String>(), calls)
    }
}

