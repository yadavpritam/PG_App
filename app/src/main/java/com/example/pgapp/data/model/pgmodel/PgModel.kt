package com.example.pgapp.data.model.pgmodel

data class PgModel(
    val id: String = "",
    val name: String = "",
    val location: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,

    val price: Int = 0,
    val gender: String = "", // Boys / Girls / Co-ed
    val occupancy: String = "", // Single / Double / Triple

    val amenities: List<String> = emptyList(),
    val description: String = "",

    val imageUrls: List<String> = emptyList(),

    val ownerId: String = "",
    val isAvailable: Boolean = true,

    val createdAt: Long = System.currentTimeMillis()
)