package com.example.pgapp.data.repository

import com.example.pgapp.data.model.pgmodel.FilterModel
import com.example.pgapp.data.model.pgmodel.PgModel
import com.example.pgapp.data.remote.FirebaseService
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PgRepository @Inject constructor(
    private val firebase: FirebaseService
) {

    private val db = firebase.db

    // ADD PG
    suspend fun addPg(pg: PgModel) {
        val doc = db.collection("pgs").document()
        val pgWithId = pg.copy(id = doc.id)
        doc.set(pgWithId).await()
    }

    // ALL PGs (MAP + LIST)
    suspend fun getAllPgs(): List<PgModel> {
        return db.collection("pgs")
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .await()
            .toObjects(PgModel::class.java)
    }

    // SINGLE PG (DETAIL SCREEN )
    suspend fun getPgById(pgId: String): PgModel? {
        return db.collection("pgs")
            .document(pgId)
            .get()
            .await()
            .toObject(PgModel::class.java)
    }

    // USER PGs (PROFILE SCREEN)
    suspend fun getUserPgs(userId: String): List<PgModel> {
        return db.collection("pgs")
            .whereEqualTo("ownerId", userId)
            .get()
            .await()
            .toObjects(PgModel::class.java)
    }

    //  FILTER
    suspend fun getFilteredPgs(
        maxPrice: Int,
        gender: String?,
        occupancy: String?,
        amenities: List<String>
    ): List<PgModel> {

        val result = db.collection("pgs").get().await()
        val list = result.toObjects(PgModel::class.java)

        return list.filter { pg ->

            val priceMatch = pg.price <= maxPrice

            val genderMatch =
                gender == null || pg.gender == gender

            val occupancyMatch =
                occupancy == null || pg.occupancy == occupancy

            val amenitiesMatch =
                amenities.isEmpty() ||
                        amenities.all { pg.amenities.contains(it) }

            priceMatch && genderMatch && occupancyMatch && amenitiesMatch
        }
    }
    // NEW
    suspend fun getFilteredPgs(filter: FilterModel): List<PgModel> {
        return getFilteredPgs(
            maxPrice = filter.maxPrice,
            gender = filter.gender,
            occupancy = filter.occupancy,
            amenities = filter.amenities
        )
    }
    // ️ TOGGLE SAVE
    suspend fun toggleSave(pgId: String, userId: String) {
        val userRef = db.collection("users").document(userId)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(userRef)
            val saved = snapshot.get("savedPgs") as? List<String> ?: emptyList()

            val updated = if (saved.contains(pgId)) {
                saved - pgId
            } else {
                saved + pgId
            }

            transaction.update(userRef, "savedPgs", updated)
        }.await()
    }

    // GET SAVED PGs
    suspend fun getSavedPgs(userId: String): List<PgModel> {

        return try {

            val userDoc = db.collection("users")
                .document(userId)
                .get()
                .await()

            val savedIds =
                userDoc.get("savedPgs") as? List<String> ?: emptyList()


            if (savedIds.isEmpty()) {
                return emptyList()
            }


            val limitedIds = savedIds.take(10)

            val result = db.collection("pgs")
                .whereIn("id", limitedIds)
                .get()
                .await()

            result.toObjects(PgModel::class.java)

        } catch (e: Exception) {

            emptyList()
        }
    }
}