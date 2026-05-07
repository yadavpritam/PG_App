package com.example.pgapp.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.pgapp.navigation.NavRoutes
import com.example.pgapp.viewmodel.AuthViewModel

@Composable
fun SplashScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {

    // NAVIGATION LOGIC
    LaunchedEffect(Unit) {

        kotlinx.coroutines.delay(2000)

        if (authViewModel.isLoggedIn.value) {

            // USER ALREADY LOGIN
            navController.navigate(NavRoutes.EXPLORE) {
                popUpTo(NavRoutes.SPLASH) {
                    inclusive = true
                }
            }

        } else {

            // NOT LOGIN
            navController.navigate(NavRoutes.AUTH) {
                popUpTo(NavRoutes.SPLASH) {
                    inclusive = true
                }
            }
        }
    }

    // DESIGN
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        // BACKGROUND
        Box(
            modifier = Modifier
                .size(350.dp)
                .align(Alignment.TopStart)
                .offset(x = (-100).dp, y = (-100).dp)
                .background(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                    RoundedCornerShape(200.dp)
                )
                .blur(120.dp)
        )

        Box(
            modifier = Modifier
                .size(350.dp)
                .align(Alignment.BottomEnd)
                .offset(x = (100).dp, y = (100).dp)
                .background(
                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                    RoundedCornerShape(200.dp)
                )
                .blur(120.dp)
        )

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(contentAlignment = Alignment.Center) {

                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            RoundedCornerShape(100.dp)
                        )
                        .blur(60.dp)
                )

                Box(
                    modifier = Modifier
                        .shadow(10.dp, RoundedCornerShape(32.dp))
                        .background(
                            MaterialTheme.colorScheme.surface,
                            RoundedCornerShape(32.dp)
                        )
                        .padding(28.dp)
                ) {
                    Icon(
                        Icons.Default.Home,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(60.dp)
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Text("Serene Stay", style = MaterialTheme.typography.headlineLarge)

            Spacer(Modifier.height(8.dp))

            Text("PREMIUM PG LIVING")
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp)
                .background(
                    MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                    RoundedCornerShape(50.dp)
                )
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.VerifiedUser, null)
            Spacer(Modifier.width(8.dp))
            Text("Trusted by 10k+ Professionals")
        }
    }
}