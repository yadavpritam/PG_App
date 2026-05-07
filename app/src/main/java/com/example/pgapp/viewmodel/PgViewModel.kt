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
class PgViewModel @Inject constructor(
    private val repo: PgRepository
) : ViewModel() {

    var pgList = mutableStateListOf<PgModel>()
        private set

    var isLoading = mutableStateOf(false)
    var error = mutableStateOf<String?>(null)

    fun loadPgs() {
        viewModelScope.launch {
            try {
                isLoading.value = true
                error.value = null

                val data = repo.getAllPgs()
                pgList.clear()
                pgList.addAll(data)

            } catch (e: Exception) {
                error.value = e.message
            } finally {
                isLoading.value = false
            }
        }
    }
}