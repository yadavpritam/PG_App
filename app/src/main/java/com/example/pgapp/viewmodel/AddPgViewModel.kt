package com.example.pgapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pgapp.data.model.pgmodel.PgModel
import com.example.pgapp.data.remote.FirebaseService
import com.example.pgapp.data.repository.PgRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPgViewModel @Inject constructor(
    private val repo: PgRepository,
    private val firebase: FirebaseService
) : ViewModel() {

    var isLoading = mutableStateOf(false)
    var isSuccess = mutableStateOf(false)
    var error = mutableStateOf<String?>(null)

    fun addPg(pg: PgModel) {

        // BASIC VALIDATION
        if (pg.name.isBlank() || pg.location.isBlank() || pg.price <= 0) {
            error.value = "Please fill all required fields"
            return
        }

        viewModelScope.launch {
            try {
                isLoading.value = true
                error.value = null
                isSuccess.value = false

                val userId = firebase.getCurrentUserId()

                if (userId == null) {
                    error.value = "User not logged in"
                    return@launch
                }

                repo.addPg(
                    pg.copy(
                        ownerId = userId,
                        createdAt = System.currentTimeMillis()
                    )
                )

                isSuccess.value = true

            } catch (e: Exception) {
                error.value = e.message
            } finally {
                isLoading.value = false
            }
        }
    }

    fun resetState() {
        isSuccess.value = false
        error.value = null
    }
}