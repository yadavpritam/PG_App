package com.example.pgapp.data.repository

import com.example.pgapp.data.model.pgmodel.UserModel
import com.example.pgapp.data.remote.FirebaseService
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebase: FirebaseService
) {

    private val auth = firebase.auth
    private val db = firebase.db

    suspend fun getUser(userId: String): UserModel? {
        return db.collection("users")
            .document(userId)
            .get()
            .await()
            .toObject(UserModel::class.java)
    }

    suspend fun login(email: String, password: String): Boolean {
        auth.signInWithEmailAndPassword(email, password).await()
        return true
    }

    suspend fun signup(name: String, email: String, password: String) {
        val result = auth.createUserWithEmailAndPassword(email, password).await()

        val user = UserModel(
            userId = result.user?.uid ?: "",
            name = name,
            email = email
        )

        db.collection("users").document(user.userId).set(user)
    }

    fun logout() {
        auth.signOut()
    }

    fun getCurrentUserId(): String? = auth.currentUser?.uid
}