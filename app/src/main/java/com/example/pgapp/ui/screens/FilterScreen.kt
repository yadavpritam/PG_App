package com.example.pgapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.pgapp.data.model.pgmodel.FilterModel
import com.example.pgapp.viewmodel.FilterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    navController: NavController,
    viewModel: FilterViewModel = hiltViewModel(),
    onApply: () -> Unit
) {

    var selectedGender by remember { mutableStateOf<String?>(null) }
    var selectedOccupancy by remember { mutableStateOf<String?>(null) }
    var price by remember { mutableStateOf(15000f) }

    val amenities = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        // TOP BAR
        TopAppBar(
            title = { Text("Filters") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.Close, null)
                }
            },
            actions = {
                TextButton(onClick = {
                    selectedGender = null
                    selectedOccupancy = null
                    price = 50000f
                    amenities.clear()
                    viewModel.resetFilter()
                }) {
                    Text("Reset")
                }
            }
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            // PRICE
            Text("Max Price: ₹${price.toInt()}")

            Slider(
                value = price,
                onValueChange = { price = it },
                valueRange = 2000f..50000f
            )

            Spacer(Modifier.height(16.dp))

            // GENDER
            Text("Preferred For")

            Row {
                listOf("Boys", "Girls", "Co-ed").forEach { type ->
                    FilterChip(
                        selected = selectedGender == type,
                        onClick = { selectedGender = type },
                        label = { Text(type) },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            //  OCCUPANCY
            Text("Occupancy")

            Row {
                listOf("Single", "Double", "Triple").forEach {
                    FilterChip(
                        selected = selectedOccupancy == it,
                        onClick = { selectedOccupancy = it },
                        label = { Text(it) },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // AMENITIES
            Text("Amenities")

            Column {
                AmenityCheck("WiFi", amenities)
                AmenityCheck("AC", amenities)
                AmenityCheck("Food", amenities)
                AmenityCheck("Laundry", amenities)
                AmenityCheck("Parking", amenities)
                AmenityCheck("Security", amenities)
            }

            Spacer(Modifier.height(80.dp))
        }

        // APPLY BUTTON
        Button(
            onClick = {

                val filter = FilterModel(
                    maxPrice = price.toInt(),
                    gender = selectedGender,
                    occupancy = selectedOccupancy,
                    amenities = amenities
                )

                viewModel.applyFilter(filter)
                onApply()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Show Results →")
        }
    }
}

// AMENITY CHECK WITH LIST CONNECTION
@Composable
fun AmenityCheck(
    text: String,
    selectedList: MutableList<String>
) {

    val isChecked = selectedList.contains(text)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline,
                RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Checkbox(
            checked = isChecked,
            onCheckedChange = {
                if (it) selectedList.add(text)
                else selectedList.remove(text)
            }
        )

        Spacer(Modifier.width(8.dp))

        Text(text)
    }
}