package com.example.jetpackcomposedemos.core

import android.util.Log
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.window.core.layout.WindowSizeClass
import com.example.jetpackcomposedemos.BuildConfig
import com.example.jetpackcomposedemos.core.settings.SettingsScreen
import com.example.jetpackcomposedemos.core.structure.AppNavHost
import com.example.jetpackcomposedemos.core.structure.AppNavigationShell
import com.example.jetpackcomposedemos.core.structure.FullScreenBox
import com.example.jetpackcomposedemos.features.artists.domain.Artist
import com.example.jetpackcomposedemos.features.artists.presentation.composables.ArtistDetails
import com.example.jetpackcomposedemos.features.artists.presentation.composables.ArtistDirectoryRoute
import com.example.jetpackcomposedemos.features.auth.domain.AuthState
import com.example.jetpackcomposedemos.features.auth.presentation.AuthViewModel
import com.example.jetpackcomposedemos.features.auth.presentation.LoginScreen
import com.example.jetpackcomposedemos.features.auth.presentation.SignInScreen
import com.example.jetpackcomposedemos.features.boardmembers.domain.BoardMember
import com.example.jetpackcomposedemos.features.boardmembers.presentation.BoardMemberDetails
import com.example.jetpackcomposedemos.features.boardmembers.presentation.BoardMemberList
import com.example.jetpackcomposedemos.features.orders.presentation.OrdersScreen
import com.example.jetpackcomposedemos.features.todo.presentation.ToDosScreen
import com.example.jetpackcomposedemos.legacy.LegacyArtistList
import dagger.hilt.android.lifecycle.HiltViewModel

@Composable
fun EventPlannerApp(
    viewModel: AuthViewModel = hiltViewModel()
) {
    val authState by viewModel.uiState.collectAsStateWithLifecycle()

    val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
    val useTwoPaneLayout = windowSizeClass.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
    )

    val navController = rememberNavController()
    val boardMembers = BoardMember.getBoardMembers()

    when (authState) {
        is AuthState.Restoring -> {
            FullScreenBox {
                CircularProgressIndicator()
            }
        }

        is AuthState.Unauthenticated -> {
            FullScreenBox {
                SignInScreen(
                    onSignIn = { user -> viewModel.login(user) }
                )
            }
        }

        is AuthState.Authenticated -> {
            AppNavigationShell(navController = navController) {
                AppNavHost(
                    navController = navController,
                    useTwoPaneLayout = useTwoPaneLayout,
                    onSignOut = {
                        viewModel.logout()
                    }
                )
            }
        }
    }

}



