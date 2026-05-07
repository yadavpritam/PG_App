package com.example.pgapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pgapp.data.model.pgmodel.FilterModel
import com.example.pgapp.data.model.pgmodel.PgModel
import com.example.pgapp.data.repository.PgRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val repo: PgRepository
) : ViewModel() {

    var filteredList = mutableStateListOf<PgModel>()
        private set

    var isLoading = mutableStateOf(false)
    var error = mutableStateOf<String?>(null)

    var currentFilter = mutableStateOf(FilterModel())

    // APPLY FILTER
    fun applyFilter(filter: FilterModel) {

        currentFilter.value = filter

        viewModelScope.launch {
            try {
                isLoading.value = true
                error.value = null

                val result = repo.getFilteredPgs(filter)

                filteredList.clear()
                filteredList.addAll(result)

            } catch (e: Exception) {
                error.value = e.message
            } finally {
                isLoading.value = false
            }
        }
    }

    // RESET FILTER
    fun resetFilter() {
        currentFilter.value = FilterModel()
        applyFilter(currentFilter.value)
    }

    // INITIAL LOAD (without filter)
    fun loadAll() {
        viewModelScope.launch {
            isLoading.value = true
            val result = repo.getAllPgs()
            filteredList.clear()
            filteredList.addAll(result)
            isLoading.value = false
        }
    }
}