package com.example.jetpackcomposedemos.features.todo.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.doubleClick
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.test.espresso.action.ViewActions.doubleClick
import com.example.jetpackcomposedemos.core.room.AppDatabase
import com.example.jetpackcomposedemos.features.todo.data.ToDoDao
import com.example.jetpackcomposedemos.features.todo.data.ToDoEntity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import com.example.jetpackcomposedemos.testutil.HiltTestActivity
import com.example.jetpackcomposedemos.testutil.builders.aToDoEntity
import com.example.jetpackcomposedemos.ui.theme.KazooTheme
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class ToDoScreeningIntegrationTest {

    // order matters: Hilt must build its component BEFORE the activity starts
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var toDoDao: ToDoDao
    @Inject
    lateinit var database: AppDatabase

    @Before
    fun setUp(){
        hiltRule.inject()
    }

    @After
    fun tearDown(){
        database.close()
    }

    // ---------- helpers ----------

    private fun showScreen() {
        composeRule.setContent {
            KazooTheme { ToDosScreen(useTwoPaneLayout = false) }
        }

        // The screen starts in Loading and fills in when Room answers on a backgrou                                                                                                                                                                nd
        // thread, which Compose cannot see. Wait for the list's Add button to appear.
        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodesWithContentDescription("Add a new to-do item").fetchSemanticsNodes().isNotEmpty()
        }
    }

    private fun waitForText(text: String) {
        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodesWithText(text, useUnmergedTree = true)
                .fetchSemanticsNodes().isNotEmpty()
        }
    }

    private fun savedToDos(): List<ToDoEntity> = runBlocking { toDoDao.getToDos() }
    private fun seed(vararg titles: String) = runBlocking {
        titles.forEach { toDoDao.insertToDo(aToDoEntity(title = it)) }
    }

    private fun addToDoThroughTheUi(title: String) {
        composeRule.onNodeWithContentDescription("Add").performClick()
        composeRule.onNodeWithText("New to-do").performTextInput(title)
        composeRule.onNodeWithText("Add").performClick()
    }


    // --------------------------- tests ---------------------------
    @Test
    fun givenSavedToDos_whenScreenOpens_thenTheyAreListed(){
        seed("Buy milk", "Walk the dog", "Wash the cat")

        showScreen()

        waitForText("Buy milk")
        composeRule.onNodeWithText("Buy milk", useUnmergedTree = true).assertIsDisplayed()
        composeRule.onNodeWithText("Walk the dog", useUnmergedTree = true).assertIsDisplayed()
        composeRule.onNodeWithText("Wash the cat", useUnmergedTree = true).assertIsDisplayed()

    }

    @Test
    fun givenSavedToDo_whenDoubleTapped_thenItIsMarkedCompletedInTheDatabase() {
        seed("Buy milk")
        showScreen()
        waitForText("Buy milk")

        composeRule.onNodeWithText("Buy milk", useUnmergedTree = true)
            .performTouchInput { doubleClick(delayMillis = 100 ) }

        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodesWithText("Buy milk", useUnmergedTree = true)
                .fetchSemanticsNodes().isNotEmpty()
        }
        assertEquals(true, savedToDos().single().completed)
    }


}