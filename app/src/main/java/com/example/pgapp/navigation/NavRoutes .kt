package com.example.pgapp.navigation

object NavRoutes {

    const val SPLASH = "splash"
    const val AUTH = "auth"

    const val EXPLORE = "explore"
    const val SAVED = "saved"
    const val PROFILE = "profile"

    const val DETAIL = "detail/{pgId}"
    const val FILTER = "filter"

    fun detailRoute(pgId: String) = "detail/$pgId"

    const val ADD_PG = "add_pg/{lat}/{lng}"

    fun addPgRoute(
        lat: Double,
        lng: Double
    ): String {
        return "add_pg/$lat/$lng"
    }
}