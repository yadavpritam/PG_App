package com.example.pgapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.pgapp.viewmodel.DetailViewModel
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController

@Composable
fun PgDetailScreen(
    pgId: String,
    navController: NavController,
    viewModel: DetailViewModel = hiltViewModel()
) {

    val pg = viewModel.pg.value
    val isSaved = viewModel.isSaved.value

    LaunchedEffect(pgId) {
        viewModel.loadPg(pgId)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            TopBar(
                navController = navController,
                pgName = pg?.name ?: "",
                pgLocation = pg?.location ?: "",
                pgPrice = pg?.price ?: 0
            )

            //  IMAGE
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                contentAlignment = Alignment.Center
            ) {

                val image = pg?.imageUrls?.firstOrNull()

                if (image.isNullOrEmpty()) {
                    Text("Image Coming Soon")
                } else {
                    AsyncImage(
                        model = image,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                IconButton(
                    onClick = { viewModel.toggleSave() },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .background(
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                            shape = RoundedCornerShape(50)
                        )
                ) {
                    Icon(
                        if (isSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = Color.Red
                    )
                }
            }

            Column(
                modifier = Modifier
                    .offset(y = (-30).dp)
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)
            ) {

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column {
                        Text(
                            "PREMIUM LISTING",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelSmall
                        )

                        Text(
                            pg?.name ?: "Loading...",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            pg?.location ?: "",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(
                                Color(0xFF4CAF50).copy(alpha = 0.1f),
                                shape = RoundedCornerShape(50)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            if (pg?.isAvailable == true) "Available" else "Not Available",
                            color = Color(0xFF2E7D32)
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))
                Divider()
                Spacer(Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column {
                        Text("Starting from")
                        Text(
                            "₹${pg?.price ?: 0}/mo",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row {
                        FeatureBox(pg?.occupancy ?: "-", Icons.Default.Bed)
                        Spacer(Modifier.width(8.dp))
                        FeatureBox("WiFi", Icons.Default.Wifi)
                    }
                }

                Spacer(Modifier.height(20.dp))

                Text("About this PG", fontWeight = FontWeight.Bold)

                Text(
                    pg?.description ?: "No description available",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.height(20.dp))

                Text("Amenities", fontWeight = FontWeight.Bold)

                Spacer(Modifier.height(8.dp))

                Column {
                    pg?.amenities?.chunked(2)?.forEach { row ->
                        Row {
                            row.forEach {
                                AmenityItem(it, Icons.Default.Wifi)
                            }
                        }
                    }
                }

                Spacer(Modifier.height(80.dp))
            }
        }

        BottomBar(
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
@Composable
fun TopBar(
    navController: NavController,
    pgName: String,
    pgLocation: String,
    pgPrice: Int
) {

    val context = LocalContext.current

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp
            ),

        horizontalArrangement =
            Arrangement.SpaceBetween,

        verticalAlignment = Alignment.CenterVertically
    ) {

        // BACK
        Box(

            modifier = Modifier
                .size(42.dp)
                .background(
                    Color.Black.copy(alpha = 0.45f),
                    CircleShape
                )
                .clickable {

                    navController.popBackStack()
                },

            contentAlignment = Alignment.Center
        ) {

            Icon(
                Icons.Default.Close,
                contentDescription = null,
                tint = Color.White
            )
        }

        // TITLE
        Text(
            "Find your PG",
            color = Color.White
        )

        // SHARE
        Box(

            modifier = Modifier
                .size(42.dp)
                .background(
                    Color.Black.copy(alpha = 0.45f),
                    CircleShape
                )
                .clickable {

                    val shareIntent = Intent().apply {

                        action = Intent.ACTION_SEND

                        putExtra(
                            Intent.EXTRA_TEXT,

                            "🏠 $pgName\n📍 $pgLocation\n💰 ₹$pgPrice/month"
                        )

                        type = "text/plain"
                    }

                    context.startActivity(

                        Intent.createChooser(
                            shareIntent,
                            "Share PG"
                        )
                    )
                },

            contentAlignment = Alignment.Center
        ) {

            Icon(
                Icons.Default.Share,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}
@Composable
fun FeatureBox(text: String, icon: ImageVector) {
    Column(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Text(text)
    }
}
@Composable
fun RowScope.AmenityItem(text: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .weight(1f)
            .padding(6.dp)
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline,
                RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.width(8.dp))
        Text(text)
    }
}
@Composable
fun BottomBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Default.Chat, null)
            Spacer(Modifier.width(6.dp))
            Text("Inquiry")
        }

        Button(
            onClick = {},
            modifier = Modifier.weight(2f)
        ) {
            Icon(Icons.Default.FlashOn, null)
            Spacer(Modifier.width(6.dp))
            Text("Contact / Book")
        }
    }
}