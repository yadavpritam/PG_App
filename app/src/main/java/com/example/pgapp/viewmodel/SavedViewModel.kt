package com.example.pgapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
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
class SavedViewModel @Inject constructor(
    private val repo: PgRepository,
    private val firebase: FirebaseService
) : ViewModel() {

    var savedList = mutableStateListOf<PgModel>()
        private set

    var isLoading = mutableStateOf(false)
    var error = mutableStateOf<String?>(null)

    fun loadSavedPgs() {
        viewModelScope.launch {
            try {
                isLoading.value = true
                error.value = null

                val userId = firebase.getCurrentUserId() ?: return@launch

                val result = repo.getSavedPgs(userId)

                savedList.clear()
                savedList.addAll(result)

            } catch (e: Exception) {
                error.value = e.message
            } finally {
                isLoading.value = false
            }
        }
    }

    fun removeSaved(pgId: String) {
        viewModelScope.launch {
            val userId = firebase.getCurrentUserId() ?: return@launch
            repo.toggleSave(pgId, userId)
            loadSavedPgs()
        }
    }
}