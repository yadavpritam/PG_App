package com.example.pgapp.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.pgapp.data.model.pgmodel.PgModel
import com.example.pgapp.navigation.NavRoutes
import com.example.pgapp.utils.DistanceUtil
import com.example.pgapp.utils.LocationHelper
import com.example.pgapp.viewmodel.MapViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@Composable
fun ExploreScreen(
    onPgClick: (String) -> Unit,
    mapViewModel: MapViewModel = hiltViewModel(),
    navController: NavController,
) {
    var cameraState by remember { mutableStateOf<CameraPositionState?>(null) }
    val context = LocalContext.current


    val locationHelper = remember {
        LocationHelper(context)
    }

    val pgList = mapViewModel.pgList

    val selectedPg =
        mapViewModel.selectedPg.value

    var userLat by rememberSaveable {
        mutableStateOf(0.0)
    }

    var userLng by rememberSaveable {
        mutableStateOf(0.0)
    }

    // SEARCH
    var searchText by remember {
        mutableStateOf("")
    }

    // FILTER
    var maxPrice by remember {
        mutableStateOf(50000)
    }

    // LOADING
    var isLoading by remember {
        mutableStateOf(true)
    }

    // LOAD DATA
    LaunchedEffect(Unit) {
        
        mapViewModel.loadData()

        val location =
            locationHelper.getCurrentLocation()

        location?.let {

            userLat = it.latitude
            userLng = it.longitude
        }

        kotlinx.coroutines.delay(1200)

        isLoading = false
    }

    // FILTERING
    val filteredList = pgList.filter { pg ->

        val searchMatch =

            pg.name.contains(searchText, true)

                    ||

                    pg.location.contains(searchText, true)

        val priceMatch =
            pg.price <= maxPrice

        searchMatch && priceMatch
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // MAP
        GoogleMapView(

            pgList = filteredList,

            onMarkerClick = {

                mapViewModel.onMarkerClick(it)
            },

            onMapLongClick = { latLng ->

                navController.navigate(

                    NavRoutes.addPgRoute(
                        latLng.latitude,
                        latLng.longitude
                    )
                )
            },
            onMapReady = {
                cameraState = it
            },
            locationHelper = locationHelper,
            userLat = userLat,
            userLng = userLng
        )

        // DARK OVERLAY
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color.Black.copy(alpha = 0.04f)
                )
        )

        // TOP SECTION
        Column(

            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 10.dp)
        ) {

            // SEARCH
            SearchBar(

                text = searchText,

                onTextChange = {

                    searchText = it
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            // FILTERS
            FilterChips(

                navController = navController,

                onPriceChange = {

                    maxPrice = it
                }
            )
        }

        // PG COUNT BADGE
        AnimatedVisibility(

            visible = filteredList.isNotEmpty(),

            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(
                    top = 150.dp,
                    start = 16.dp
                )
        ) {

            Card(

                shape = RoundedCornerShape(50),

                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                ),

                colors = CardDefaults.cardColors(
                    containerColor =
                        MaterialTheme.colorScheme.surface
                )
            ) {

                Row(

                    modifier = Modifier.padding(
                        horizontal = 14.dp,
                        vertical = 8.dp
                    ),

                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "🏠"
                    )

                    Spacer(
                        modifier = Modifier.width(6.dp)
                    )

                    Text(
                        text =
                            "${filteredList.size} PGs Found",

                        style =
                            MaterialTheme.typography.labelLarge
                    )
                }
            }
        }

        // EMPTY STATE
        AnimatedVisibility(

            visible =
                !isLoading &&
                        filteredList.isEmpty(),

            modifier = Modifier.align(Alignment.Center)
        ) {

            Card(

                shape = RoundedCornerShape(28.dp),

                elevation = CardDefaults.cardElevation(
                    defaultElevation = 12.dp
                )
            ) {

                Column(

                    modifier = Modifier.padding(28.dp),

                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "😕",

                        style =
                            MaterialTheme.typography.displayMedium
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        text = "No PG Found",

                        style =
                            MaterialTheme.typography.titleLarge
                    )

                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )

                    Text(
                        text =
                            "Try changing filters or search",

                        color =
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // BOTTOM SECTION

        var isExpanded by remember {
            mutableStateOf(false)
        }

        AnimatedVisibility(

            visible = filteredList.isNotEmpty(),

            modifier =
                Modifier.align(Alignment.BottomCenter)
        ) {

            Column(

                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                // HANDLE BUTTON
                Card(

                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .clickable {

                            isExpanded = !isExpanded
                        },

                    shape = RoundedCornerShape(50),

                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    )
                ) {

                    Row(

                        modifier = Modifier.padding(
                            horizontal = 18.dp,
                            vertical = 10.dp
                        ),

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Text(

                            text =
                                if (isExpanded)
                                    "▼ Hide PGs"
                                else
                                    "▲ ${filteredList.size} PGs Near You"
                        )
                    }
                }

                // EXPANDABLE SECTION
                AnimatedVisibility(
                    visible = isExpanded
                ) {
                    PgBottomSection(

                        pgList = filteredList,

                        selectedPg = selectedPg,

                        onClick = onPgClick,

                        userLat = userLat,
                        userLng = userLng
                    )
                }
            }
        }
        // RECENTER PG BUTTON

        FloatingActionButton(

            onClick = {

                filteredList.firstOrNull()?.let { pg ->

                    cameraState?.move(

                        CameraUpdateFactory.newLatLngZoom(

                            LatLng(
                                pg.latitude,
                                pg.longitude
                            ),

                            12f
                        )
                    )
                }
            },

            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = 18.dp,
                    bottom = 200.dp
                ),

            containerColor =
                MaterialTheme.colorScheme.surface
        ) {

            Icon(

                Icons.Default.LocationOn,

                contentDescription = null,

                tint = MaterialTheme.colorScheme.primary
            )
        }

        // FAB
        FloatingActionButton(

            onClick = {

                navController.navigate(
                    NavRoutes.ADD_PG
                )
            },

            shape = CircleShape,

            containerColor =
                MaterialTheme.colorScheme.primary,

            contentColor =
                MaterialTheme.colorScheme.onPrimary,

            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = 18.dp,
                    bottom = 120.dp
                )
                .size(64.dp)
        ) {

            Icon(

                Icons.Default.Add,

                contentDescription = null,

                modifier = Modifier.size(30.dp)
            )
        }

        // LOADING
        AnimatedVisibility(

            visible = isLoading,

            modifier = Modifier.align(Alignment.Center)
        ) {

            Card(

                shape = RoundedCornerShape(24.dp),

                elevation = CardDefaults.cardElevation(
                    defaultElevation = 12.dp
                )
            ) {

                Column(

                    modifier = Modifier.padding(28.dp),

                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    CircularProgressIndicator()

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    Text(
                        text = "Loading nearby PGs..."
                    )
                }
            }
        }
    }
}

