package com.example.pgapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pgapp.data.model.pgmodel.PgModel
import com.example.pgapp.data.remote.FirebaseService
import com.example.pgapp.data.repository.AuthRepository
import com.example.pgapp.data.repository.PgRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository
) : ViewModel() {

    var isLoggedIn = mutableStateOf(false)
        private set

    var isLoading = mutableStateOf(false)
        private set

    var error = mutableStateOf<String?>(null)

    init {
        checkLoginState()
    }

    // CHECK EXISTING USER (IMPORTANT)
    private fun checkLoginState() {
        isLoggedIn.value = repo.getCurrentUserId() != null
    }

    // LOGIN
    fun login(email: String, password: String) {

        if (email.isBlank() || password.isBlank()) {
            error.value = "Email & Password required"
            return
        }

        viewModelScope.launch {
            try {
                isLoading.value = true
                error.value = null

                repo.login(email, password)

                isLoggedIn.value = true

            } catch (e: Exception) {
                error.value = e.message
                isLoggedIn.value = false
            } finally {
                isLoading.value = false
            }
        }
    }

    // SIGN UP
    fun signUp(name: String, email: String, password: String) {

        if (name.isBlank() || email.isBlank() || password.length < 6) {
            error.value = "Fill all fields properly"
            return
        }

        viewModelScope.launch {
            try {
                isLoading.value = true
                error.value = null

                repo.signup(name, email, password)

                isLoggedIn.value = true

            } catch (e: Exception) {
                error.value = e.message
                isLoggedIn.value = false
            } finally {
                isLoading.value = false
            }
        }
    }

    // LOGOUT
    fun logOut() {
        repo.logout()
        isLoggedIn.value = false
    }

    // RESET ERROR
    fun clearError() {
        error.value = null
    }
}