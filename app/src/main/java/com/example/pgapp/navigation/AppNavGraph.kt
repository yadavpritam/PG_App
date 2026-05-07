package com.example.pgapp.navigation


import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pgapp.ui.bottombar.BottomNavBar
import com.example.pgapp.ui.screens.AddPgScreen
import com.example.pgapp.ui.screens.AuthScreen
import com.example.pgapp.ui.screens.ExploreScreen
import com.example.pgapp.ui.screens.FilterScreen
import com.example.pgapp.ui.screens.PgDetailScreen
import com.example.pgapp.ui.screens.ProfileScreen
import com.example.pgapp.ui.screens.SavedScreen
import com.example.pgapp.ui.screens.SplashScreen

@Composable
fun AppNavGraph() {

    val navController = rememberNavController()

    // track current route
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    // bottom bar on detail screen
    val showBottomBar = currentRoute in listOf(
        NavRoutes.EXPLORE,
        NavRoutes.SAVED,
        NavRoutes.ADD_PG,
        NavRoutes.PROFILE
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(navController)
            }
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = NavRoutes.SPLASH,
            modifier = Modifier.padding(padding)
        ) {
            composable(NavRoutes.SPLASH) {
                SplashScreen(navController)
            }

            composable(NavRoutes.AUTH) {
                AuthScreen(navController)
            }
            composable(NavRoutes.EXPLORE) {
                ExploreScreen(
                    onPgClick = { pgId ->
                        navController.navigate(NavRoutes.detailRoute(pgId))
                    },
                    navController=navController
                )
            }

            composable(NavRoutes.SAVED) {
                SavedScreen()
            }

            composable(
                route = NavRoutes.ADD_PG,
                arguments = listOf(
                    navArgument("lat") {
                        type = NavType.StringType
                    },
                    navArgument("lng") {
                        type = NavType.StringType
                    }
                )
            ) {

                val lat =
                    it.arguments?.getString("lat")
                        ?.toDoubleOrNull() ?: 0.0

                val lng =
                    it.arguments?.getString("lng")
                        ?.toDoubleOrNull() ?: 0.0

                AddPgScreen(
                    lat = lat,
                    lng = lng,
                    navController = navController
                )
            }

            composable(NavRoutes.PROFILE) {
                ProfileScreen(navController)
            }
            composable(
                route = NavRoutes.FILTER
            ) {

                FilterScreen(

                    navController = navController,

                    onApply = {

                        navController.popBackStack()
                    }
                )
            }
            composable(
                route = NavRoutes.DETAIL,
                arguments = listOf(
                    navArgument("pgId") { type = NavType.StringType }
                )
            ) {
                PgDetailScreen(
                    pgId = it.arguments?.getString("pgId") ?: "",
                    navController = navController
                )
            }
        }
    }
}

