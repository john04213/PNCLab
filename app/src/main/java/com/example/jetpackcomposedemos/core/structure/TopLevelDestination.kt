package com.example.jetpackcomposedemos.core.structure

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.jetpackcomposedemos.R
import com.example.jetpackcomposedemos.core.ArtistListRoute
import com.example.jetpackcomposedemos.core.DashboardRoute
import com.example.jetpackcomposedemos.core.EventPlannerRoute
import com.example.jetpackcomposedemos.core.OrdersRoute
import com.example.jetpackcomposedemos.core.SettingsRoute
import com.example.jetpackcomposedemos.core.ToDosRoute

enum class TopLevelDestination (
    val route: EventPlannerRoute,
    @StringRes val labelRes: Int,
    val Icon: ImageVector
) {
    HOME(DashboardRoute, R.string.nav_home, Icons.Filled.Home),
    ARTISTS(ArtistListRoute, R.string.nav_artists, Icons.Filled.Person),
    TODOS(ToDosRoute, R.string.nav_todos, Icons.Filled.CheckCircle),
    ORDERS(OrdersRoute, R.string.nav_orders, Icons.Filled.ShoppingCart),
    SETTINGS(SettingsRoute, R.string.nav_settings, Icons.Filled.Settings)
}