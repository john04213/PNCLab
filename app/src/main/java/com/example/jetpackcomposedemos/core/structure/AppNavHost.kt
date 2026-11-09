package com.example.jetpackcomposedemos.core.structure

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.jetpackcomposedemos.core.ArtistListRoute
import com.example.jetpackcomposedemos.core.BoardMemberDetailsRoute
import com.example.jetpackcomposedemos.core.BoardMemberListRoute
import com.example.jetpackcomposedemos.core.DashboardRoute
import com.example.jetpackcomposedemos.core.EventPlanningDashboard
import com.example.jetpackcomposedemos.core.OrdersRoute
import com.example.jetpackcomposedemos.core.SettingsRoute
import com.example.jetpackcomposedemos.core.ToDosRoute
import com.example.jetpackcomposedemos.core.settings.SettingsScreen
import com.example.jetpackcomposedemos.features.artists.presentation.composables.ArtistDirectoryRoute
import com.example.jetpackcomposedemos.features.boardmembers.domain.BoardMember
import com.example.jetpackcomposedemos.features.boardmembers.presentation.BoardMemberDetails
import com.example.jetpackcomposedemos.features.boardmembers.presentation.BoardMemberList
import com.example.jetpackcomposedemos.features.orders.presentation.PagedOrdersScreen
import com.example.jetpackcomposedemos.features.todo.presentation.ToDosScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    useTwoPaneLayout: Boolean,
    onSignOut: () -> Unit,
) {
    val boardMembers = BoardMember.getBoardMembers()
    NavHost(
        navController = navController,
        startDestination = DashboardRoute
    ) {
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
                },
                onSignOut =onSignOut,
                onSettings = {
                    navController.navigate(SettingsRoute)
                }
            )
        }

        composable<ArtistListRoute> {
            // load the artists from repository or service class

            ArtistDirectoryRoute(
                onBack = {
                    navController.popBackStack()
                }
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

        composable<BoardMemberDetailsRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<BoardMemberDetailsRoute>()
            val memberId = route.boardMemberId

            val member = boardMembers.find { it.id == memberId }

            if (member != null) {
                BoardMemberDetails(boardMember = member)
            } else {
                Text("Board Member not found")
            }
        }

        composable<OrdersRoute> {
            PagedOrdersScreen(useTwoPaneLayout = useTwoPaneLayout)
        }

        composable<ToDosRoute> {
            ToDosScreen(useTwoPaneLayout = useTwoPaneLayout)
        }

        composable<SettingsRoute> {
            SettingsScreen()
        }


    }
}