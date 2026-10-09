package com.example.jetpackcomposedemos.core

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.window.core.layout.WindowSizeClass
import com.example.jetpackcomposedemos.features.artists.domain.Artist
import com.example.jetpackcomposedemos.features.artists.presentation.composables.ArtistDetails
import com.example.jetpackcomposedemos.features.artists.presentation.composables.ArtistDirectoryRoute
import com.example.jetpackcomposedemos.features.auth.domain.AuthState
import com.example.jetpackcomposedemos.features.auth.presentation.AuthViewModel
import com.example.jetpackcomposedemos.features.auth.presentation.LoginScreen
import com.example.jetpackcomposedemos.features.boardmembers.domain.BoardMember
import com.example.jetpackcomposedemos.features.boardmembers.presentation.BoardMemberDetails
import com.example.jetpackcomposedemos.features.boardmembers.presentation.BoardMemberList
import com.example.jetpackcomposedemos.legacy.LegacyArtistList
import com.example.jetpackcomposedemos.BuildConfig
import com.example.jetpackcomposedemos.features.orders.presentation.OrdersScreen
import com.example.jetpackcomposedemos.features.todo.presentation.ToDosScreen

@Composable
fun EventPlannerApp(
    viewModel: AuthViewModel = viewModel()
){

    val authState by viewModel.uiState.collectAsState()

    val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
    val useTwoPaneLayout = windowSizeClass.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
    )

    val navController = rememberNavController()
    val boardMembers = BoardMember.getBoardMembers()

    when (authState) {
        is AuthState.Unauthenticated -> {
            LoginScreen(
                onLoginSuccess = { user ->
                    viewModel.login(user)
                }
            )
        }

        is AuthState.Authenticated -> {
            NavHost(
                navController = navController,
                startDestination = DashboardRoute
            ){
                composable<DashboardRoute> {
                    EventPlanningDashboard(
                        onViewArtists = {
                            navController.navigate(ArtistListRoute)
                        },
                        onViewBoardMembers = {
                            navController.navigate(BoardMemberListRoute)
                        },
                        onViewOrders = {
                            navController.navigate(OrdersRoute)
                        },
                        onViewToDos = {
                            navController.navigate(ToDosRoute)
                        }
                    )
                }

                composable<ArtistListRoute> {
                    ArtistDirectoryRoute(
                        onArtistSelected = { artistId ->
                            navController.navigate(ArtistDetailsRoute(artistId))

                        },
                        onBack = {
                            navController.popBackStack()
                        },
                        useTwoPaneLayout = useTwoPaneLayout
                    )
                }
                composable<BoardMemberListRoute> {
                    BoardMemberList(
                        members = boardMembers,
                        onMemberSelected = { memberId ->
                            navController.navigate(BoardMemberDetailsRoute(memberId))
                        },
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                }


                composable<ArtistDetailsRoute> { backStackEntry ->
                    val route = backStackEntry.toRoute<ArtistDetailsRoute>()
                    val artistId = route.artistId
                    ArtistDetails(artistId = artistId)


                }

                composable<BoardMemberDetailsRoute> { backStackEntry ->
                    val route = backStackEntry.toRoute<BoardMemberDetailsRoute>()
                    val memberId = route.boardMemberId

                    val member = boardMembers.find { it.id == memberId }
                    if(member != null) {
                        BoardMemberDetails(boardMember = member)
                    } else {
                        Text("Member not found")
                    }


                }

                composable<OrdersRoute> {
                    OrdersScreen(useTwoPaneLayout = useTwoPaneLayout)
                }

                composable<ToDosRoute> {
                    ToDosScreen(
                        useTwoPaneLayout = useTwoPaneLayout
                    )
                }

            }
        }
    }

}

