package com.example.pgapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.pgapp.navigation.NavRoutes
import com.example.pgapp.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {

    val user = viewModel.user.value
    val isLoading = viewModel.isLoading.value

    LaunchedEffect(Unit) {
        viewModel.loadUser()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
    ) {

     Box(  modifier = Modifier.height(65.dp)){
         // TOP BAR
         TopAppBar(
             title = {
                 Text(
                     "Find your PG",
                     color = MaterialTheme.colorScheme.primary
                 )
             },
             navigationIcon = {
                 IconButton(onClick = {}) {
                     Icon(Icons.Default.Menu, null)
                 }
             },
             actions = {

                 val image = user?.profileImage

                 if (image.isNullOrEmpty()) {
                     Box(
                         modifier = Modifier
                             .padding(end = 12.dp)
                             .size(36.dp)
                             .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
                         contentAlignment = Alignment.Center
                     ) {
                         Text("U")
                     }
                 } else {
                     AsyncImage(
                         model = image,
                         contentDescription = null,
                         modifier = Modifier
                             .padding(end = 12.dp)
                             .size(36.dp)
                             .clip(CircleShape)
                     )
                 }
             }
         )
     }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {

            // PROFILE SECTION
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {

                Spacer(Modifier.height(16.dp))

                Box {

                    val image = user?.profileImage

                    if (image.isNullOrEmpty()) {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No Image")
                        }
                    } else {
                        AsyncImage(
                            model = image,
                            contentDescription = null,
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }

                    IconButton(
                        onClick = {},
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .background(
                                MaterialTheme.colorScheme.primary,
                                CircleShape
                            )
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    user?.name ?: "User",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    user?.email ?: "",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.height(24.dp))

            // MENU CARD
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(8.dp)
            ) {

                MenuItem("My Listings", Icons.Default.Home)
                MenuItem("Saved PGs", Icons.Default.FavoriteBorder)
                MenuItem("Edit Profile", Icons.Default.Edit)
                MenuItem("Settings", Icons.Default.Settings)

                MenuItem(
                    "Logout",
                    Icons.Default.Logout,
                    isLogout = true,
                    onClick = {

                        viewModel.logout()

                        navController.navigate(
                            NavRoutes.AUTH
                        ) {

                            popUpTo(0)
                        }
                    }
                )
            }

            Spacer(Modifier.height(24.dp))

            // APP INFO
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    "APP INFO",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.height(8.dp))

                Row {
                    Text("Version 2.4.0")
                    Spacer(Modifier.width(8.dp))
                    Text("•")
                    Spacer(Modifier.width(8.dp))
                    Text("Privacy Policy")
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}
@Composable
fun MenuItem(
    title: String,
    icon: ImageVector,
    isLogout: Boolean = false,
    onClick: () -> Unit = {}
) {

    val textColor = if (isLogout)
        MaterialTheme.colorScheme.error
    else
        MaterialTheme.colorScheme.onSurface

    val bgColor = if (isLogout)
        MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
    else
        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // ICON BOX
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(bgColor, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = textColor
            )
        }

        Spacer(Modifier.width(12.dp))

        // TITLE
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            color = textColor,
            style = MaterialTheme.typography.bodyLarge
        )

        // ARROW
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}