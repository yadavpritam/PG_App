package com.example.pgapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.pgapp.data.model.pgmodel.PgModel
import com.example.pgapp.viewmodel.AddPgViewModel

@Composable
fun AddPgScreen(
    lat: Double,
    lng: Double,
    navController: NavController,
    viewModel: AddPgViewModel = hiltViewModel()
) {

    // STATES
    var name by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedRoom by remember { mutableStateOf("Single") }

    val isLoading = viewModel.isLoading.value
    val isSuccess = viewModel.isSuccess.value
    val error = viewModel.error.value

    // SUCCESS
    LaunchedEffect(isSuccess) {

        if (isSuccess) {

            name = ""
            location = ""
            price = ""
            description = ""

            viewModel.resetState()

            navController.popBackStack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {

        Spacer(modifier = Modifier.height(60.dp))

        Text(
            "List Your Accommodation",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(16.dp))

        // IMAGE
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .border(
                    BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.AddAPhoto, null)
        }

        Spacer(Modifier.height(16.dp))

        // INPUTS
        CustomTextField("PG Name", name) { name = it }

        Spacer(Modifier.height(12.dp))

        CustomTextField("Location", location, Icons.Default.LocationOn) {
            location = it
        }

        Spacer(Modifier.height(12.dp))

        CustomTextField("Price", price) { price = it }

        Spacer(Modifier.height(16.dp))

        // ROOM TYPE
        Row {
            listOf("Single", "Double", "Triple").forEach { type ->
                FilterChip(
                    selected = selectedRoom == type,
                    onClick = { selectedRoom = type },
                    label = { Text(type) },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // DESCRIPTION
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            placeholder = { Text("Description") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(Modifier.height(24.dp))

        // BUTTON
        Button(
            onClick = {
                viewModel.addPg(
                    PgModel(
                        name = name,
                        location = location,
                        latitude = lat,
                        longitude = lng,
                        price = price.toIntOrNull() ?: 0,
                        occupancy = selectedRoom,
                        description = description,
                        imageUrls = listOf("https://via.placeholder.com/300")
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Icon(Icons.Default.Upload, null)
                Spacer(Modifier.width(8.dp))
                Text("Submit PG")
            }
        }

        // ERROR
        error?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        // SUCCESS
        if (isSuccess) {
            Text("PG Added Successfully 🎉", color = MaterialTheme.colorScheme.primary)
        }
    }
}

// REUSABLE FIELD
@Composable
fun CustomTextField(
    placeholder: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        placeholder = { Text(placeholder) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        trailingIcon = {
            icon?.let { Icon(it, null) }
        }
    )
}