//
// ---------------- MAP ----------------
//
@Composable
fun GoogleMapView(
    pgList: List<PgModel>,
    onMarkerClick: (PgModel) -> Unit,
    onMapLongClick: (LatLng) -> Unit,
    onMapReady: (CameraPositionState) -> Unit = {},
    locationHelper: LocationHelper,
    userLat: Double,
    userLng: Double,
) {
    val coroutineScope = rememberCoroutineScope()


    // CAMERA STATE
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(0.0, 0.0),
            2f
        )
    }
    LaunchedEffect(Unit) {
        onMapReady(cameraPositionState)
    }
    LaunchedEffect(Unit) {

        val location =
            locationHelper.getCurrentLocation()


        location?.let {

            coroutineScope.launch {

                cameraPositionState.animate(

                    update =
                        CameraUpdateFactory.newLatLngZoom(

                            LatLng(
                                it.latitude,
                                it.longitude
                            ),

                            15f
                        ),

                    durationMs = 1200
                )
            }
        }
    }

    // MAP UI SETTINGS
    val mapUiSettings = remember {

        MapUiSettings(

            zoomControlsEnabled = false,

            compassEnabled = true,

            myLocationButtonEnabled = true,

            mapToolbarEnabled = false
        )
    }

    GoogleMap(

        modifier = Modifier.fillMaxSize(),

        cameraPositionState = cameraPositionState,

        // USER LOCATION
        properties = MapProperties(

            isMyLocationEnabled = true,

            mapType = MapType.NORMAL
        ),

        // UI SETTINGS
        uiSettings = mapUiSettings,

        // LONG PRESS ADD PG
        onMapLongClick = {

            onMapLongClick(it)
        }

    ) {
        // 🔵 MY LOCATION
        if (userLat != 0.0 && userLng != 0.0) {

            MarkerInfoWindow(

                state = MarkerState(

                    position = LatLng(
                        userLat,
                        userLng
                    )
                ),

                title = "You",

                snippet = "Current Location",

                icon = BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_AZURE
                )
            )
        }

        pgList.forEach { pg ->

            if (pg.latitude != 0.0 && pg.longitude != 0.0) {

                val markerPosition = LatLng(
                    pg.latitude,
                    pg.longitude
                )

                // CUSTOM PRICE MARKER
                MarkerInfoWindow(

                    state = MarkerState(
                        position = markerPosition
                    ),

                    title = pg.name,

                    snippet = "₹${pg.price}/month",

                    // CUSTOM MARKER COLOR
                    icon = BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_RED
                    ),

                    onClick = {

                        // CAMERA ANIMATION
                        coroutineScope.launch {

                            cameraPositionState.animate(
                                update = CameraUpdateFactory.newLatLngZoom(
                                    markerPosition,
                                    15f
                                ),

                                durationMs = 700
                            )
                        }

                        onMarkerClick(pg)

                        false
                    }
                ) {

                    // CUSTOM INFO WINDOW
                    Card(

                        shape = RoundedCornerShape(18.dp),

                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 8.dp
                        ),

                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {

                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {

                            Text(
                                text = pg.name,

                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "₹${pg.price}/month",

                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text =
                                    if (pg.isAvailable)
                                        "🟢 Available"
                                    else
                                        "🔴 Occupied",

                                color =
                                    if (pg.isAvailable)
                                        Color(0xFF2E7D32)
                                    else
                                        Color.Red
                            )
                        }
                    }
                }
            }
        }
    }
}

