package com.example.pgapp.data.model.pgmodel

data class FilterModel(
    val maxPrice: Int = 50000,
    val gender: String? = null,
    val occupancy: String? = null,
    val amenities: List<String> = emptyList()
)