package com.example.jetpackcomposedemos.features.auth.presentation

import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.jetpackcomposedemos.features.auth.domain.AuthRepository
import com.example.jetpackcomposedemos.features.auth.domain.LoginUseCase
import com.example.jetpackcomposedemos.features.auth.domain.User
import com.example.jetpackcomposedemos.ui.theme.KazooTheme
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
import org.junit.Rule
import androidx.compose.ui.test.performTextInput
import com.example.jetpackcomposedemos.testutil.builders.aUser
import org.junit.Test

@OptIn(ExperimentalTestApi::class)
class LoginScreenSynchronizationTest{
    @get: Rule
    val composeRule = createComposeRule()

    private class SlowAuthReposiitory(
        private val delayMillis: Long,
        private val result: Result<User>
    ): AuthRepository {
        override suspend fun login(userId: String, passcode:String): Result<User> {
            delay(delayMillis)
            return result
        }
    }

    private class GatedAuthRepository: AuthRepository{
        val gate = CompletableDeferred<Result<User>>()
        override suspend fun login(userId: String, passcode: String): Result<User>  = gate.await()
    }

    private val loggedInUsers = mutableListOf<User>()

    private fun setLoginScreen(repository: AuthRepository) {

        val viewModel = LoginViewModel(LoginUseCase(repository))
        composeRule.setContent{
            KazooTheme {
                LoginScreen (
                    viewModel = viewModel,
                    onLoginSuccess = { loggedInUsers.add(it) }
                )
            }
        }
    }

    private val loadingIndicator = hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)

    private fun enterCredentialsAndTapLogin() {
        composeRule.onNodeWithText("User ID").performTextInput("hello")
        composeRule.onNodeWithText("Passcode").performTextInput("world")
        composeRule.onNodeWithText("Login").performClick()
    }

//    @Test
//    fun givenSlowLogin_whenAssertedImmediately_thenFails() {}


    @Test
    fun givenSlowLogin_whenLoginTapped_thenWelcomeAppearsOnceLoginCompletes() {
        setLoginScreen(SlowAuthReposiitory(50, Result.success(aUser(username = "hello"))))

        enterCredentialsAndTapLogin()

        // Compose cannot know how long our repository will take
        composeRule.waitUntilExactlyOneExists(hasText("Welcome, hello"), timeoutMillis = 5_000)
    }

    @Test
    fun givenGatedLogin_whenLoginTapped_thenLoadingShowsUntilTheGateOpens() {
        val repository = GatedAuthRepository()
        setLoginScreen(repository)

        enterCredentialsAndTapLogin()

        // the repository is parked at the gate, so Loading should be on the screen
        // and will stay there until we release the repository from the gate
        composeRule.onNode(loadingIndicator).assertIsDisplayed()
        composeRule.onNodeWithText("welcome, hello").assertDoesNotExist()

            // once we are done with all assertions we want to run at this point
        repository.gate.complete(Result.success(aUser(username = "hello")))

        composeRule.waitUntilExactlyOneExists(hasText("Welcome, hello"),timeoutMillis = 5_000)
        composeRule.onNode(loadingIndicator).assertDoesNotExist()
    }
}