//
// ---------------- SEARCH ----------------
//
@Composable
fun SearchBar(
    text: String,
    onTextChange: (String) -> Unit,
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {

        // GLASS BACKGROUND
        Card(

            modifier = Modifier.fillMaxWidth(),

            shape = RoundedCornerShape(28.dp),

            elevation = CardDefaults.cardElevation(
                defaultElevation = 12.dp
            ),

            colors = CardDefaults.cardColors(
                containerColor =
                    MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
            )
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),

                verticalAlignment = Alignment.CenterVertically
            ) {

                // SEARCH ICON
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                            CircleShape
                        ),

                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // TEXT FIELD
                TextField(

                    value = text,

                    onValueChange = {
                        onTextChange(it)
                    },

                    placeholder = {

                        Text(
                            "Search PG, area, location...",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },

                    modifier = Modifier.weight(1f),

                    singleLine = true,

                    colors = TextFieldDefaults.colors(

                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,

                        disabledContainerColor = Color.Transparent,

                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,

                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )

                // CLEAR BUTTON
                AnimatedVisibility(
                    visible = text.isNotEmpty()
                ) {

                    IconButton(
                        onClick = {
                            onTextChange("")
                        }
                    ) {

                        Icon(
                            Icons.Default.Close,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // FILTER BUTTON
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            CircleShape
                        )
                        .clickable {

                            // future filter action
                        },

                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        Icons.Default.Tune,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

//
// ---------------- FILTERS ----------------
//

@Composable
fun FilterChips(
    navController: NavController,
    onPriceChange: (Int) -> Unit,
) {

    // selected chip state
    var selectedChip by remember {
        mutableStateOf("All")
    }

    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {

        // ALL
        FilterChipItem(
            text = "All",

            isSelected = selectedChip == "All"
        ) {

            selectedChip = "All"

            onPriceChange(50000)
        }

        // 5000
        FilterChipItem(
            text = "Under 5000",

            isSelected = selectedChip == "Under 5000"
        ) {

            selectedChip = "Under 5000"

            onPriceChange(5000)
        }

        // 10000
        FilterChipItem(
            text = "Under 10000",

            isSelected = selectedChip == "Under 10000"
        ) {

            selectedChip = "Under 10000"

            onPriceChange(10000)
        }

        // 15000
        FilterChipItem(
            text = "Under 15000",

            isSelected = selectedChip == "Under 15000"
        ) {

            selectedChip = "Under 15000"

            onPriceChange(15000)
        }

        // FILTER SCREEN
        FilterChipItem(
            text = "Filters",

            isSelected = false
        ) {

            navController.navigate(NavRoutes.FILTER)
        }
    }
}

@Composable
fun FilterChipItem(
    text: String,
    isSelected: Boolean = false,
    onClick: () -> Unit,
) {

    AssistChip(
        onClick = onClick,

        label = {

            Text(
                text = text,

                color =
                    if (isSelected)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.onSurface,

                style = MaterialTheme.typography.labelLarge
            )
        },

        leadingIcon = {

            //  icon
            Icon(
                imageVector =
                    if (text == "Filters")
                        Icons.Default.Tune
                    else
                        Icons.Default.Done,

                contentDescription = null,

                tint =
                    if (isSelected)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.primary,

                modifier = Modifier.size(18.dp)
            )
        },

        shape = RoundedCornerShape(50),

        border = BorderStroke(
            width =
                if (isSelected) 0.dp else 1.dp,

            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        ),

        colors = AssistChipDefaults.assistChipColors(

            containerColor =

                if (isSelected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
        ),

        modifier = Modifier
            .padding(end = 10.dp)
            .height(42.dp)
    )
}

//
// ---------------- BOTTOM SECTION ----------------
//

@Composable
fun PgBottomSection(
    pgList: List<PgModel>,
    selectedPg: PgModel?,
    onClick: (String) -> Unit,
    userLat: Double,
    userLng: Double,
) {

    val displayList =
        if (selectedPg != null)
            listOf(selectedPg)
        else
            pgList

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surface,
                RoundedCornerShape(
                    topStart = 24.dp,
                    topEnd = 24.dp
                )
            )
            .padding(top = 12.dp)
    ) {

        Text(
            text =
                if (selectedPg != null)
                    "Selected PG"
                else
                    "PGs Near You",

            style = MaterialTheme.typography.titleLarge,

            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {

            items(displayList) { pg ->

                PgCard(
                    pg = pg,
                    onClick = onClick,

                    userLat = userLat,
                    userLng = userLng
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

//
// ---------------- CARD ----------------
//

@Composable
fun PgCard(
    pg: PgModel,
    onClick: (String) -> Unit,
    userLat: Double,
    userLng: Double,
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .width(280.dp)
            .padding(end = 14.dp)
            .clickable {
                onClick(pg.id)
            },

        shape = RoundedCornerShape(24.dp),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),

        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {

        Column {

            // IMAGE SECTION
            Box {

                AsyncImage(
                    model =
                        pg.imageUrls.firstOrNull()
                            ?: "https://via.placeholder.com/300",

                    contentDescription = null,

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),

                    contentScale = ContentScale.Crop
                )

                // GRADIENT OVERLAY
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.7f)
                                )
                            )
                        )
                )

                // PRICE TAG
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            RoundedCornerShape(50)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {

                    Text(
                        text = "₹${pg.price}",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                // AVAILABILITY BADGE
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                        .background(
                            if (pg.isAvailable)
                                Color(0xFF2E7D32)
                            else
                                Color.Red,
                            RoundedCornerShape(50)
                        )
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {

                    Text(
                        text =
                            if (pg.isAvailable)
                                "🟢 Available Now"
                            else
                                "🔴 Occupied",

                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            // CONTENT SECTION
            Column(
                modifier = Modifier.padding(14.dp)
            ) {

                // PG NAME
                Text(
                    text = pg.name,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(6.dp))

                // LOCATION ROW
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = pg.location,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // BOTTOM INFO ROW
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    // PRICE TEXT
                    Text(
                        text = "₹${pg.price}/month",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium
                    )

                    // DISTANCE MOCK
                    Text(

                        text = DistanceUtil.calculateDistance(

                            userLat,
                            userLng,

                            pg.latitude,
                            pg.longitude
                        ),

                        color = Color(0xFF1565C0),
                        textDecoration = TextDecoration.Underline,

                        style = MaterialTheme.typography.bodySmall,

                        modifier = Modifier.clickable {

                            val intent = Intent(
                                Intent.ACTION_VIEW,

                                Uri.parse(
                                    "google.navigation:q=${pg.latitude},${pg.longitude}"
                                )
                            )

                            intent.setPackage("com.google.android.apps.maps")

                            context.startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}
