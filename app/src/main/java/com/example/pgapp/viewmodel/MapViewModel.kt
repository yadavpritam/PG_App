package com.example.pgapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pgapp.data.model.pgmodel.PgModel
import com.example.pgapp.data.repository.PgRepository
import com.example.pgapp.utils.LocationHelper
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val repo: PgRepository,
    private val locationHelper: LocationHelper
) : ViewModel() {

    var pgList = mutableStateListOf<PgModel>()
        private set

    var selectedPg = mutableStateOf<PgModel?>(null)
        private set

    var userLocation = mutableStateOf<LatLng?>(null)
        private set

    var isLoading = mutableStateOf(false)
    var error = mutableStateOf<String?>(null)

    // MAIN LOAD FUNCTION
    fun loadData() {
        viewModelScope.launch {

            try {
                isLoading.value = true
                error.value = null

                // PG DATA
                val data = repo.getAllPgs()
                pgList.clear()
                pgList.addAll(data)

                // USER LOCATION
                val location = locationHelper.getCurrentLocation()
                location?.let {
                    userLocation.value = LatLng(it.latitude, it.longitude)
                }

            } catch (e: Exception) {
                error.value = e.message
            } finally {
                isLoading.value = false
            }
        }
    }

    // MARKER CLICK
    fun onMarkerClick(pg: PgModel) {
        selectedPg.value = pg
    }

    // CLEAR SELECTION
    fun clearSelection() {
        selectedPg.value = null
    }

    // REFRESH DATA
    fun refresh() {
        loadData()
    }

    // FILTER APPLY (future use)
    fun applyFilter(filteredList: List<PgModel>) {
        pgList.clear()
        pgList.addAll(filteredList)
    }
}