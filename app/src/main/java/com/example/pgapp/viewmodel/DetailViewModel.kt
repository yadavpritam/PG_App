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
class DetailViewModel @Inject constructor(
    private val repo: PgRepository,
    private val firebase: FirebaseService
) : ViewModel() {

    var pg = mutableStateOf<PgModel?>(null)
        private set

    var isLoading = mutableStateOf(false)
        private set

    var error = mutableStateOf<String?>(null)

    var isSaved = mutableStateOf(false)

    // LOAD PG DETAIL
    fun loadPg(id: String) {
        viewModelScope.launch {
            try {
                isLoading.value = true
                error.value = null

                val data = repo.getPgById(id)
                pg.value = data

                checkIfSaved(id)

            } catch (e: Exception) {
                error.value = e.message
            } finally {
                isLoading.value = false
            }
        }
    }

    // CHECK SAVED STATUS
    private suspend fun checkIfSaved(pgId: String) {
        val userId = firebase.getCurrentUserId() ?: return

        val savedList = repo.getSavedPgs(userId)
        isSaved.value = savedList.any { it.id == pgId }
    }

    // TOGGLE SAVE
    fun toggleSave() {
        val userId = firebase.getCurrentUserId() ?: return
        val pgId = pg.value?.id ?: return

        viewModelScope.launch {
            repo.toggleSave(pgId, userId)
            isSaved.value = !isSaved.value
        }
    }

    // REFRESH
    fun refresh() {
        pg.value?.id?.let { loadPg(it) }
    }
}