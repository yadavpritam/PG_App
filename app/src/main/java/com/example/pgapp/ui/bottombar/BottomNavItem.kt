package com.example.pgapp.ui.bottombar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.pgapp.navigation.NavRoutes

sealed class BottomNavItem (
    val route: String,
    val icon: ImageVector,
    val label: String
){
    object Explore : BottomNavItem(NavRoutes.EXPLORE, Icons.Default.Explore, "Explore")
    object Saved : BottomNavItem(NavRoutes.SAVED, Icons.Default.FavoriteBorder, "Saved")
    object AddPg : BottomNavItem(NavRoutes.ADD_PG, Icons.Default.AddCircleOutline, "Add PG")
    object Profile : BottomNavItem(NavRoutes.PROFILE, Icons.Default.AccountCircle, "Profile")
}