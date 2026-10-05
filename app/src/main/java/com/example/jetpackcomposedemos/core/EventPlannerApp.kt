package com.example.jetpackcomposedemos

import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.window.core.layout.WindowSizeClass

@Composable
fun EventPlannerApp(){

    val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
    val useTwoPaneLayout = windowSizeClass.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
    )

    val navController = rememberNavController()
    val artists = Artist.getArtists()
    val boardMembers = BoardMember.getBoardMembers()

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
                onLegacyArtists = {
                    navController.navigate(LegacyArtistListRoute)
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

            val artist = artists.find { it.id == artistId }
            if (artist != null){
                ArtistDetails(artist = artist)
            }else{
                Text("Artist not found")

            }

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

        composable<LegacyArtistListRoute> {
            LegacyArtistList(
                artists = artists,
                onArtistSelected = { artistId ->
                    navController.navigate(ArtistDetailsRoute(artistId))
                }
            )
        }

    }
}

